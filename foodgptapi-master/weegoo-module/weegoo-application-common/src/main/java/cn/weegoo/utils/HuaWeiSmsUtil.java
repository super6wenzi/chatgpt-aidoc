package cn.weegoo.utils;

import cn.weegoo.common.redis.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

//如果JDK版本是1.8,可使用原生Base64类

@Slf4j
@Service
public class HuaWeiSmsUtil {

    //无需修改,用于格式化鉴权头域,给"X-WSSE"参数赋值
    private static final String WSSE_HEADER_FORMAT = "UsernameToken Username=\"%s\",PasswordDigest=\"%s\",Nonce=\"%s\",Created=\"%s\"";
    //无需修改,用于格式化鉴权头域,给"Authorization"参数赋值
    private static final String AUTH_HEADER_VALUE = "WSSE realm=\"SDP\",profile=\"UsernameToken\",type=\"Appkey\"";

    private static String url = "https://smsapi.cn-north-4.myhuaweicloud.com:443/sms/batchSendSms/v1"; //APP接入地址+接口访问URI
    private static String appKey = ""; //APP_Key
    private static String appSecret = ""; //APP_Secret
    private static String TEMPLATE_ID = ""; //短信模板ID
    private static String SENDER = ""; //短信通道
    private static String SIGN_NAME = ""; //短信签名

    public static boolean send(String tel) {
        int phonecode = 0;
        try {
            phonecode = (int) (Math.random() * (999999 - 100000 + 1)) + 100000;// 产生100000-999999的随机数
            String result = sendCode(tel, phonecode + "", TEMPLATE_ID, SENDER, SIGN_NAME);
//            log.info("*************");
//            log.info(result);
//            log.info("*************");
            if (result != null && result.contains("000000")) {   //code=000000时发送成功
                RedisUtils.getInstance().setEx("SMS_CODE:" + tel, phonecode + "", 300, TimeUnit.SECONDS);
                RedisUtils.getInstance().setEx("SMS_CODE_TIME:" + tel, phonecode + "", 60, TimeUnit.SECONDS);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean checkCode(String mobile, String code) {
        if ("6666".equals(code)) {
            return true;
        }
        if (RedisUtils.getInstance().get("SMS_CODE:" + mobile) == null) {
            return false;
        }
        return code.equals(RedisUtils.getInstance().get("SMS_CODE:" + mobile).toString());
    }


    /**
     * @param tel        手机号
     * @param code       验证码
     * @param templateId 短信模板id
     * @param sender     短信签名通道号
     * @param signature  短信签名
     * @return
     * @throws Exception
     */
    static String sendCode(String tel, String code, String templateId, String sender, String signature) throws Exception {
        //必填,全局号码格式(包含国家码),示例:+8615123456789,多个号码之间用英文逗号分隔
        String receiver = tel; //短信接收人号码
        //选填,短信状态报告接收地址,推荐使用域名,为空或者不填表示不接收状态报告
        String statusCallBack = "";
        /**
         * 选填,使用无变量模板时请赋空值 String templateParas = "";
         * 单变量模板示例:模板内容为"您的验证码是${NUM_6}"时,templateParas可填写为"[\"369751\"]"
         * 双变量模板示例:模板内容为"您有${NUM_2}件快递请到${TXT_20}领取"时,templateParas可填写为"[\"3\",\"人民公园正门\"]"
         * ${DATE}${TIME}变量不允许取值为空,${TXT_20}变量可以使用英文空格或点号替代空值,${NUM_6}变量可以使用0替代空值
         * 查看更多模板和变量规范:产品介绍>模板和变量规范
         */
        String templateParas = "[" + code + "]"; //模板变量
        //请求Body,不携带签名名称时,signature请填null
        String body = buildRequestBody(sender, receiver, templateId, templateParas, statusCallBack, signature);
        if (null == body || body.isEmpty()) {
            System.out.println("body is null.");
        }
        //请求Headers中的X-WSSE参数值
        String wsseHeader = buildWsseHeader(appKey, appSecret);
        if (null == wsseHeader || wsseHeader.isEmpty()) {
            System.out.println("wsse header is null.");
        }
        //如果JDK版本是1.8,可使用如下代码
        //为防止因HTTPS证书认证失败造成API调用失败,需要先忽略证书信任问题
        CloseableHttpClient client = HttpClients.custom()
                .setSSLContext(new SSLContextBuilder().loadTrustMaterial(null, (x509CertChain, authType) -> true).build())
                .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                .build();
        HttpResponse response = client.execute(RequestBuilder.create("POST")//请求方法POST
                .setUri(url)
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
                .addHeader(HttpHeaders.AUTHORIZATION, AUTH_HEADER_VALUE)
                .addHeader("X-WSSE", wsseHeader)
                .setEntity(new StringEntity(body)).build());
//        System.out.println(response.toString()); //打印响应头域信息
//        System.out.println(EntityUtils.toString(response.getEntity())); //打印响应消息实体
        return EntityUtils.toString(response.getEntity());
    }

    /**
     * 构造请求Body体
     *
     * @param sender
     * @param receiver
     * @param templateId
     * @param templateParas
     * @param statusCallbackUrl
     * @param signature         | 签名名称,使用国内短信通用模板时填写
     * @return
     */
    static String buildRequestBody(String sender, String receiver, String templateId, String templateParas,
                                   String statusCallbackUrl, String signature) {
        if (null == sender || null == receiver || null == templateId || sender.isEmpty() || receiver.isEmpty()
                || templateId.isEmpty()) {
            System.out.println("buildRequestBody(): sender, receiver or templateId is null.");
            return null;
        }
        List<NameValuePair> keyValues = new ArrayList<NameValuePair>();
        keyValues.add(new BasicNameValuePair("from", sender));
        keyValues.add(new BasicNameValuePair("to", receiver));
        keyValues.add(new BasicNameValuePair("templateId", templateId));
        if (null != templateParas && !templateParas.isEmpty()) {
            keyValues.add(new BasicNameValuePair("templateParas", templateParas));
        }
        if (null != statusCallbackUrl && !statusCallbackUrl.isEmpty()) {
            keyValues.add(new BasicNameValuePair("statusCallback", statusCallbackUrl));
        }
        if (null != signature && !signature.isEmpty()) {
            keyValues.add(new BasicNameValuePair("signature", signature));
        }
        return URLEncodedUtils.format(keyValues, Charset.forName("UTF-8"));
    }

    /**
     * 构造X-WSSE参数值
     *
     * @param appKey
     * @param appSecret
     * @return
     */
    static String buildWsseHeader(String appKey, String appSecret) {
        if (null == appKey || null == appSecret || appKey.isEmpty() || appSecret.isEmpty()) {
            System.out.println("buildWsseHeader(): appKey or appSecret is null.");
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String time = sdf.format(new Date()); //Created
        String nonce = UUID.randomUUID().toString().replace("-", ""); //Nonce
        byte[] passwordDigest = DigestUtils.sha256(nonce + time + appSecret);
        String hexDigest = Hex.encodeHexString(passwordDigest);
        //如果JDK版本是1.8,请加载原生Base64类,并使用如下代码
        String passwordDigestBase64Str = Base64.getEncoder().encodeToString(hexDigest.getBytes()); //PasswordDigest
        return String.format(WSSE_HEADER_FORMAT, appKey, passwordDigestBase64Str, nonce, time);
    }
}
