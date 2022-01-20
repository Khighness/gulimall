package top.parak.gulimall.coupon.config;

import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis配置
 *
 * @author KHighness
 * @since 2022-01-20
 * @email parakovo@gmial.com
 */
@Configuration
@MapperScan("top.parak.gulimall.coupon.dao")
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
