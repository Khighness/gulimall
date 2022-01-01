package top.parak.gulimall.product.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author KHighness
 * @since 2021-10-16
 * @email parakovo@gmail.com
 */
@Data
public class MemberPrice {

    private Long id;
    private String name;
    private BigDecimal price;

}
