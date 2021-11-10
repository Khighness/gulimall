package top.parak.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.parak.gulimall.common.utils.PageUtils;
import top.parak.gulimall.product.entity.ProductAttrValueEntity;

import java.util.List;
import java.util.Map;

/**
 * spu属性值
 *
 * @author KHighness
 * @email parakovo@gmail.com
 * @date 2021-09-24 21:59:22
 */
public interface ProductAttrValueService extends IService<ProductAttrValueEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 保存SPU的属性（规则参数）
     * @param productAttrValueEntities 属性列表
     */
    void saveProductAttr(List<ProductAttrValueEntity> productAttrValueEntities);

    /**
     * 获取SPU的规格
     * @param spuId SPU ID
     * @return SPU的规格列表
     */
    List<ProductAttrValueEntity> baseAttrListForSpu(Long spuId);

    /**
     * 根据SPU ID修改商品规格
     * @param spuId    SPU ID
     * @param entities 规格数组
     */
    void updateSpuAttr(Long spuId, List<ProductAttrValueEntity> entities);

}

