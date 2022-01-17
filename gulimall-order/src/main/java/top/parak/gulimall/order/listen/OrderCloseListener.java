package top.parak.gulimall.order.listen;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.parak.gulimall.order.entity.OrderEntity;
import top.parak.gulimall.order.service.OrderService;

import java.io.IOException;

/**
 * 监听关闭订单消息
 *
 * @author KHighness
 * @since 2022-01-17
 * @email parakovo@gmail.com
 */
@Component
@RabbitListener(queues = "${gulimall.rabbitmq.order.dead-queue}")
@Slf4j
public class OrderCloseListener {

    @Autowired
    private OrderService orderService;

    @RabbitHandler
    public void listenToOrder(OrderEntity orderEntity, Channel channel, Message message) throws IOException {
        log.info("订单过期 => 关闭订单：{}", orderEntity);
        try {
            orderService.closeOrder(orderEntity);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            // 出现异常，拒绝确认消息，重新放回队列
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
        }
    }

}
