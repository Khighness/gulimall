package top.parak.gulimall.seckill.service;

import top.parak.gulimall.seckill.to.SeckillSkuRedisTo;

import java.util.List;

/**
 * 秒杀接口
 *
 * @author KHighness
 * @since 2022-01-21
 * @email parakovo@gmail.com
 */
public interface SeckillService {

    /**
     * 上架近三天的秒杀活动商品
     */
    void uploadSeckillSkuLatest3Days();

    /**
     * 获取当前可以参与秒杀的商品信息
     */
    List<SeckillSkuRedisTo> getCurrentSeckillSkus();

    /**
     * 根据SKU ID查询秒杀信息
     * @param skuId SKU ID
     * @return 秒杀细腻
     */
    SeckillSkuRedisTo getSkuSeckillInfo(Long skuId);

    /**
     * 秒杀
     * @param killId 秒杀ID
     * @param key 随机码
     * @param num 数量
     * @return 秒杀结果
     */
    String kill(String killId, String key, Integer num) throws InterruptedException;

}
