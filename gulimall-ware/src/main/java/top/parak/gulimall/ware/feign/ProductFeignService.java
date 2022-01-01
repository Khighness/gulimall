package top.parak.gulimall.ware.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import top.parak.gulimall.common.utils.R;

/**
 * @author KHighness
 * @since 2021-10-28
 * @email parakovo@gmial.com
 */
@FeignClient("gulimall-product")
public interface ProductFeignService {

    @RequestMapping("product/skuinfo/info/{skuId}")
    R info(@PathVariable("skuId") Long skuId);

}
