package top.parak.gulimall.member.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import top.parak.gulimall.common.utils.R;

/**
 * @author KHighness
 * @email parakovo@gmial.com
 * @date 2021-02-25 17:13:57
 */

@FeignClient("gulimall-coupon")
public interface CouponFeignService {

    @RequestMapping("/coupon/coupon/member/list")
    public R memberCoupons();

}
