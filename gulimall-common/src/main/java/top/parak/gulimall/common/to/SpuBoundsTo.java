package top.parak.gulimall.common.to;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author KHighness
 * @since 2021-10-16
 * @email parakovo@gmail.com
 */
@Data
public class SpuBoundsTo {

    private Long spuId;
    private BigDecimal buyBounds;
    private BigDecimal growBounds;

}
