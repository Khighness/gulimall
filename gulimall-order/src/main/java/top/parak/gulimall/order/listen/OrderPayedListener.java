package top.parak.gulimall.order.listen;

import com.alipay.api.internal.util.AlipaySignature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import top.parak.gulimall.order.pay.AlipayTemplate;
import top.parak.gulimall.order.pay.PayAsyncVo;
import top.parak.gulimall.order.service.OrderService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 监听支付宝回调消息
 *
 * @author KHighness
 * @since 2022-01-20
 * @email parakovo@gmail.com
 */
@Slf4j
@RestController
public class OrderPayedListener {

    @Autowired
    private OrderService orderService;

    @Autowired
    private AlipayTemplate alipayTemplate;

    @PostMapping("/payed/notify")
    public String handleAliPayed(PayAsyncVo payAsyncVo, HttpServletRequest request) throws Exception {
        log.info("支付宝回调通知：{}", payAsyncVo);

        // 验证签名
        Map<String, String> params = new HashMap<>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (String name : requestParams.keySet()) {
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }

            // ISO编码，防止中文乱码
            // valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }

        if (AlipaySignature.rsaCheckV1(params, alipayTemplate.getAlipayPublicKey(),
                alipayTemplate.getCharset(), alipayTemplate.getSignType())) {
            return orderService.handlePayResult(payAsyncVo);
        }

        return "fail";
    }

}
