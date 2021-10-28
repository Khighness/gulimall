package top.parak.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.parak.gulimall.common.utils.PageUtils;
import top.parak.gulimall.product.entity.AttrEntity;
import top.parak.gulimall.product.entity.ProductAttrValueEntity;
import top.parak.gulimall.product.vo.AttrGroupRelationVo;
import top.parak.gulimall.product.vo.AttrRespVo;
import top.parak.gulimall.product.vo.AttrVo;

import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author KHighness
 * @email parakovo@gmail.com
 * @date 2021-02-24 21:59:22
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 保存属性
     * @param attr 属性
     */
    void saveAttr(AttrVo attr);

    /**
     * 根据分类ID查询属性列表
     * @param params    分页参数
     * @param attrType  属性类型
     * @param catelogId 分类ID
     * @return 属性列表
     */
    PageUtils queryBaseAttrPage(Map<String, Object> params, String attrType, Long catelogId);

    /**
     * 根据属性ID查询属性
     * @param attrId 属性ID
     * @return 属性详细信息
     */
    AttrRespVo getAttrInfo(Long attrId);

    /**
     * 更新属性
     * @param attr 属性
     */
    void updateAttr(AttrVo attr);

    /**
     * 根据分组ID查找关联的所有属性
     * @param attrGroupId 分组ID
     * @return 关联的属性列表
     */
    List<AttrEntity> getRelationAttr(Long attrGroupId);

    /**
     * 解除分类ID和属性的关联
     * @param vos 分类ID和属性ID的vo数组
     */
    void deleteRelation(AttrGroupRelationVo[] vos);

    /**
     * 根据分组ID查找未关联的所有属性
     * @param params 分页参数
     * @param attrGroupId 分组ID
     * @return 未关联的属性列表
     */
    PageUtils getNoRelationAttr(Map<String, Object> params, Long attrGroupId);

}

