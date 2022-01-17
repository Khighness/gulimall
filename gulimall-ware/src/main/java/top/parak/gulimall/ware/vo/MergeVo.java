package top.parak.gulimall.ware.vo;

import lombok.Data;

import java.util.List;

/**
 * 合并购物车
 *
 * @author KHighness
 * @since 2021-10-18
 * @email parakovo@gmail.com
 */
@Data
public class MergeVo {

    private Long purchaseId;

    private List<Long> items;

}
