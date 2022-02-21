package top.parak.gulimall.seckill.scheduel;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import top.parak.gulimall.common.cosntant.SeckillConstant;
import top.parak.gulimall.seckill.service.SeckillService;

import java.util.concurrent.TimeUnit;

/**
 * 定时任务上架商品
 *
 * @author KHighness
 * @since 2022-01-21
 * @email parakovo@gmail.com
 */
@Slf4j
@Service
public class SeckillSkuScheduled {

    @Autowired
    private SeckillService seckillService;

    @Autowired
    private RedissonClient redissonClient;

    /**
     * 选择服务器压力比较小的时候执行定时任务
     * <p>这里选择凌辰三点.
     */
    @Scheduled(cron = "0 0 3 * * ?")
    public void uploadSeckillSkuLatest3Days() {
        // 重复上架无需处理
        log.info("【定时任务】 上架秒杀活动的商品");

        // 获取分布式锁
        RLock lock = redissonClient.getLock(SeckillConstant.UPLOAD_SECKILL_SKU_LOCK);
        lock.lock(10, TimeUnit.SECONDS);
        try {
            seckillService.uploadSeckillSkuLatest3Days();
        } finally {
            lock.unlock();
        }
    }

}
