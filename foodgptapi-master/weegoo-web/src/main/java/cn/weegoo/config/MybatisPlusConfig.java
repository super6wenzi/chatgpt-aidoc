package cn.weegoo.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.DataPermissionInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.mapping.VendorDatabaseIdProvider;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * @author weegoo
 */
@Configuration
@MapperScan({"cn.weegoo.**.mapper.**", "com.jeeplus.**.mapper.**"})
public class MybatisPlusConfig {


    /**
     * 新的分页插件,一缓和二缓遵循mybatis的规则,需要设置 MybatisConfiguration#useDeprecatedExecutor = false 避免缓存出现问题(该属性会在旧插件移除后一同移除)
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {

        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor ( );
        // 添加自定义的数据权限处理器
        interceptor.addInnerInterceptor ( new DataPermissionInterceptor ( new DataPermissionHandlerImpl ( ) ) );
        // 添加分页插件
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor ( DbType.MYSQL );
        paginationInnerInterceptor.setOverflow ( true );
        interceptor.addInnerInterceptor ( paginationInnerInterceptor );
        return interceptor;
    }


    @Bean
    public ConfigurationCustomizer configurationCustomizer() {
        return configuration -> {
            configuration.setJdbcTypeForNull ( JdbcType.NULL );
        };
    }

    @Bean
    public DatabaseIdProvider getDatabaseIdProvider() {
        DatabaseIdProvider databaseIdProvider = new VendorDatabaseIdProvider ( );
        Properties properties = new Properties ( );
        properties.setProperty ( "Oracle", "oracle" );
        properties.setProperty ( "MySQL", "mysql" );
        properties.setProperty ( "DB2", "db2" );
        properties.setProperty ( "Derby", "derby" );
        properties.setProperty ( "H2", "h2" );
        properties.setProperty ( "HSQL", "hsql" );
        properties.setProperty ( "Informix", "informix" );
        properties.setProperty ( "MS-SQL", "mssql" );
        properties.setProperty ( "SQL Server", "mssql" );
        properties.setProperty ( "PostgreSQL", "postgre" );
        properties.setProperty ( "Sybase", "sybase" );
        properties.setProperty ( "Hana", "hana" );
        databaseIdProvider.setProperties ( properties );
        return databaseIdProvider;
    }
}
