package top.parak.gulimall.member.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import top.parak.gulimall.common.utils.R;

/**
 * 远程调用活动服务
 *
 * @author KHighness
 * @since 2021-09-25
 * @email parakovo@gmail.com
 */
@FeignClient("gulimall-coupon")
public interface CouponFeignService {

    @RequestMapping("/coupon/coupon/member/list")
    public R memberCoupons();

}
