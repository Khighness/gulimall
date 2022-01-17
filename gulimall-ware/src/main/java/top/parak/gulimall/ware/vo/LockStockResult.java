package top.parak.gulimall.ware.vo;

import lombok.Data;

/**
 * 库存锁定结果
 *
 * @author KHighness
 * @since 2022-01-14
 * @email parakovo@gmail.com
 */
@Data
public class LockStockResult {

    /**
     * SKU ID
     */
    private Long skuId;

    /**
     * 锁定数量
     */
    private Integer num;

    /**
     * 是否锁定成功
     */
    private Boolean locked;

}
