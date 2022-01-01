package top.parak.gulimall.product.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import top.parak.gulimall.common.to.SkuReductionTo;
import top.parak.gulimall.common.to.SpuBoundsTo;
import top.parak.gulimall.common.utils.R;

/**
 * @author KHighness
 * @since 2021-10-16
 * @email parakovo@gmail.com
 */
@FeignClient("gulimall-coupon")
public interface CouponFeignService {

    @PostMapping("/coupon/spubounds/save")
    R saveSpuBounds(@RequestBody SpuBoundsTo spuBoundsTo);

    @PostMapping("/coupon/skufullreduction/saveinfo")
    R saveSkuReduction(@RequestBody SkuReductionTo skuReductionTo);

}
