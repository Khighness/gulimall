package top.parak.gulimall.product.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author KHighness
 * @since 2021-10-16
 */
@Data
public class Bounds {

    /**
     * 积分
     */
    private BigDecimal buyBounds;
    /**
     * 成长值
     */
    private BigDecimal growBounds;

}
