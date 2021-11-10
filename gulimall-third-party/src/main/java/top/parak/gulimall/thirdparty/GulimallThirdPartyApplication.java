package top.parak.gulimall.thirdparty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 第三方服务启动器
 *
 * @author KHighness
 * @email parakovo@gmial.com
 * @date 2021-09-27 00:26:45
 */
@EnableDiscoveryClient
@SpringBootApplication
public class GulimallThirdPartyApplication {
    public static void main(String[] args) {
        SpringApplication.run(GulimallThirdPartyApplication.class, args);
    }
}
