package top.parak.gulimall.order.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 订单确认页数据
 *
 * @author KHighness
 * @since 2022-01-10
 * @email parakovo@gmail.com
 */
@ToString
public class OrderConfirmVo {

    /**
     * 收货地址
     */
    @Getter
    @Setter
    private List<MemberReceiveAddressVo> address;

    /**
     * 选中购物项
     */
    @Getter
    @Setter
    private List<OrderItemVo> items;

    /**
     * 用户积分
     */
    @Getter
    @Setter
    private Integer integration;

    /**
     * 防重令牌
     */
    @Getter
    @Setter
    private String orderToken;

    /**
     * 商品库存
     */
    @Getter
    @Setter
    private Map<Long, Boolean> stocks;

    /**
     * 商品数量
     */
    public Integer getCount() {
        Integer count = 0;
        if (!CollectionUtils.isEmpty(items)) {
            for (OrderItemVo item : items) {
                count += item.getCount();
            }
        }
        return count;
    }

    /**
     * 订单总额
     */
    public BigDecimal getTotal() {
        BigDecimal total = new BigDecimal("0");
        if (!CollectionUtils.isEmpty(items)) {
            for (OrderItemVo item : items) {
                BigDecimal itemAmount = item.getPrice()
                        .multiply(new BigDecimal(item.getCount().toString()));
                total = total.add(itemAmount);
            }
        }
        return total;
    }

    /**
     * 应付金额
     */
    public BigDecimal getPayPrice() {
        return getTotal();
    }
}
