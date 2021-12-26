package top.parak.gulimall.product.vo;

import lombok.Data;
import top.parak.gulimall.product.entity.SkuImagesEntity;
import top.parak.gulimall.product.entity.SkuInfoEntity;
import top.parak.gulimall.product.entity.SpuInfoDescEntity;

import java.util.List;

/**
 * @author KHighness
 * @since 2021-12-24
 */
@Data
public class SkuItemVo {

    /**
     * SKU基本信息
     */
    private SkuInfoEntity info;

    /**
     * 是否有库存
     */
    private boolean hasStock = true;

    /**
     * SKU图片信息
     */
    private List<SkuImagesEntity> images;

    /**
     * SPU销售属性
     */
    private List<SkuItemSaleAttrVo> saleAttr;

    /**
     * SPU介绍信息
     */
    private SpuInfoDescEntity desc;

    /**
     * SPU规格参数
     */
    private List<SpuItemAttrGroupVo> groupAttrs;

    /**
     * 秒杀商品的优惠信息
     */
    private SeckillSkuVo seckillSkuVo;


    @Data
    public static class SkuItemSaleAttrVo {
        private Long attrId;
        private String attrName;
        private List<AttrValueWithSkuIdVo> attrValues;
    }

    @Data
    public static class SpuItemAttrGroupVo {
        private String groupName;
        private List<Attr> attrs;
    }

    @Data
    public static class SpuBaseAttrVo {
        private String attrName;
        private String attrValue;
    }

}
