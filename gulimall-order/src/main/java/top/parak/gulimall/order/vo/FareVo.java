package top.parak.gulimall.order.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author KHighness
 * @since 2022-01-14
 * @email parakovo@gmail.com
 */
@Data
public class FareVo {

    private MemberAddressVo address;

    private BigDecimal fare;

}
