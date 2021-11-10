package top.parak.gulimall.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 网关服务启动器
 *
 * @author KHighness
 * @email parakovo@gmial.com
 * @date 2021-09-27 00:26:45
 */
@SpringBootApplication
@EnableDiscoveryClient
public class KHighnessGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(KHighnessGatewayApplication.class, args);
    }
}
