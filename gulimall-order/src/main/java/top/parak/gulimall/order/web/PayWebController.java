package top.parak.gulimall.order.web;

import com.alipay.api.AlipayApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import top.parak.gulimall.order.pay.AlipayTemplate;
import top.parak.gulimall.order.pay.PayVo;
import top.parak.gulimall.order.service.OrderService;

/**
 * 支付web控制器
 *
 * @author KHighness
 * @since 2022-01-19
 * @email parakovo@gmail.com
 */
@Controller
@Slf4j
public class PayWebController {

    @Autowired
    private AlipayTemplate alipayTemplate;

    @Autowired
    private OrderService orderService;

    /**
     * 支付宝支付
     */
    @ResponseBody
    @GetMapping(value = "/payOrder", produces = "text/html")
    public String payOrder(@RequestParam("orderSn") String orderSn) throws AlipayApiException {
        PayVo payVo = orderService.getOrderPay(orderSn);

        // 返回的是一段js脚本，渲染支付宝支付页面
        return alipayTemplate.pay(payVo);
    }

}
