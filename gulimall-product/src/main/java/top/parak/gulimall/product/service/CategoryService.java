package top.parak.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.parak.gulimall.common.utils.PageUtils;
import top.parak.gulimall.product.entity.CategoryEntity;
import top.parak.gulimall.product.vo.Catelog2Vo;

import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author KHighness
 * @email parakovo@gmail.com
 * @date 2021-09-24 21:59:22
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

    /**
     * 查询所有的一级分类
     * @return 一级分类列表
     */
    List<CategoryEntity> getLevel1Categories();

    /**
     * 查询所有分类封装成指定格式
     * @return 首页分类数据 MAP(key: 一级菜单ID, value: 二级菜单列表)
     */
    Map<String, List<Catelog2Vo>> getCatalogJson();

    /**
     * 通过Redis-SETNX分布式锁实现
     * @deprecated
     * @return 首页分类数据 MAP(key: 一级菜单ID, value: 二级菜单列表)
     */
    Map<String, List<Catelog2Vo>> getCatalogJsonDbWithRedisLock();

    /**
     * 通过redisson分布式锁实现
     * @return 首页分类数据 MAP(key: 一级菜单ID, value: 二级菜单列表)
     */
    Map<String, List<Catelog2Vo>> getCatalogJsonDbWithRedisson();

    /**
     * 通过Redis-SpringCache实现
     * @return 首页分类数据 MAP(key: 一级菜单ID, value: 二级菜单列表)
     */
    Map<String, List<Catelog2Vo>> getCatalogJsonDbWithSpringCache();

}

