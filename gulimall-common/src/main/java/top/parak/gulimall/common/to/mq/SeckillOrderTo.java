package top.parak.gulimall.common.to.mq;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author KHighness
 * @since 2022-01-30
 * @email parakovo@gmail.com
 */
@Data
public class SeckillOrderTo {

    /**
     * 秒杀订单id
     */
    private String orderSn;

    /**
     * 活动场次id
     */
    private Long promotionSessionId;
    /**
     * 商品id
     */
    private Long skuId;
    /**
     * 秒杀价格
     */
    private BigDecimal seckillPrice;
    /**
     * 秒杀总量
     */
    private Integer num;

    /**
     * 会员id
     */
    private Long memberId;

}
