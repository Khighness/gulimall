package top.parak.gulimall.seckill.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * RabbitMQ配置
 *
 * @author KHighness
 * @since 2022-01-30
 * @email parakovo@gmail.com
 */
@Configuration
public class GulimallRabbitConfig {

    @Component
    @ConfigurationProperties(prefix = "gulimall.rabbitmq.seckill")
    @Data
    public static class GulimallRabbitSeckillProperties {

        private String exchange;

        private String routingKey;

    }

}
