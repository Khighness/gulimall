package top.parak.gulimall.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author KHighness
 * @since 2021-11-02
 */
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "top.parak.gulimall.search.feign")
@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class
})
public class GulimallSearchApplication {
    public static void main(String[] args) {
        SpringApplication.run(GulimallSearchApplication.class, args);
    }
}
