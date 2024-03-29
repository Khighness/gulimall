package top.parak.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.parak.gulimall.common.utils.PageUtils;
import top.parak.gulimall.product.entity.SpuInfoDescEntity;
import top.parak.gulimall.product.entity.SpuInfoEntity;
import top.parak.gulimall.product.vo.SpuSaveVo;

import java.util.Map;

/**
 * spu信息
 *
 * @author KHighness
 * @since 2021-09-25
 * @email parakovo@gmail.com
 */
public interface SpuInfoService extends IService<SpuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 新增商品，保存大量信息
     * @param vo {@link SpuSaveVo}
     */
    void saveSpuInfo(SpuSaveVo vo);

    /**
     * 保存SPU的基本信息
     * @param spuInfoEntity SPU实体
     */
    void saveBaseSpuInfo(SpuInfoEntity spuInfoEntity);

    /**
     * SPU检索
     * @param params 参数
     * @return SPU列表
     */
    PageUtils queryPageByCondition(Map<String, Object> params);

    /**
     * 商品上架
     * @param spuId SPU ID
     */
    void up(Long spuId);

    /**
     * 根据SKU ID查询SPU信息
     * @param skuId SKU ID
     * @return SPU信息
     */
    SpuInfoEntity getSpuInfoBySkuId(Long skuId);

}

