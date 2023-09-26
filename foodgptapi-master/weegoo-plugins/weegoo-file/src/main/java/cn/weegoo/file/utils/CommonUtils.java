package cn.weegoo.file.utils;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

public class CommonUtils {

    /**
     * 判断文件名是否带盘符，重新处理
     *
     * @param fileName
     * @return
     */
    public static String getFileName(String fileName) {
        //判断是否带有盘符信息
        // Check for Unix-style path
        int unixSep = fileName.lastIndexOf ( '/' );
        // Check for Windows-style path
        int winSep = fileName.lastIndexOf ( '\\' );
        // Cut off at latest possible point
        int pos = (winSep > unixSep ? winSep : unixSep);
        if ( pos != -1 ) {
            // Any sort of path separator found...
            fileName = fileName.substring ( pos + 1 );
        }
        //替换上传文件名字的特殊字符
        fileName = fileName.replace ( "=", "" ).replace ( ",", "" ).replace ( "&", "" ).replace ( " ", "" );
        return fileName;
    }

    /**
     * 获取服务的根路径
     *
     * @return
     */
    public static String getContextPath() {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes ( );
        HttpServletRequest request = ((ServletRequestAttributes) ra).getRequest ( );
        ServletContext context = request.getSession ( ).getServletContext ( );
        return context.getRealPath ( "/" );
    }

}
