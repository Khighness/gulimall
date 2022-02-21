package top.parak.gulimall.product.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import top.parak.gulimall.common.utils.R;
import top.parak.gulimall.product.feign.fallback.SeckillFeignServiceFallback;

/**
 * @author KHighness
 * @since 2022-01-29
 * @email parakovo@gmail.com
 */
@FeignClient(value = "gulimall-seckill", fallback = SeckillFeignServiceFallback.class)
public interface SeckillFeignService {

    @GetMapping("/sku/seckill/{skuId}")
    R getSkuSeckillInfo(@PathVariable("skuId") Long skuId);

}
