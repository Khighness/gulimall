package top.parak.gulimall.ware.config;

import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author KHighness
 * @since 2021-10-28
 * @email parakovo@gmial.com
 */
@Configuration
@MapperScan("top.parak.gulimall.ware.dao")
public class GulimallBatisConfig {

    @Bean
    public PaginationInnerInterceptor paginationInterceptor() {
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
//        // 设置请求的页面大于最大页后操作，true调回首页，false继续请求，默认false
//        paginationInnerInterceptor.setOverflow(true);
//        // 设置最大单页数量,，-1不受限制
//        paginationInnerInterceptor.setMaxLimit(1000L);
        return paginationInnerInterceptor;
    }

}
