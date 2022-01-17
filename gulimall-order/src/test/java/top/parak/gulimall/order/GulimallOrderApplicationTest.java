package top.parak.gulimall.order;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.parak.gulimall.order.entity.OrderReturnReasonEntity;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class GulimallOrderApplicationTest {

    @Autowired
    private AmqpAdmin amqpAdmin;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    void testCreateExchange() {
        // 创建交换机
        // param1: 交换机名称
        // param2: 是否持久化
        // param3: 是否自动删除
        DirectExchange directExchange = new DirectExchange("hello-exchange", true, false);
        amqpAdmin.declareExchange(directExchange);
    }

    @Test
    void testCreateQueue() {
        // 创建队列
        // param1: 队列名称
        // param2: 是否持久化
        // param3: 是否排他
        // param3: 是否自动删除
        Queue queue = new Queue("hello-queue", true, false, false);
        amqpAdmin.declareQueue(queue);
    }

    @Test
    void testCreateBinding() {
        // 创建绑定
        // param1: 队列名称
        // param2: 绑定类型
        // param3: 交换机名称
        // param4: 路由键
        // param5: 自定义参数
        Binding binding = new Binding("hello-queue", Binding.DestinationType.QUEUE,
                "hello-exchange", "hello", null);
        amqpAdmin.declareBinding(binding);
    }

    @Test
    void testSendMessage() {
        OrderReturnReasonEntity entity = new OrderReturnReasonEntity();
        entity.setId(1L);
        entity.setName("不喜欢");
        entity.setCreateTime(new Date());
        // 发送消息
        // param1: 交换机
        // param2: 路由
        // param3: 消息
        rabbitTemplate.convertAndSend("hello-exchange", "hello", entity);
    }

}
