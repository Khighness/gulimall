package top.parak.gulimall.ware.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 收货地址和运费信息
 *
 * @author KHighness
 * @since 2022-01-13
 * @email parakovo@gmail.com
 */
@Data
public class FareVo {

    private MemberAddressVo address;

    private BigDecimal fare;

}
