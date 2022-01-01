package top.parak.gulimall.common.to.es;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author KHighness
 * @since 2021-11-10
 * @email parakovo@gmail.com
 * @apiNote ElasticSearch存储模型
 * <pre>
 * {@code
 * PUT gulimall_product
 * {
 *   "mappings": {
 *     "properties": {
 *       "skuId": {
 *         "type": "long"
 *       },
 *       "spuId": {
 *         "type": "keyword"
 *       },
 *       "skuTitle": {
 *         "type": "text",
 *         "analyzer": "ik_smart"
 *       },
 *       "skuPrice": {
 *         "type": "keyword"
 *       },
 *       "skuImg": {
 *         "type": "keyword"
 *       },
 *       "saleCount": {
 *         "type": "long"
 *       },
 *       "hasStock": {
 *         "type": "boolean"
 *       },
 *       "hotScore": {
 *         "type": "long"
 *       },
 *       "brandId": {
 *         "type": "long"
 *       },
 *       "brandName": {
 *         "type": "keyword"
 *       },
 *       "brandImg": {
 *         "type": "keyword"
 *       },
 *       "catalogId": {
 *         "type": "long"
 *       },
 *       "catalogName": {
 *         "type": "keyword"
 *       },
 *       "attrs": {
 *         "type": "nested",
 *         "properties": {
 *           "attrId": {
 *             "type": "long"
 *           },
 *           "attrName": {
 *             "type": "keyword"
 *           },
 *           "attrValue":  {
 *             "type": "keyword"
 *           }
 *         }
 *       }
 *     }
 *   }
 * }
 * }
 * </pre>
 */
@Data
public class SkuEsModel {

    private Long skuId;

    private Long spuId;

    private String skuTitle;

    private BigDecimal skuPrice;

    private String skuImg;

    private Long saleCount;

    private Boolean hasStock;

    private Long hotScore;

    private Long brandId;

    private String brandName;

    private String brandImg;

    private Long catalogId;

    private String catalogName;

    private List<Attrs> attrs;

    @Data
    public static class Attrs {
        private Long attrId;
        private String attrName;
        private String attrValue;
    }

}
