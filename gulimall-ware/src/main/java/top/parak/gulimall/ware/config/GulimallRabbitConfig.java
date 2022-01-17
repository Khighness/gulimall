package top.parak.gulimall.ware.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * RabbitMQ配置
 *
 * @author KHighness
 * @since 2022-01-16
 * @email parakovo@gmail.com
 */
@Configuration
@Slf4j
public class GulimallRabbitConfig {

    @Component
    @ConfigurationProperties(prefix = "gulimall.rabbitmq.stock")
    @Data
    public static class GulimallRabbitStockProperties {

        /**
         * 业务交换机
         */
        private String eventExchange;

        /**
         * 存活时间
         */
        private Integer ttl;

        /**
         * 延迟队列
         */
        private String delayQueue;

        /**
         * 锁库存路由
         */
        private String lockStockRoutingKey;

        /**
         * 死信队列
         */
        private String deadQueue;

        /**
         * 解锁库存路由
         */
        private String releaseStockRoutingKey;

    }

    @Autowired
    private GulimallRabbitStockProperties rabbitStockProperties;

    @Bean
    public Exchange stockEventExchange() {
        return ExchangeBuilder.topicExchange(rabbitStockProperties.getEventExchange()).durable(true).build();
    }

    @Bean
    public Queue stockDelayQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", rabbitStockProperties.getEventExchange());
        args.put("x-dead-letter-routing-key", rabbitStockProperties.getReleaseStockRoutingKey());
        args.put("x-message-ttl", rabbitStockProperties.getTtl());
        return new Queue(rabbitStockProperties.getDelayQueue(), true, false, false, args);
    }

    @Bean
    public Queue stockDeadQueue() {
        return new Queue(rabbitStockProperties.getDeadQueue(), true, false, false);
    }

    @Bean
    public Binding stockLockStockBinding() {
        return new Binding(rabbitStockProperties.getDelayQueue(), Binding.DestinationType.QUEUE,
                rabbitStockProperties.getEventExchange(), rabbitStockProperties.getLockStockRoutingKey(),
                null);
    }

    @Bean
    public Binding orderReleaseStockBinding() {
        return new Binding(rabbitStockProperties.getDeadQueue(), Binding.DestinationType.QUEUE,
                rabbitStockProperties.getEventExchange(), rabbitStockProperties.getReleaseStockRoutingKey(),
                null);
    }

}
