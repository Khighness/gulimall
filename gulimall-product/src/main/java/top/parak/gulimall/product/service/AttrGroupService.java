package top.parak.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.parak.gulimall.common.utils.PageUtils;
import top.parak.gulimall.product.entity.AttrGroupEntity;
import top.parak.gulimall.product.vo.AttrGroupWithAttrsVo;

import java.util.List;
import java.util.Map;

/**
 * 属性分组
 *
 * @author KHighness
 * @email parakovo@gmail.com
 * @date 2021-09-24 21:59:22
 */
public interface AttrGroupService extends IService<AttrGroupEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 根据分类ID查询该分类下的属性分组
     * @param params    参数
     * @param catelogId 分类ID
     * @return 属性分组列表
     */
    PageUtils queryPage(Map<String, Object> params, Long catelogId);

    /**
     * 根据分类ID查询该分类下所有属性分组和关联属性
     * @param catelogId 分类ID
     * @return 所有属性分组以及属性列表
     */
    List<AttrGroupWithAttrsVo> getAttrGroupWithAttrsByCatelogId(Long catelogId);

}

