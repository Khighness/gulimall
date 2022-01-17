package top.parak.gulimall.order.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.zaxxer.hikari.HikariDataSource;
//import io.seata.rm.datasource.DataSourceProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;

/**
 * Seata配置
 *
 * @author KHighness
 * @since 2022-01-15
 * @email parakovo@gmail.com
 */
//@Configuration
public class GulimalSeataConfig {

//    @Bean
//    public DataSource dataSource(DataSourceProperties dataSourceProperties) {
//        DruidDataSource dataSource = dataSourceProperties.initializeDataSourceBuilder().type(DruidDataSource.class).build();
//
//        return new DataSourceProxy(dataSource);
//
//    }

}
