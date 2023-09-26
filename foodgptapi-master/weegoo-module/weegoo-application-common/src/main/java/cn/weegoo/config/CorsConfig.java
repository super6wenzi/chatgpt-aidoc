package cn.weegoo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 跨域配置
 * @author weegoo
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 允许该路径的跨域请求
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowCredentials(true)
                // 允许跨域请求通过的类型
                .allowedMethods("GET", "POST", "DELETE", "OPTIONS")
                // 缓存时间, 60分钟内的其他跨域请求不再校验
                .maxAge(3600);
    }
}