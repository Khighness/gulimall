package top.parak.gulimall.common.to;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author KHighness
 * @since 2021-10-16
 */
@Data
public class MemberPrice {

    private Long id;
    private String name;
    private BigDecimal price;

}
