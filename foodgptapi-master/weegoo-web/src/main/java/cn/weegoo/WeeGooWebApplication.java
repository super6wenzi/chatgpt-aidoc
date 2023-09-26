package cn.weegoo;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;


@ServletComponentScan("cn.weegoo")
@SpringBootApplication(scanBasePackages = {"cn.weegoo","com.jeeplus", "cn.hutool.extra.spring", "org.flowable.ui.modeler", "org.flowable.ui.common"},
        exclude = {
                DruidDataSourceAutoConfigure.class,
                org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class
        }
)
public class WeeGooWebApplication extends SpringBootServletInitializer {

    // 其中 dataSource 框架会自动为我们注入
    @Bean
    public PlatformTransactionManager txManager(DataSource dataSource) {
        return new DataSourceTransactionManager ( dataSource );
    }

    //
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        // 注意这里要指向原先用main方法执行的Application启动类
        return builder.sources ( WeeGooWebApplication.class );
    }

    public static void main(String[] args) {
        SpringApplication.run ( WeeGooWebApplication.class, args );
    }

}
