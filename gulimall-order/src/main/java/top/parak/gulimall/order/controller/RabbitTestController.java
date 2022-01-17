package top.parak.gulimall.order.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * @author KHighness
 * @since 2022-01-07
 * @email parakovo@gmail.com
 */
@RequestMapping("/rabbit")
@RestController
@Slf4j
public class RabbitTestController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 测试消息发送，成功与失败
     * <p>交换机: hello-exchange
     * <p>队列: hello-queue
     * <p>路由: hello
     * @see <a href="http://localhost:9000/rabbit/hello-exchange/hello?msg=Khighness">succeed</a>
     * @see <a href="http://localhost:9000/rabbit/byebye-exchange/hello?msg=Khighness">fail to reach the broker</a>
     * @see <a href="http://localhost:9000/rabbit/hello-exchange/byebye?msg=Khighness">fail to reach the queue</a>
     */
    @GetMapping("/{exchange}/{routeKey}")
    public String sendMessage(@PathVariable("exchange") String exchange,
                              @PathVariable("routeKey") String routeKey,
                              @RequestParam("msg") String msg) {
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend(exchange, routeKey, msg, correlationData);
        return msg;
    }

}
