package top.parak.gulimall.product.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author KHighness
 * @since 2021-12-25
 * @email parakovo@gmail.com
 */
@Data
public class SeckillSkuVo {

    /**
     * 活动ID
     */
    private Long promotionId;

    /**
     * 活动场次ID
     */
    private Long promotionSessionId;

    /**
     * 商品ID
     */
    private Long skuId;

    /**
     * 秒杀价格
     */
    private BigDecimal seckillPrice;

    /**
     * 秒杀总量
     */
    private Integer seckillCount;

    /**
     * 每人限购数量
     */
    private Integer seckillLimit;

    /**
     * 排序
     */
    private Integer seckillSort;

    /**
     * 秒杀开始时间
     */
    private Long startTime;

    /**
     * 秒杀结束时间
     */
    private Long endTime;

    /**
     * 当前商品秒杀的随机码
     */
    private String randomCode;

}
