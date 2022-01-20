package top.parak.gulimall.member.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import top.parak.gulimall.common.utils.R;

import java.util.Map;

/**
 * @author KHighness
 * @since 2022-01-19
 * @email parakovo@gmail.com
 */
@FeignClient("gulimall-order")
public interface OrderFeignService {

    @PostMapping("/order/order/listWithItem")
    R listWithItem(@RequestBody Map<String, Object> params);

}
