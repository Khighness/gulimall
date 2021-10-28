package top.parak.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.parak.gulimall.common.utils.PageUtils;
import top.parak.gulimall.product.entity.CategoryEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author KHighness
 * @email parakovo@gmail.com
 * @date 2021-02-24 21:59:22
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 查询出所有分类，并且组装成父子结构
     * @return 分类列表
     */
    List<CategoryEntity> listWithTree();

    /**
     * 根据分类ID批量删除分类
     * @param idList 分类ID列表
     */
    void removeCategoryByIds(List<Long> idList);

    /**
     * 根据ID查找分类的完整路径
     * @param catelogId 分类ID
     * @return 路径形式 [父/子/孙]
     */
    Long[] findCatelogPath(Long catelogId);

    /**
     * 级联更新所有关联的数据
     * @param category 分类
     */
    void updateCascade(CategoryEntity category);

}

