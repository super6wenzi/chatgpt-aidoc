package cn.weegoo.utils;

import cn.hutool.http.HttpUtil;
import cn.weegoo.sys.utils.Constant;
import net.sf.json.JSONObject;

public class WxUtils {

    /**
     * 根据code获取openId
     * @return
     */
    public static String getOpenIdByCode(String code) throws Exception{
        String strUrl="https://api.weixin.qq.com/sns/jscode2session?appid="+ Constant.WX_APPID+"&secret="+Constant.WX_SECTET+"&js_code="+code+"&grant_type=authorization_code";
        String res = HttpUtil.get(strUrl);
        System.out.println(res);
        JSONObject jsonObject = JSONObject.fromObject(res);
        return jsonObject.getString("openid");
    }
}
