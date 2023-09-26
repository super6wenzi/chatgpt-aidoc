package cn.weegoo.config;

import cn.weegoo.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author weegoo
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Bean
    public LoginInterceptor setLoginInterceptor(){
        return new LoginInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(setLoginInterceptor())
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api/user/wxLogin")
                .excludePathPatterns("/api/foodBrand/asd")

                .excludePathPatterns("/api/file/**")
                .excludePathPatterns("/api/chatGPT/**")
                .excludePathPatterns("/api/foodBuildType/**");
    }


}