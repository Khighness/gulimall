package top.parak.gulimall.ware.vo;

import lombok.Data;

import java.util.List;

/**
 * @author KHighness
 * @since 2021-10-18
 */
@Data
public class MergeVo {

    private Long purchaseId;

    private List<Long> items;

}
