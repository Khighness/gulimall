package top.parak.gulimall.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 订单服务启动器
 *
 * @author KHighness
 * @since 2021-09-25
 * @email parakovo@gmail.com
 */
@SpringBootApplication
@EnableTransactionManagement
@EnableDiscoveryClient
@MapperScan("top.parak.gulimall.order.dao")
public class GulimallOrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(GulimallOrderApplication.class, args);
    }
}
