package top.parak.gulimall.order.listener;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.parak.gulimall.common.to.mq.SeckillOrderTo;
import top.parak.gulimall.order.service.OrderService;

import java.io.IOException;

/**
 * 监听秒杀订单消息
 *
 * @author KHighness
 * @since 2022-01-30
 * @email parakovo@gmail.com
 */
@Component
@RabbitListener(queues = "${gulimall.rabbitmq.order.seckill-queue}")
@Slf4j
public class OrderSeckillListener {

    @Autowired
    private OrderService orderService;

    @RabbitHandler
    public void listenToOrder(SeckillOrderTo seckillOrderTo, Channel channel, Message message) throws IOException {
        log.info("【消息队列】 秒杀成功 => 创建订单：{}", seckillOrderTo);
        try {
            orderService.createSSeckillOrder(seckillOrderTo);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            // 出现异常，拒绝确认消息，重新放回队列
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
        }
    }

}
