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
 * @email parakovo@gmail.com
 * @date 2021-02-25 11:22:40
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
