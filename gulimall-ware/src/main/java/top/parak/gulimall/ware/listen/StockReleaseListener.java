package top.parak.gulimall.ware.listen;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.parak.gulimall.common.to.mq.OrderTo;
import top.parak.gulimall.common.to.mq.StockLockedTo;
import top.parak.gulimall.ware.service.WareSkuService;

import java.io.IOException;

/**
 * 监听解锁库存消息
 *
 * @author KHighness
 * @since 2022-01-17
 * @email parakovo@gmail.com
 */
@Component
@RabbitListener(queues = "${gulimall.rabbitmq.stock.dead-queue}")
@Slf4j
public class StockReleaseListener {

    @Autowired
    private WareSkuService wareSkuService;

    /**
     * 订单超时或者用户取消，订单关闭 => 解锁库存
     */
    @RabbitHandler
    public void handleOrderClosedRelease(OrderTo orderTo, Message message, Channel channel)
            throws IOException {
        log.info("订单关闭 => 解锁库存：{}", orderTo);
        try {
            wareSkuService.unlockStock(orderTo);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
        }
    }

    /**
     * 下单成功，库存解锁，但是接下来业务调用失败 => 解锁库存
     */
    @RabbitHandler
    public void handleStockLockedRelease(StockLockedTo stockLockedTo, Message message, Channel channel)
            throws IOException {
        log.info("业务回滚 => 解锁库存：{}", stockLockedTo);
        try {
            wareSkuService.unlockStock(stockLockedTo);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
        }
    }

}
