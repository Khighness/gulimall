package top.parak.gulimall.product.feign.fallback;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.parak.gulimall.common.exception.BizCodeEnum;
import top.parak.gulimall.common.utils.R;
import top.parak.gulimall.product.feign.SeckillFeignService;

/**
 * @author KHighness
 * @since 2022-01-31
 * @email parakovo@gmail.com
 */
@Component
@Slf4j
public class SeckillFeignServiceFallback implements SeckillFeignService {

    @Override
    public R getSkuSeckillInfo(Long skuId) {
        log.warn("【服务熔断】 远程调用：秒杀服务熔断");
        return R.error(BizCodeEnum.TOO_MANY_EXCEPTION.getCode(), BizCodeEnum.TOO_MANY_EXCEPTION.getMessage());
    }

}
