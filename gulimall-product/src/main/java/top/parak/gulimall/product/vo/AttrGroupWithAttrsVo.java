package top.parak.gulimall.product.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import top.parak.gulimall.product.entity.AttrEntity;

import java.util.List;

/**
 * @author KHighness
 * @since 2021-10-15
 */
@Data
public class AttrGroupWithAttrsVo {

    /**
     * 分组id
     */
    @TableId
    private Long attrGroupId;
    /**
     * 组名
     */
    private String attrGroupName;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 描述
     */
    private String descript;
    /**
     * 组图标
     */
    private String icon;
    /**
     * 所属分类id
     */
    private Long catelogId;

    /**
     * 属性分组的关联属性
     */
    private List<AttrEntity> attrs;

}
