package cn.weegoo.database;

import cn.weegoo.database.datalink.domain.DataSource;
import cn.weegoo.database.datalink.jdbc.DBPool;
import cn.weegoo.database.datalink.service.DataSourceService;
import cn.weegoo.database.datalink.service.dto.DataSourceDTO;
import cn.weegoo.database.datalink.service.mapstruct.DataSourceWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DataSourceConfig implements ApplicationRunner {

    /**
     * 主数据源
     */
    @Value("${spring.datasource.dynamic.primary}")
    public String primary;
    @Autowired
    javax.sql.DataSource dataSource;
    @Autowired
    DBPool dbPool;
    private Logger logger = LoggerFactory.getLogger ( DataSourceConfig.class );
    @Autowired
    private DataSourceService dataSourceService;

    @Override
    public void run(ApplicationArguments args) {

        //获取数据库中定义的数据源, 启动完成后自动加载

        List <DataSourceDTO> list = DataSourceWrapper.INSTANCE.toDTO ( dataSourceService.lambdaQuery ( ).eq ( DataSource::isEnable, true ).list ( ) );
        for (DataSourceDTO dataLink : list) {
            try {
                dbPool.addDs ( dataLink );
            } catch (Exception e) {
                logger.error ( "加载数据库【" + dataLink.getPollName ( ) + "】连接失败" );
            }
        }

        logger.debug ( "多数据插件加载完成!" );
    }
}

