package top.parak.gulimall.order.vo;

import lombok.Data;

import java.util.List;

/**
 * 锁定库存
 *
 * @author KHighness
 * @since 2022-01-14
 * @email parakovo@gmail.com
 */
@Data
public class WareSkuLockVo {

    /**
     * 订单号
     */
    private String orderSn;

    /**
     * 需要锁住的库存信息
     */
    private List<OrderItemVo> locks;

}
