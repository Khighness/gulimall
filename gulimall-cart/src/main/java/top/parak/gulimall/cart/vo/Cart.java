package top.parak.gulimall.cart.vo;

import java.math.BigDecimal;
import java.util.List;

/**
 * 购物车
 * 需要计算的属性需要重写{@code get() }方法
 *
 * @author KHighness
 * @since 2022-01-02
 * @email parakovo@gmail.com
 */
public class Cart {

    /**
     * 购买商品列表
     */
    private List<CartItem> items;

    /**
     * 商品数量
     */
    private Integer countNum;

    /**
     * 商品类型数量
     */
    private Integer countType;

    /**
     * 商品总价
     */
    private BigDecimal totalAmount;

    /**
     * 减免价格
     */
    private BigDecimal reduce = new BigDecimal("0.00");

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    public Integer getCountNum() {
        int num = 0;
        if (items != null && items.size() > 0) {
            for (CartItem item : items) {
                num += item.getCount();
            }
        }
        return num;
    }

    public Integer getCountType() {
        int count = 0;
        if(this.items != null && this.items.size() > 0){
            for (CartItem item : this.items) {
                count += 1;
            }
        }
        return count;
    }

    public BigDecimal getTotalAmount() {
        BigDecimal amount = new BigDecimal("0");
        // 1. 计算购物项总价
        if (items != null && items.size() > 0) {
            for (CartItem item : items) {
                if (item.getCheck()) {
                    amount = amount.add(item.getTotalPrice());
                }
            }
        }
        // 2. 减去优惠价
        return amount.subtract(getReduce());
    }

    public BigDecimal getReduce() {
        return reduce;
    }

    public void setReduce(BigDecimal reduce) {
        this.reduce = reduce;
    }
}
