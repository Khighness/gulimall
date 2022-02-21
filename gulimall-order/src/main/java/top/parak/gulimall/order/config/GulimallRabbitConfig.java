package top.parak.gulimall.order.config;

import lombok.Data;
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
public class GulimallRabbitConfig {

    /**
     * <b>NOTE 订单消息的生命周期</b>
     * <ol>
     * <li>创建订单的消息被投入业务交换机</li>
     * <li>订单消息被路由到延迟队列中</li>
     * <li>订单超时没有支付，成为死信</li>
     * <li>订单信息被投入死信交换机</li>
     * <li>订单通过死信路由到死信队列</li>
     * <li>死信队列的消费者取消订单</li>
     * </ol>
     *
     * 谷粒商城的业务将业务交换机和死信交换机合二为一.
     */
    @Component
    @ConfigurationProperties(prefix = "gulimall.rabbitmq.order")
    @Data
    public static class GulimallRabbitOrderProperties {

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
         * 创建订单路由
         */
        private String createOrderRoutingKey;

        /**
         * 死信队列
         */
        private String deadQueue;

        /**
         * 关闭订单路由
         */
        private String releaseOrderRoutingKey;

        /**
         * 解锁队列
         */
        private String otherQueue;

        /**
         * 解锁库存路由
         */
        private String releaseOtherRoutingKey;

        /**
         * 秒杀队列
         */
        private String seckillQueue;

        /**
         * 秒杀订单路由
         */
        private String seckillOrderRoutingKey;

    }

    @Autowired
    private GulimallRabbitOrderProperties rabbitOrderProperties;

    @Bean
    public Exchange orderEventExchange() {
        return ExchangeBuilder.topicExchange(rabbitOrderProperties.getEventExchange()).durable(true).build();
    }

    @Bean
    public Queue orderDelayQueue() {
        // x-dead-letter-exchange    声明延迟队列绑定的死信交换机
        // x-dead-letter-routing-key 声明延迟队列的死信路由
        // x-message-ttl             声明延迟队列的消息过期时间
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", rabbitOrderProperties.getEventExchange());
        args.put("x-dead-letter-routing-key", rabbitOrderProperties.getReleaseOrderRoutingKey());
        args.put("x-message-ttl", rabbitOrderProperties.getTtl());
        return new Queue(rabbitOrderProperties.getDelayQueue(), true, false, false, args);
    }

    @Bean
    public Queue orderDeadQueue() {
        return new Queue(rabbitOrderProperties.getDeadQueue(), true, false, false);
    }

    @Bean
    public Queue orderSeckillQueue() {
        return new Queue(rabbitOrderProperties.getSeckillQueue(), true, false, false);
    }

    @Bean
    public Binding orderCreateOrderBinding() {
        return new Binding(rabbitOrderProperties.getDelayQueue(), Binding.DestinationType.QUEUE,
                rabbitOrderProperties.getEventExchange(), rabbitOrderProperties.getCreateOrderRoutingKey(),
                null);
    }

    @Bean
    public Binding orderReleaseOrderBinding() {
        return new Binding(rabbitOrderProperties.getDeadQueue(), Binding.DestinationType.QUEUE,
                rabbitOrderProperties.getEventExchange(), rabbitOrderProperties.getReleaseOrderRoutingKey(),
                null);
    }

    @Bean
    public Binding orderReleaseOtherBinding() {
        return new Binding(rabbitOrderProperties.getOtherQueue(), Binding.DestinationType.QUEUE,
                rabbitOrderProperties.getEventExchange(), rabbitOrderProperties.getReleaseOtherRoutingKey(),
                null);
    }

    @Bean
    public Binding orderSeckillQueueBinding() {
        return new Binding(rabbitOrderProperties.getSeckillQueue(), Binding.DestinationType.QUEUE,
                rabbitOrderProperties.getEventExchange(), rabbitOrderProperties.getSeckillOrderRoutingKey(),
                null);
    }

}
