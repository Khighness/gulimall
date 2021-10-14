package top.parak.gulimall.product.config;

import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author KHighness
 * @since 2021-10-14
 */
@Configuration
@EnableTransactionManagement
@MapperScan("top.parak.gulimall.product.dao")
public class MyBatisConfig {

    @Bean
    public PaginationInnerInterceptor paginationInterceptor() {
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
        // 设置请求的页面大于最大页后操作，true调回首页，false继续请求，默认false
        paginationInnerInterceptor.setOverflow(true);
        // 设置最大单页数量,，-1不受限制
        paginationInnerInterceptor.setMaxLimit(1000L);
        return paginationInnerInterceptor;
    }
}
