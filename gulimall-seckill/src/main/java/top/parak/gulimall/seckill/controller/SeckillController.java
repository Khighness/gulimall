package top.parak.gulimall.seckill.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import top.parak.gulimall.common.utils.R;
import top.parak.gulimall.seckill.service.SeckillService;
import top.parak.gulimall.seckill.to.SeckillSkuRedisTo;

import java.util.List;

/**
 * 秒杀控制器
 *
 * @author KHighness
 * @since 2022-01-22
 * @email parakovo@gmail.com
 */
@Controller
public class SeckillController {

    @Autowired
    private SeckillService seckillService;

    @ResponseBody
    @GetMapping("/currentSeckillSkus")
    public R getCurrentSeckillSkus() {
        List<SeckillSkuRedisTo> tos = seckillService.getCurrentSeckillSkus();

        return R.ok().setData(tos);
    }

    @ResponseBody
    @GetMapping("/sku/seckill/{skuId}")
    public R getSkuSeckillInfo(@PathVariable("skuId") Long skuId) {
        SeckillSkuRedisTo seckillSkuRedisTo = seckillService.getSkuSeckillInfo(skuId);

        return R.ok().setData(seckillSkuRedisTo);
    }

    @GetMapping("kill")
    public String secKill(@RequestParam("killId") String killId,
                          @RequestParam("key") String key,
                          @RequestParam("num") Integer num,
                          Model model) throws InterruptedException {
        String orderSn = seckillService.kill(killId, key, num);
        model.addAttribute("orderSn", orderSn);

        return "success";
    }

}
