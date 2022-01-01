package top.parak.gulimall.search.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import top.parak.gulimall.common.utils.R;

import java.util.List;

/**
 * @author KHighness
 * @since 2021-12-17
 * @email parakovo@gmail.com
 */
@FeignClient("gulimall-product")
public interface ProductFeignService {

    @GetMapping("/product/attr/info/{attrId}")
    R getAttrInfo(@PathVariable("attrId") Long attrId);

    @GetMapping("/product/brand/infos")
    R getBrandInfo(@RequestParam("brandIds") List<Long> brandIds);

}
