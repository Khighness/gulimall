package top.parak.gulimall.member;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 会员服务启动器
 *
 * @author KHighness
 * @since 2021-09-25
 * @email parakovo@gmail.com
 */
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "top.parak.gulimall.member.feign")
@EnableRedisHttpSession
@EnableTransactionManagement
@SpringBootApplication
public class GulimallMemberApplication {
    public static void main(String[] args) {
        SpringApplication.run(GulimallMemberApplication.class, args);
    }
}
