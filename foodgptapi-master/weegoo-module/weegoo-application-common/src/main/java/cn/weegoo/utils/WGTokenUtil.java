package cn.weegoo.utils;

import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

public class WGTokenUtil {

    public static String getToken(String userId) {
        String token = "";
        token = JWT.create().withAudience(userId)
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 30L))        //设置过期时间，30L天
                .sign(Algorithm.HMAC256("WeeGoo"));
        return token;
    }

    public static String getIp(String token) {
        return JWT.decode(token).getAudience().get(1);
    }

    public static String getUserId(){
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();//获取当前绑定的对象(绑定的线程)
        HttpServletRequest request = requestAttributes == null ? null : ((ServletRequestAttributes)requestAttributes).getRequest();

        String token = request.getHeader("token");
        if (StrUtil.isBlank(token)){
            return null;
        }
        //校验token
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256("WeeGoo")).build();
        try {
            jwtVerifier.verify(token);
        } catch (JWTVerificationException e) {
            return null;
        }

        return JWT.decode(token).getAudience().get(0);
    }
}
