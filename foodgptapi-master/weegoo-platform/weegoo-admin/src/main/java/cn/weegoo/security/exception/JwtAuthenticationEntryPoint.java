package cn.weegoo.security.exception;

import cn.weegoo.common.redis.RedisUtils;
import cn.weegoo.core.errors.ErrorConstants;
import cn.weegoo.security.jwt.TokenProvider;
import cn.weegoo.sys.constant.CacheNames;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;

/**
 * 认证失败处理类，返回401
 * Author: liugaofeng
 */
@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

    private static final long serialVersionUID = -8970718410437077606L;

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        //验证为未登陆状态会进入此方法，认证错误
        String errMsg;
        String token = TokenProvider.resolveToken ( request );
        if ( StringUtils.isEmpty ( token ) ) { // 没有token抛出的异常
            errMsg = ErrorConstants.LOGIN_ERROR_NOT_LOGIN_IN;
            response.setStatus ( HttpStatus.UNAUTHORIZED.value ( ) );
        } else { // token过期抛出的异常
            Object accessTokenObj = RedisUtils.getInstance ( ).get ( CacheNames.USER_CACHE_TOKEN + TokenProvider.getLoginName ( token ) + ":" + token );
            if ( accessTokenObj != null && accessTokenObj.toString ( ).equals ( "kickOut" ) ) {
                RedisUtils.getInstance ( ).delete ( CacheNames.USER_CACHE_TOKEN + TokenProvider.getLoginName ( token ) + ":" + token );
                errMsg = ErrorConstants.LOGIN_ERROR__KICK_OUT_LOGGED_IN_ELSEWHERE;
            } else {
                errMsg = ErrorConstants.LOGIN_ERROR_EXPIRED;
            }
            response.setStatus ( HttpStatus.REQUEST_TIMEOUT.value ( ) );
        }
        log.error ( errMsg );
        response.setCharacterEncoding ( "UTF-8" );
        response.setContentType ( "application/json; charset=utf-8" );
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        PrintWriter printWriter = response.getWriter ( );
        printWriter.write ( errMsg );
        printWriter.flush ( );
    }
}
