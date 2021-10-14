package top.parak.gulimall.coupon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * 优惠服务启动器
 *
 * @author KHighness
 * @email parakovo@gmail.com
 * @date 2021-02-25 10:35:39
 */
@RefreshScope
@SpringBootApplication
@EnableDiscoveryClient
public class GulimallCouponApplication {
    public static void main(String[] args) {
        SpringApplication.run(GulimallCouponApplication.class, args);
    }
}
