package top.parak.gulimall.product.service.impl;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import top.parak.gulimall.common.utils.PageUtils;
import top.parak.gulimall.common.utils.Query;

import top.parak.gulimall.product.dao.CategoryDao;
import top.parak.gulimall.product.entity.CategoryEntity;
import top.parak.gulimall.product.service.CategoryService;

/**
 * 商品三级分类
 *
 * @author KHighness
 * @email parakovo@gmail.com
 * @date 2021-02-24 21:59:22
 */
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listWithTree() {
        // 查出所有分类
        List<CategoryEntity> entities = baseMapper.selectList(null);
        // 组装父子结构
        return entities.stream()
                // 获取一级分类
                .filter(categoryEntity -> categoryEntity.getParentCid() == 0)
                // 获取子分类
                .peek(categoryEntity -> categoryEntity.setChildren(getChildren(categoryEntity, entities)))
                // 排序
                .sorted((c1, c2) -> {
                    int s1 = c1.getSort() == null ? 0 : c1.getSort();
                    int s2 = c2.getSort() == null ? 0 : c2.getSort();
                    return s1 - s2;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void removeCategoryByIds(List<Long> idList) {
        // todo 检查当前删除的分类，是否被别的地方引用
        baseMapper.deleteBatchIds(idList);
    }

    /**
     * 递归获取子分类
     *
     * @param root 当前分类
     * @param all  所有分类
     * @return 当前分类的子分类
     */
    private List<CategoryEntity> getChildren(CategoryEntity root, List<CategoryEntity> all) {
        return all.stream()
                // 获取子分类
                .filter(categoryEntity -> categoryEntity.getParentCid().equals(root.getCatId()))
                // 子分类获取子分类
                .peek(categoryEntity -> categoryEntity.setChildren(getChildren(categoryEntity, all)))
                // 排序
                .sorted((c1, c2) -> {
                    int s1 = c1.getSort() == null ? 0 : c1.getSort();
                    int s2 = c2.getSort() == null ? 0 : c2.getSort();
                    return s1 - s2;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Long[] findCatelogPath(Long catelogId) {
        List<Long> paths = findParentPath(catelogId, new ArrayList<>());
        Collections.reverse(paths);
        return paths.toArray(new Long[0]);
    }

    private List<Long> findParentPath(Long catelogId, List<Long> paths) {
        paths.add(catelogId);
        CategoryEntity categoryEntity = this.getById(catelogId);
        if (categoryEntity.getParentCid() != 0) {
            findParentPath(categoryEntity.getParentCid(), paths);
        }
        return paths;
    }

}
