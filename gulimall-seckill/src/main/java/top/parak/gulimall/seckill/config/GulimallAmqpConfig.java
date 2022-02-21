package top.parak.gulimall.seckill.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * AMQP配置
 *
 * @author KHighness
 * @since 2022-01-20
 * @email parakovo@gmail.com
 */
@Configuration
public class GulimallAmqpConfig {

    /**
     * RabbitTemplate JSON序列化
     */
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * 确保消息成功到达Broker
     */
    @Slf4j
    @Component
    public static class RabbitConfirmCallback implements RabbitTemplate.ConfirmCallback {

        @Autowired
        private RabbitTemplate rabbitTemplate;

        /**
         * 消息确认回调：消息发出后。是否被代理服务器Broker接收到，消息生产者会得到反馈。
         */
        @PostConstruct
        public void init() {
            rabbitTemplate.setConfirmCallback(this);
        }

        /**
         * 不管消息有没有抵达Broker，这个方法都会执行
         * @param correlationData 关联数据，可以关联id和KV数据
         * @param ack             消息到达代理服务器broker为true，否则为false
         * @param cause           未到达原因
         */
        @Override
        public void confirm(CorrelationData correlationData, boolean ack, String cause) {
            String id = correlationData != null ? correlationData.getId() : "null";
            if (ack) {
                log.info("【消息队列】 message [id={}] arrived broker succeed", id);
            } else {
                log.warn("【消息队列】 message [id={}] arrived broker failed, cause: [{}]", id, cause);
            }
        }

    }

    /**
     * 确保消息成功到达Queue
     */
    @Slf4j
    @Component
    public static class RabbitReturnConfirmCallback implements RabbitTemplate.ReturnCallback {

        @Autowired
        private RabbitTemplate rabbitTemplate;

        /**
         * 消息打回回调：消息没有被队列Queue成功收到，可以被消息生产者回收到。
         */
        @PostConstruct
        public void init() {
            rabbitTemplate.setReturnCallback(this);
        }

        /**
         * 消息没有抵达Queue，这个方法才会执行
         * @param message    消息
         * @param replyCode  返回码
         * @param replyText  返回原因
         * @param exchange   交换机
         * @param routingKey 路由键
         */
        @Override
        public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
            log.warn("【消息队列】 message [id={}, exchange={}, routingKey={},] arrived queue failed, detail [reply code: {}, reply text: {}]",
                    message.getMessageProperties().getCorrelationId(), exchange, routingKey, replyCode, replyText);
        }

    }

}
