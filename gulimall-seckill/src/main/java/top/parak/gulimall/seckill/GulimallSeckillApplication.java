package top.parak.gulimall.seckill;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * 秒杀服务启动器
 *
 * @author KHighness
 * @since 2022-01-20
 * @email parakovo@gmail.com
 */
@EnableDiscoveryClient
@EnableFeignClients
@EnableRedisHttpSession
@SpringBootApplication
public class GulimallSeckillApplication {
    public static void main(String[] args) {
        SpringApplication.run(GulimallSeckillApplication.class, args);
    }
}
