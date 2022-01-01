package top.parak.gulimall.product.vo;

import lombok.Data;

/**
 * @author KHighness
 * @since 2021-10-14
 * @email parakovo@gmail.com
 */
@Data
public class AttrRespVo extends AttrVo {

    /**
     * 分类名称
     */
    private String catelogName;
    /**
     * 分组名称
     */
    private String groupName;
    /**
     * 分类路径
     */
    private Long[] catelogPath;


}
