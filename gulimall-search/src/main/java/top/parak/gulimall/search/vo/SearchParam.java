package top.parak.gulimall.search.vo;

import lombok.Data;

import java.util.List;

/**
 * @author KHighness
 * @since 2021-12-13
 * @email parakovo@gmail.com
 * @apiNote 查询条件
 */
@Data
public class SearchParam {

    /**
     * 全文匹配关键字
     */
    private String keyword;

    /**
     * 三级分类Id
     */
    private Long catalog3Id;

    /**
     * 排序方式
     * <ul>
     * <li>销量 saleCount_[asc/desc]</li>
     * <li>价格 skuPrice_[asc/desc]</li>
     * <li>热度 hotScore_[asc/desc]</li>
     * </ul>
     */
    private String sort;

    /**
     * 是否有货 hasStock=[0/1]
     */
    private Integer hasStock;

    /**
     * 价格区间 skuPrice=[min_max/_max/min_]
     */
    private String skuPrice;

    /**
     * 品牌ID brandId=1&brandId=2
     */
    private List<Long> brandId;

    /**
     * 属性筛选 attrs=1_X:2_Y
     */
    private List<String> attrs;

    /**
     * 页码
     */
    private Integer pageNum = 1;

    /**
     * 原生的所有查询条件
     */
    private String _queryString;

}
