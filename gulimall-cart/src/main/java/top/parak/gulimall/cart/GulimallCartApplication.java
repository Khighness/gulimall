package top.parak.gulimall.cart;

import org.springframework.boot.Banner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * @author KHighness
 * @since 2022-01-02
 * @email parakovo@gmail.com
 */
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "top.parak.gulimall.cart.feign")
@EnableRedisHttpSession
@SpringBootApplication(
        exclude = { DataSourceAutoConfiguration.class }
)
public class GulimallCartApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(GulimallCartApplication.class)
                .web(WebApplicationType.SERVLET)
                .bannerMode(Banner.Mode.CONSOLE)
                .run(args);
    }
}
