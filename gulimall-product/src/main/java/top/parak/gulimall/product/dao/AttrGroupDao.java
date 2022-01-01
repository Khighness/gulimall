package top.parak.gulimall.product.dao;

import org.apache.ibatis.annotations.Param;
import top.parak.gulimall.product.entity.AttrGroupEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.parak.gulimall.product.vo.SkuItemVo;

import java.util.List;

/**
 * 属性分组
 *
 * @author KHighness
 * @since 2021-09-25
 * @email parakovo@gmail.com
 */
@Mapper
public interface AttrGroupDao extends BaseMapper<AttrGroupEntity> {

    List<SkuItemVo.SpuItemAttrGroupVo> getAttrGroupWithAttrsBySpuId(@Param("spuId") Long spuId,
                                                                    @Param("catalogId") Long catalogId);

}
