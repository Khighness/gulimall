package top.parak.gulimall.common.to.mq;

import lombok.Data;

/**
 * @author KHighness
 * @since 2022-01-17
 * @email parakovo@gmail.com
 */
@Data
public class StockLockedTo {

    /**
     * 库存工作单
     */
    private Long id;

    /**
     * 工作单详情
     */
    private StockDetailTo detailTo;

}
