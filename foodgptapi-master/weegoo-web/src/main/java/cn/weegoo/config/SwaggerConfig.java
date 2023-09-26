package cn.weegoo.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.github.xiaoymin.knife4j.spring.extension.OpenApiExtensionResolver;
import com.google.common.base.Predicate;
import cn.weegoo.security.jwt.TokenProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import com.google.common.base.Function;
import com.google.common.base.Optional;


/**
 * Swagger配置类
 *
 * @author weegoo
 */
@Configuration
@EnableKnife4j
@AllArgsConstructor
@ConditionalOnProperty(name = "knife4j.enable", havingValue = "true")
public class SwaggerConfig {

    // 定义分隔符,配置Swagger多包
    private static final String SPLIT_CODE = ";";
    private final String BASE_PACKAGES = "cn.weegoo";

    /**
     * 引入Knife4j扩展类
     */
    private final OpenApiExtensionResolver openApiExtensionResolver;

    @Bean
    public Docket sysDocket() {
        return docket("系统模块", BASE_PACKAGES + ".sys.controller");
    }

    @Bean
    public Docket applicationDocket() {
        return docket("业务管理端模块", BASE_PACKAGES + ".application.controller");
    }
    
    @Bean
    public Docket applicationApiDocket() {
        return docket("业务客户端模块", BASE_PACKAGES + ".food.front");
    }
    
    private Docket docket(String groupName, String basePackages) {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName(groupName)
                .apiInfo(apiInfo())
                .select()
                .apis(basePackage(basePackages))
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build();
//                .securitySchemes(Collections.singletonList(securityScheme()));

    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("WeeGooPlatform")
                .description("接口文档")
                .contact(new Contact("WeeGoo", "http://weegoo.cn", "dev@weegoo.cn"))
                .version("v1.0")
//                .contact(new Contact("weegoo", "http://www.weegoo.org", "platform@weegoo.cn"))
//                .license("The Apache License, Version 2.0")
//                .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
                .build();
    }

    /**
     * 获取包集合
     *
     * @param basePackage 多个包名集合
     */
    public static Predicate<RequestHandler> basePackage(final String basePackage) {
        return input -> declaringClass(input).transform(handlerPackage(basePackage)).or(true);
    }

    private static Function<Class<?>, Boolean> handlerPackage(final String basePackage) {
        return input -> {
            // 循环判断匹配
            for (String strPackage : basePackage.split(SPLIT_CODE)) {
                boolean isMatch = input.getPackage().getName().startsWith(strPackage);
                if (isMatch) {
                    return true;
                }
            }
            return false;
        };
    }

    /***
     * swagger token 配置。
     */
    @Bean
    SecurityScheme securityScheme() {
        return new ApiKey(TokenProvider.TOKEN, TokenProvider.TOKEN, "header");
    }

    private static Optional<? extends Class<?>> declaringClass(RequestHandler input) {
        return Optional.fromNullable(input.declaringClass());
    }

}