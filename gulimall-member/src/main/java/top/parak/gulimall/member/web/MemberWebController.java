package top.parak.gulimall.member.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.parak.gulimall.common.utils.R;
import top.parak.gulimall.member.feign.OrderFeignService;

import java.util.HashMap;
import java.util.Map;

/**
 * 会员web控制器
 *
 * @author KHighness
 * @since 2022-01-19
 * @email parakovo@gmail.com
 */
@Controller
@Slf4j
public class MemberWebController {

    @Autowired
    private OrderFeignService orderFeignService;

    /**
     * 我的订单页面
     * <p>支付同步通知回调</p>
     */
    @GetMapping("/memberOrder.html")
    public String memgerOrderPage(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                  Model model) {
        Map<String, Object> params = new HashMap<>();
        params.put("page", pageNum.toString());

        try {
            R r = orderFeignService.listWithItem(params);
            model.addAttribute("orders", r);
        } catch (Exception e) {
            log.error("【远程调用】 远程查询用户订单信息失败：[订单服务可能未启动或者已宕机]");
        }

        return "orderList";
    }

}
