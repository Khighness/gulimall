package top.parak.gulimall.ware.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 订单项
 *
 * @author KHighness
 * @since 2022-01-10
 * @email parakovo@gmail.com
 */
@Data
public class OrderItemVo {

    /**
     * 商品ID
     */
    private Long skuId;

    /**
     * 商品标题
     */
    private String title;

    /**
     * 商品图片
     */
    private String image;

    /**
     * 属性列表
     */
    private List<String> skuAttr;

    /**
     * 单价
     */
    private BigDecimal price;

    /**
     * 数量
     */
    private Integer count;

    /**
     * 小计
     */
    private BigDecimal totalPrice;

    /**
     * 是否被选择
     */
    private Boolean check = true;

    /**
     * 重量
     */
    private BigDecimal weight;

}
