package top.parak.gulimall.order.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 订单提交页数据
 *
 * @author KHighness
 * @since 2022-01-13
 * @email parakovo@gmail.com
 */
@Data
public class OrderSubmitVo extends OrderConfirmVo {

    /**
     * 收货地址ID
     */
    private Long addrId;

    /**
     * 支付方式
     */
    private Integer payType;


    /* 去购物车再获取一遍购买的商品 */

    /**
     * 防重令牌
     */
    private String orderToken;

    /**
     * 验价
     */
    private BigDecimal payPrice;

    /**
     * 备注
     */
    private String note;
}
