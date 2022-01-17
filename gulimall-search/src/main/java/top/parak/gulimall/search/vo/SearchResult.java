package top.parak.gulimall.search.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.parak.gulimall.common.to.es.SkuEsModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 检索结果
 *
 * @author KHighness
 * @since 2021-12-13
 * @email parakovo@gmail.com
 */
@Data
public class SearchResult {

    /*=====================================================
    ========================商品信息========================
    =====================================================*/

    /**
     * 查询到的所有商品信息
     */
    private List<SkuEsModel> products;


    /*=====================================================
    ========================分页信息========================
    ====================================================*/

    /**
     * 当前页码
     */
    private Integer pageNum;

    /**
     * 总记录数
     */
    private Long total;

    /**
     * 总页码
     */
    private Integer totalPages;

    /**
     * 导航栏
     */
    private List<Integer> pageNavs;


    /*=====================================================
    ========================其他信息========================
    ====================================================*/

    /**
     * 品牌信息
     */
    private List<BrandVo> brands;

    /**
     * 属性信息
     */
    private List<AttrVo> attrs;

    /**
     * 分类信息
     */
    private List<CatalogVo> catalogs;

    /**
     * 面包屑导航栏
     */
    private List<NavVo> navs;

    /**
     * 筛选属性ID
     */
    private List<Long> attrIds = new ArrayList<>();


    /*=====================================================
    =========================内部类=========================
    ====================================================*/

    /**
     * 品牌VO
     */
    @Data
    @AllArgsConstructor
    public static class BrandVo {
        private Long brandId;
        private String brandName;
        private String brandImg;
    }

    /**
     * 属性VO
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AttrVo {
        private Long attrId;
        private String attrName;
        private List<String> attrValue;
    }

    /**
     * 分类VO
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CatalogVo {
        private Long catalogId;
        private String catalogName;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NavVo {
        private String navName;
        private String navValue;
        private String link;
    }

}
