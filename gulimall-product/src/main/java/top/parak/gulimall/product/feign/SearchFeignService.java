package top.parak.gulimall.product.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import top.parak.gulimall.common.to.es.SkuEsModel;
import top.parak.gulimall.common.utils.R;

import java.util.List;

/**
 * @author KHighness
 * @since 2021-11-10
 */
@FeignClient("gulimall-search")
public interface SearchFeignService {

    @PostMapping("/search/save/product/")
    public R productStatusUp(@RequestBody List<SkuEsModel> skuEsModels);

}
