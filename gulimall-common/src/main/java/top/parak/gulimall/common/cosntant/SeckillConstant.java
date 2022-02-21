package top.parak.gulimall.common.cosntant;

/**
 * @author KHighness
 * @since 2022-01-21
 * @email parakovo@gmail.com
 */
public class SeckillConstant {

    /**
     * 上架秒杀商品任务分布式锁
     */
    public static final String UPLOAD_SECKILL_SKU_LOCK = "guliamll:seckill:upload:lock";

    /**
     * 秒杀活动redis存储分组
     */
    public static final String CACHE_SESSION_PREFIX = "guliamll:seckill:sessions:";

    /**
     * 秒杀商品redis存储分组
     */
    public static final String CACHE_SKU_PREFIX = "guliamll:seckill:sku:";

    /**
     * 秒杀商品库存redis存储分组
     */
    public static final String CACHE_STOCK_PREFIX = "guliamll:seckill:stock:";

}
