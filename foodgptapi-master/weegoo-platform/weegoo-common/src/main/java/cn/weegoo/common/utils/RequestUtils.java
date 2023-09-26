package cn.weegoo.common.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class RequestUtils {

    /**
     * 获取请求Request
     *
     * @return
     */
    public static HttpServletRequest getRequest() {
        if ( RequestContextHolder.getRequestAttributes ( ) == null ) {
            return null;
        }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes ( )).getRequest ( );
        return request;
    }

    /**
     * 获取请求header中的值
     *
     * @return
     */
    public static String getHeader(String key) {
        if ( RequestContextHolder.getRequestAttributes ( ) == null ) {
            return null;
        }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes ( )).getRequest ( );
        if ( request != null ) {
            return request.getHeader ( key );
        }
        return null;
    }

    /**
     * 获取token，支持三种方式, 请求参数、header、cookie， 优先级依次降低，以请求参数中的优先级最高。
     *
     * @param httpServletRequest
     * @return
     */
    public static String resolveToken(HttpServletRequest httpServletRequest, String token) {
        if ( httpServletRequest == null ) {
            return null;
        }
        String token0 = httpServletRequest.getParameter ( token );
        String token1 = httpServletRequest.getHeader ( token );
        Cookie token2 = ServletUtil.getCookie ( httpServletRequest, token );
        if ( StrUtil.isNotBlank ( token0 ) ) {
            return token0;
        }
        if ( StrUtil.isNotBlank ( token1 ) ) {
            return token1;
        }
        if ( token2 != null && StrUtil.isNotBlank ( token2.getValue ( ) ) ) {
            return token2.getValue ( );
        }
        return null;
    }
}
