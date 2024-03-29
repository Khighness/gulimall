package top.parak.gulimall.product.dao;

import org.apache.ibatis.annotations.Param;
import top.parak.gulimall.product.entity.CategoryBrandRelationEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 品牌分类关联
 *
 * @author KHighness
 * @since 2021-09-25
 * @email parakovo@gmail.com
 */
@Mapper
public interface CategoryBrandRelationDao extends BaseMapper<CategoryBrandRelationEntity> {

    void updateCategory(@Param("catId") Long catId, @Param("name") String name);

}
