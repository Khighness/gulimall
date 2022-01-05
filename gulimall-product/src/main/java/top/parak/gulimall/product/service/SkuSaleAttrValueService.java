package top.parak.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.parak.gulimall.common.utils.PageUtils;
import top.parak.gulimall.product.entity.SkuSaleAttrValueEntity;
import top.parak.gulimall.product.vo.SkuItemVo;

import java.util.List;
import java.util.Map;

/**
 * sku销售属性&值
 *
 * @author KHighness
 * @since 2021-09-25
 * @email parakovo@gmail.com
 */
public interface SkuSaleAttrValueService extends IService<SkuSaleAttrValueEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 根据SPU ID查询SKU的销售属性
     * @param spuId SPU ID
     * @return SPU销售属性列表
     */
    List<SkuItemVo.SkuItemSaleAttrVo> getSaleAttrsBySpuId(Long spuId);

    /**
     * 根据SKU ID查询SKU的销售属性值
     * @param skuId SKU ID
     * @return 销售属性值列表[属性名:属性值...]
     */
    List<String> getSkuSaleAttrValuesAsStringList(Long skuId);

}

