package top.parak.gulimall.order.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import top.parak.gulimall.common.cosntant.GulimallPageConstant;
import top.parak.gulimall.common.exception.NoStockException;
import top.parak.gulimall.order.config.GulimallRabbitConfig;
import top.parak.gulimall.order.entity.OrderEntity;
import top.parak.gulimall.order.service.OrderService;
import top.parak.gulimall.order.vo.OrderConfirmVo;
import top.parak.gulimall.order.vo.OrderSubmitVo;
import top.parak.gulimall.order.vo.SubmitOrderResponseVo;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

/**
 * 订单web控制器
 *
 * @author KHighness
 * @since 2022-01-09
 * @email parakovo@gmail.com
 */
@Controller
@Slf4j
public class OrderWebController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private GulimallRabbitConfig.GulimallRabbitOrderProperties rabbitOrderProperties;

    /**
     * 测试下单
     * @see <a href="http://order.gulimall.com/createOrder">createOrder</a>
     */
    @ResponseBody
    @GetMapping("/createOrder")
    public String createOrder() {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setOrderSn(UUID.randomUUID().toString());
        orderEntity.setModifyTime(new Date());
        rabbitTemplate.convertAndSend(rabbitOrderProperties.getEventExchange(),
                rabbitOrderProperties.getCreateOrderRoutingKey()
                , orderEntity);

        return "下单成功!";
    }

    /**
     * 购物车结算
     */
    @GetMapping("/toTrade")
    public String toTrade(Model model) throws ExecutionException, InterruptedException {
        OrderConfirmVo orderConfirmVo = orderService.confirmOrder();
        model.addAttribute("orderConfirmData", orderConfirmVo);

        return "confirm";
    }

    /**
     * 提交订单
     */
    @PostMapping("/submitOrder")
    public String submitOrder(OrderSubmitVo orderSubmitVo, Model model, RedirectAttributes redirectAttributes) {
        try {
            // 1. 创建订单
            SubmitOrderResponseVo submitOrderResponseVo = orderService.submitOrder(orderSubmitVo);

            // 2. 下单成功来到支付选项
            if (submitOrderResponseVo.getCode() == 0) {
                model.addAttribute("submitOrderResp", submitOrderResponseVo);
                return "pay";
            }

            // 3. 下单失败回到订单确认页重新确认订单信息
            else {
                String msg = "下单失败：";
                switch (submitOrderResponseVo.getCode()) {
                    case 1: msg += "订单信息过期，请刷新再提交"; break;
                    case 2: msg += "订单的商品价格已发生变化，请确认后再次提交"; break;
                    case 3: msg += "商品库存不足"; break;
                }

                redirectAttributes.addFlashAttribute("msg", msg);
                return GulimallPageConstant.REDIRECT + GulimallPageConstant.ORDER_PAGE;
            }

        } catch (Exception e) {
            log.error("创建订单失败：{}", e.getMessage(), e);

            if (e instanceof NoStockException) {
                redirectAttributes.addFlashAttribute("msg", e.getMessage());
            }

            return GulimallPageConstant.REDIRECT + GulimallPageConstant.ORDER_PAGE;
        }
    }

}
