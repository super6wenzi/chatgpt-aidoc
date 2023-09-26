/**
 * Copyright &copy; 2021-2026 <a href="http://www.weegoo.org/">weegoo</a> All rights reserved.
 */
package cn.weegoo.config.properties;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.Serializable;

/**
 * 全局配置类
 *
 * @author weegoo
 * @version 2021-06-25
 */
@Data
@Configuration
public class WeeGooProperties implements Serializable {

    /**
     * 上传文件基础虚拟路径
     */
    public static final String USERFILES_BASE_URL = "/userdir/";
    private static final long serialVersionUID = 1L;
    /**
     * 超时时间
     */
    @Value("${jwt.accessToken.expireTime}")
    public long EXPIRE_TIME;
    /**
     * 文件存储路径
     */
    @Value("${config.accessory.local.location}")
    private String userfilesBasedir;

    public static WeeGooProperties newInstance() {

        return SpringUtil.getBean ( WeeGooProperties.class );
    }


    /**
     * 获取上传文件的根目录
     *
     * @return
     */
    public String getUserfilesBaseDir() {
        String dir = this.userfilesBasedir;
        if ( StrUtil.isBlank ( dir ) ) {
            try {
                return new File ( this.getClass ( ).getResource ( "/" ).getPath ( ) )
                        .getParentFile ( ).getParentFile ( ).getParentFile ( ).getCanonicalPath ( );
            } catch (Exception e) {
                return "";
            }
        }
        if ( !dir.endsWith ( "/" ) ) {
            dir += "/";
        }

        return dir;
    }


}
