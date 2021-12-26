package top.parak.gulimall.product.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import top.parak.gulimall.product.service.SkuInfoService;
import top.parak.gulimall.product.vo.SkuItemVo;

import java.util.concurrent.ExecutionException;

/**
 * @author KHighness
 * @since 2021-12-22
 */
@Controller
public class ItemController {

    @Autowired
    private SkuInfoService skuInfoService;

    @GetMapping("/{skuId}.html")
    public String skuItem(@PathVariable("skuId") Long skuId, Model model)
            throws ExecutionException, InterruptedException {

        SkuItemVo skuItemVo = skuInfoService.item(skuId);
        model.addAttribute("item", skuItemVo);

        return "item";
    }

}
