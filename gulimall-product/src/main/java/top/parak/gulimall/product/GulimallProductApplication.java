package top.parak.gulimall.product;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 商品服务启动器
 *
 * @author KHighness
 * @since  2021-09-25
 * @email parakovo@gmail.com
 */
@EnableCaching
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "top.parak.gulimall.product.feign")
@EnableRedisHttpSession
@EnableTransactionManagement
@SpringBootApplication
public class GulimallProductApplication {
    public static void main(String[] args) {
        SpringApplication gulimallProductApplication = new SpringApplicationBuilder(GulimallProductApplication.class)
                .web(WebApplicationType.SERVLET)
                .bannerMode(Banner.Mode.CONSOLE)
                .application();
        ConfigurableApplicationContext applicationContext = gulimallProductApplication.run(args);
    }
}
