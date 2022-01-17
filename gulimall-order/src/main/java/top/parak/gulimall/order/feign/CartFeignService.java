package top.parak.gulimall.order.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import top.parak.gulimall.order.vo.OrderItemVo;

import java.util.List;

/**
 * @author KHighness
 * @since 2022-01-10
 * @email parakovo@gmail.com
 */
@FeignClient("gulimall-cart")
public interface CartFeignService {

    @GetMapping("/currentUserCartItems")
    List<OrderItemVo> getCurrentUserCartItems();

}
