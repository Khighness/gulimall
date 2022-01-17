package top.parak.gulimall.ware.vo;

import lombok.Data;

/**
 * 采购项数据
 *
 * @author KHighness
 * @since 2021-10-28
 * @email parakovo@gmail.com
 */
@Data
public class PurchaseItemVo {

    private Long itemId;

    private Integer status;

    private String reason;

}
