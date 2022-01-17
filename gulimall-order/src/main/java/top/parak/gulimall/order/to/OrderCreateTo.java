package top.parak.gulimall.order.to;

import lombok.Data;
import top.parak.gulimall.order.entity.OrderEntity;
import top.parak.gulimall.order.entity.OrderItemEntity;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author KHighness
 * @since 2022-01-14
 * @email parakovo@gmail.com
 */
@Data
public class OrderCreateTo {

    /**
     * 订单
     */
    private OrderEntity order;

    /**
     * 订单项
     */
    private List<OrderItemEntity> orderItems;

    /**
     * 订单价格
     */
    private BigDecimal payPrice;

    /**
     * 运费
     */
    private BigDecimal fare;

}
