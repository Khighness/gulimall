package top.parak.gulimall.seckill.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import top.parak.gulimall.common.utils.R;

/**
 * @author KHighness
 * @since 2022-01-21
 * @email parakovo@gmail.com
 */
@FeignClient("gulimall-coupon")
public interface CouponFeignService {

    @GetMapping("/coupon/seckillsession/latest3DaysSession")
    R getLatest3DaysSession();

}
