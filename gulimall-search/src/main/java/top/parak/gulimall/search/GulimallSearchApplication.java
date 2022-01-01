package top.parak.gulimall.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * 检索服务启动器
 *
 * @author KHighness
 * @since 2021-11-02
 * @email parakovo@gmail.com
 */
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "top.parak.gulimall.search.feign")
@EnableRedisHttpSession
@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class
})
public class GulimallSearchApplication {
    public static void main(String[] args) {
        SpringApplication.run(GulimallSearchApplication.class, args);
    }
}
