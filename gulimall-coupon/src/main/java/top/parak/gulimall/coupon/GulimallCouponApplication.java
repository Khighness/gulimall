package top.parak.gulimall.coupon;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author KHighness
 * @since 2021-10-16
 * @email parakovo@gmail.com
 */
@EnableDiscoveryClient
@EnableTransactionManagement
@SpringBootApplication
public class GulimallCouponApplication {
    public static void main(String[] args) {
        SpringApplication.run(GulimallCouponApplication.class);
    }
}
