package top.parak.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.parak.gulimall.common.utils.PageUtils;
import top.parak.gulimall.product.entity.BrandEntity;
import top.parak.gulimall.product.entity.CategoryBrandRelationEntity;

import java.util.List;
import java.util.Map;

/**
 * 品牌分类关联
 *
 * @author KHighness
 * @since 2021-09-25
 * @email parakovo@gmail.com
 */
public interface CategoryBrandRelationService extends IService<CategoryBrandRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 关联品牌和分类
     * @param categoryBrandRelation 品牌和分类关联信息
     */
    void saveDetail(CategoryBrandRelationEntity categoryBrandRelation);

    /**
     * 品牌更新名称时更新品牌和分类关联信息
     * @param brandId 品牌ID
     * @param name    更新后的品牌名称
     */
    void updateBrand(Long brandId, String name);

    /**
     * 分类名称更新时更新品牌和分类关联信息
     * @param catId 分类ID
     * @param name  分类名称
     */
    void updateCategory(Long catId, String name);

    /**
     * 根据分类ID查询关联的所有品牌
     * @param catId 分类ID
     * @return 品牌列表
     */
    List<BrandEntity> getBrandsByCatId(Long catId);

}

