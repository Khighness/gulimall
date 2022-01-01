package top.parak.gulimall.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.nested.ParsedNested;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import top.parak.gulimall.common.to.es.SkuEsModel;
import top.parak.gulimall.common.utils.R;
import top.parak.gulimall.search.vo.BrandVo;
import top.parak.gulimall.search.vo.SearchResult;
import top.parak.gulimall.search.config.GulimallElasticSearchConfig;
import top.parak.gulimall.search.constant.EsConstant;
import top.parak.gulimall.search.feign.ProductFeignService;
import top.parak.gulimall.search.service.MallSearchService;
import top.parak.gulimall.search.vo.AttrResponseVo;
import top.parak.gulimall.search.vo.SearchParam;

import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author KHighness
 * @since 2021-12-13
 * @email parakovo@gmail.com
 */
@Slf4j
@Service
public class MallSearchServiceImpl implements MallSearchService {
    public static final String SEARCH_LINK = "http://search.gulimall.com/list.html";

    @Autowired
    private RestHighLevelClient client;

    @Autowired
    private ProductFeignService productFeignService;

    @Override
    public SearchResult search(SearchParam searchParam) {
        SearchResult searchResult = null;

        // 1. 准备检索请求
        SearchRequest searchRequest = buildSearchRequest(searchParam);

        try {
            // 2. 执行检索请求
            SearchResponse searchResponse = client.search(searchRequest, GulimallElasticSearchConfig.COMMON_OPTIONS);

            // 3. 分析响应数据，封装成指定格式
            searchResult = buildSearchResult(searchParam, searchResponse);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return searchResult;
    }


    /**
     * 构建检索请求
     * <ul>
     * <li>模拟匹配</li>
     * <li>过滤条件（属性、分类、品牌、库存、价格区间）</li>
     * <li>排序分页</li>
     * <li>结果高亮</li>
     * <li>聚合分析</li>
     * </ul>
     * @param searchParam 检索参数
     * @return 检索请求
     */
    private SearchRequest buildSearchRequest(SearchParam searchParam) {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        /* 1. 模糊匹配，过滤条件 */
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        /* 1.1 must 模糊匹配 */
        // keyword全文检索
        if (!StringUtils.isEmpty(searchParam.getKeyword())) {
            boolQuery.must(QueryBuilders.matchQuery("skuTitle", searchParam.getKeyword()));
        }
        /* 1.2 filter 过滤条件 */
        // 按照三级分类ID查询
        if (searchParam.getCatalog3Id() != null) {
            boolQuery.filter(QueryBuilders.termQuery("catalogId", searchParam.getCatalog3Id()));
        }
        // 按照品牌ID查询
        if (searchParam.getBrandId() != null && searchParam.getBrandId().size() > 0) {
            boolQuery.filter(QueryBuilders.termsQuery("brandId", searchParam.getBrandId()));
        }
        // 按照指定的属性进行查询
        List<String> attrs = searchParam.getAttrs();
        if (attrs != null && attrs.size() > 0) {
            for (String attrStr : attrs) {
                BoolQueryBuilder nestedBoolQuery = QueryBuilders.boolQuery();
                String[] s = attrStr.split("_");
                String attrId = s[0];
                String[] attrValues = s[1].split(":");
                nestedBoolQuery.must(QueryBuilders.termQuery("attrs.attrId", attrId));
                nestedBoolQuery.must(QueryBuilders.termsQuery("attrs.attrValue", attrValues));
                NestedQueryBuilder nestedQuery = QueryBuilders.nestedQuery("attrs", nestedBoolQuery, ScoreMode.None);
                boolQuery.filter(nestedQuery);
            }
        }
        // 按照是否有库存查询
        if (searchParam.getHasStock() != null) {
            boolQuery.filter(QueryBuilders.termQuery("hasStock", searchParam.getHasStock() == 1));
        }
        // 按照价格区间查询
        if (!StringUtils.isEmpty(searchParam.getSkuPrice())) {
            String skuPrice = searchParam.getSkuPrice();
            String[] prices = skuPrice.split("_");

            RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("skuPrice");
            if (skuPrice.startsWith("_") && prices.length == 2) {
                rangeQuery.lte(Integer.parseInt(prices[1]));
            } else if (skuPrice.endsWith("_") && prices.length == 1) {
                rangeQuery.gte(Integer.parseInt(prices[0]));
            } else if (skuPrice.contains("_") && prices.length == 2) {
                rangeQuery.gte(Integer.parseInt(prices[0])).lte(Integer.parseInt(prices[1]));
            }
            boolQuery.filter(rangeQuery);
        }
        // 拼接
        sourceBuilder.query(boolQuery);

        /* 2. 排序、分页、高亮 */
        /* 2.1 排序 */
        if (!StringUtils.isEmpty(searchParam.getSort())) {
            String sort = searchParam.getSort();
            String[] s = sort.split("_");
            sourceBuilder.sort(s[0], s[1].equalsIgnoreCase("asc") ? SortOrder.ASC
                    : SortOrder.DESC);
        }
        /* 2.2 分页 */
        // from = (pageNum - 1) * size
        sourceBuilder.from((searchParam.getPageNum() - 1) * EsConstant.PRODUCT_PAGE_SIZE);
        sourceBuilder.size(EsConstant.PRODUCT_PAGE_SIZE);
        /* 2.3 高亮 */
        if (!StringUtils.isEmpty(searchParam.getKeyword())) {
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.field("skuTitle");
            highlightBuilder.preTags("<b style='color: red'>");
            highlightBuilder.postTags("</b>");
            sourceBuilder.highlighter(highlightBuilder);
        }

        /* 3. 聚合分析 */
        /* 3.1 按照品牌聚合 */
        // 先按照品牌ID聚合
        TermsAggregationBuilder brandAgg = AggregationBuilders.terms("brandAgg").field("brandId");
        // 再按照品牌名称和品牌图片聚合
        TermsAggregationBuilder brandNameAgg = AggregationBuilders.terms("brandNameAgg").field("brandName").size(1);
        TermsAggregationBuilder brandImgAgg = AggregationBuilders.terms("brandImgAgg").field("brandImg").size(1);
        brandAgg.subAggregation(brandNameAgg);
        brandAgg.subAggregation(brandImgAgg);
        sourceBuilder.aggregation(brandAgg);
        /* 3.2 按照分类聚合 */
        // 先按照分类ID聚合
        TermsAggregationBuilder catalogAgg = AggregationBuilders.terms("catalogAgg").field("catalogId").size(20);
        // 再按分类名称聚合
        TermsAggregationBuilder catalogNameAgg = AggregationBuilders.terms("catalogNameAgg").field("catalogName").size(1);
        catalogAgg.subAggregation(catalogNameAgg);
        sourceBuilder.aggregation(catalogAgg);
        /* 3.3 按照属性聚合 */
        NestedAggregationBuilder nestedAggregationBuilder = new NestedAggregationBuilder("attrs", "attrs");
        // 先按照属性Id聚合
        TermsAggregationBuilder attrIdAgg = AggregationBuilders.terms("attrIdAgg").field("attrs.attrId").size(50);
        // 再按照属性名和属性值聚合
        TermsAggregationBuilder attrNameAgg = AggregationBuilders.terms("attrNameAgg").field("attrs.attrName").size(1);
        TermsAggregationBuilder attrValueAgg = AggregationBuilders.terms("attrValueAgg").field("attrs.attrValue").size(50);
        attrIdAgg.subAggregation(attrNameAgg);
        attrIdAgg.subAggregation(attrValueAgg);
        nestedAggregationBuilder.subAggregation(attrIdAgg);
        sourceBuilder.aggregation(nestedAggregationBuilder);

        log.info("构建DSL: {}", sourceBuilder.toString());
//        // 记录查询DSL到文件
//        File file = new File(System.getProperty("user.dir") + "/gulimall-search/src/main/resources/json/" +
//                System.currentTimeMillis() + "-dsl.json");
//        try {
//            if (!file.exists()) {
//                file.createNewFile();
//            }
//            FileChannel channel = new FileOutputStream(file).getChannel();
//            ByteBuffer buffer = ByteBuffer.allocate(1024 * 8);
//            buffer.put(sourceBuilder.toString().getBytes(StandardCharsets.UTF_8));
//            buffer.flip();
//            channel.write(buffer);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        SearchRequest searchRequest = new SearchRequest(new String[]{ EsConstant.PRODUCT_INDEX }, sourceBuilder);

        return searchRequest;
    }

    /**
     * 将检索结果封装成指定格式
     * @param searchParam    检索参数
     * @param searchResponse 检索结果
     * @return 指定格式
     */
    private SearchResult buildSearchResult(SearchParam searchParam, SearchResponse searchResponse) {
        SearchResult searchResult = new SearchResult();
        SearchHits hits = searchResponse.getHits();

        /* 1. 封装查询到的商品信息 */
        if (hits.getHits() != null && hits.getHits().length > 0) {
            List<SkuEsModel> skuEsModels = new ArrayList<>();
            for (SearchHit hit : hits) {
                String sourceAsString = hit.getSourceAsString();
                SkuEsModel skuEsModel = JSON.parseObject(sourceAsString, SkuEsModel.class);
                // 设置高亮属性
                if (!StringUtils.isEmpty(searchParam.getKeyword())) {
                    HighlightField skuTitle = hit.getHighlightFields().get("skuTitle");
                    String highLight = skuTitle.getFragments()[0].string();
                    skuEsModel.setSkuTitle(highLight);
                }
                skuEsModels.add(skuEsModel);
            }
            searchResult.setProducts(skuEsModels);
        }

        /* 2. 封装分页信息 */
        // 当前页码
        searchResult.setPageNum(searchParam.getPageNum());
        // 总记录数
        long total = hits.getTotalHits().value;
        searchResult.setTotal(total);
        // 总页码
        int totalPages = (int) total % EsConstant.PRODUCT_PAGE_SIZE == 0 ?
                (int) total / EsConstant.PRODUCT_PAGE_SIZE : (int) total / EsConstant.PRODUCT_PAGE_SIZE + 1;
        searchResult.setTotalPages(totalPages);
        List<Integer> pageNavs = new ArrayList<>();
        for (int i = 1; i <= totalPages; i++) {
            pageNavs.add(i);
        }
        searchResult.setPageNavs(pageNavs);

        /* 3. 封装聚合的品牌信息 */
        List<SearchResult.BrandVo> brandVos = new ArrayList<>();
        Aggregations aggregations = searchResponse.getAggregations();
        // ParsedLongTerms用于接收terms聚合的结果，并且可以把key转化为Long类型的数据
        ParsedLongTerms brandAgg = aggregations.get("brandAgg");
        for (Terms.Bucket bucket : brandAgg.getBuckets()) {
            // 3.1 获取品牌ID
            long brandId = bucket.getKeyAsNumber().longValue();

            Aggregations subBrandAggs = bucket.getAggregations();
            // 3.2 获取品牌图片
            ParsedStringTerms brandImgAgg = subBrandAggs.get("brandImgAgg");
            String brandImg = brandImgAgg.getBuckets().get(0).getKeyAsString();
            // 3.3 获取品牌名字
            Terms brandNameAgg = subBrandAggs.get("brandNameAgg");
            String brandName = brandNameAgg.getBuckets().get(0).getKeyAsString();

            // 3.4 封装品牌VO
            SearchResult.BrandVo brandVo = new SearchResult.BrandVo(brandId, brandName, brandImg);
            brandVos.add(brandVo);
        }
        searchResult.setBrands(brandVos);

        /* 4. 封装聚合的分类信息 */
        List<SearchResult.CatalogVo> catalogVos = new ArrayList<>();
        ParsedLongTerms catalogAgg = aggregations.get("catalogAgg");
        for (Terms.Bucket bucket : catalogAgg.getBuckets()) {
            // 4.1 获取分类ID
            long catalogId = bucket.getKeyAsNumber().longValue();
            Aggregations subCatalogAggs = bucket.getAggregations();

            // 4.2 获取分类名字
            ParsedStringTerms catalogNameAgg = subCatalogAggs.get("catalogNameAgg");
            String catalogName = catalogNameAgg.getBuckets().get(0).getKeyAsString();

            // 4.3 封装分类VO
            SearchResult.CatalogVo catalogVo = new SearchResult.CatalogVo(catalogId, catalogName);
            catalogVos.add(catalogVo);
        }
        searchResult.setCatalogs(catalogVos);

        /* 5. 封装聚合的属性信息 */
        List<SearchResult.AttrVo> attrVos = new ArrayList<>();
        // ParsedNested用于接收内置属性的聚合
        ParsedNested parsedNested = aggregations.get("attrs");
        ParsedLongTerms attrIdAgg = parsedNested.getAggregations().get("attrIdAgg");
        for (Terms.Bucket bucket : attrIdAgg.getBuckets()) {
            // 5.1 获取属性ID
            long attrId = bucket.getKeyAsNumber().longValue();

            Aggregations subAttrAggs = bucket.getAggregations();
            // 5.2 获取属性名
            ParsedStringTerms attrNameAgg = subAttrAggs.get("attrNameAgg");
            String attrName = attrNameAgg.getBuckets().get(0).getKeyAsString();
            // 5.3 获取属性值
            ParsedStringTerms attrValueAgg = subAttrAggs.get("attrValueAgg");
            List<String> attrValues = new ArrayList<>();
            for (Terms.Bucket attrValueAggBucket : attrValueAgg.getBuckets()) {
                String attrValue = attrValueAggBucket.getKeyAsString();
                attrValues.add(attrValue);
            }

            // 5.4 封装属性VO
            SearchResult.AttrVo attrVo = new SearchResult.AttrVo(attrId, attrName, attrValues);
            attrVos.add(attrVo);
        }
        searchResult.setAttrs(attrVos);

        /* 6. 构建面包屑导航 */
        List<String> attrs = searchParam.getAttrs();
        if (attrs != null && attrs.size() > 0) {
            List<SearchResult.NavVo> navVos = attrs.stream().map(attr -> {
                SearchResult.NavVo navVo = new SearchResult.NavVo();
                String[] s = attr.split("_");

                // 6.1 设置属性值
                navVo.setNavValue(s[1]);

                // 6.2 添加到属性ID
                searchResult.getAttrIds().add(Long.parseLong(s[0]));

                // 6.3 查询并设置属性名
                R r = productFeignService.getAttrInfo(Long.parseLong(s[0]));
                if (r.getCode() == 0) {
                    AttrResponseVo attrResponseVo = r.getData("attr", new TypeReference<AttrResponseVo>() { });
                    navVo.setNavName(attrResponseVo.getAttrName());
                } else { // 失败，使用id作为名称
                    navVo.setNavName(s[0]);
                }

                // 6.4 设置面包屑跳转连接
                String replaceQuery = replaceQueryString(searchParam.get_queryString(), "attrs", attr);
                navVo.setLink(SEARCH_LINK + "?" + replaceQuery);

                return navVo;
            }).collect(Collectors.toList());

            searchResult.setNavs(navVos);
        }

        /* 7. 根据品牌设置导航栏新的跳转链接 */
        if (searchParam.getBrandId() != null && searchParam.getBrandId().size() > 0) {
            SearchResult.NavVo navVo = new SearchResult.NavVo();
            navVo.setNavName("品牌");

            R r = productFeignService.getBrandInfo(searchParam.getBrandId());
            if (r.getCode() == 0) {
                List<BrandVo> brands = r.getData("data", new TypeReference<List<BrandVo>>() { });
                StringBuilder builder = new StringBuilder();
                String replaceQuery = "";

                // 替换所有品牌ID
                for (BrandVo brandVo : brands) {
                    builder.append(brandVo.getBrandName()).append(';');
                    replaceQuery = replaceQueryString(searchParam.get_queryString(), "branId", brandVo.getBrandId() + "");
                }

                navVo.setNavValue(builder.toString());
                navVo.setLink(SEARCH_LINK + "?" + replaceQuery);
            }

            List<SearchResult.NavVo> navs = searchResult.getNavs();
            if (navs == null) { // 注意判空
                navs = new ArrayList<>();
            }
            navs.add(navVo);
        }

        return searchResult;
    }

    /**
     * 替换原查询字符串
     * @param queryStr 查询条件
     * @param key      值
     * @param value    键
     * @return 新的URL
     */
    private String replaceQueryString(String queryStr, String key, String value) {
        String urlValue = null;

        try {
            urlValue = URLEncoder.encode(value, "UTF-8");
            urlValue = urlValue.replace("+", "%20");
            // log.debug("URL encode: [{}] => [{}]", value, urlValue);
        } catch (UnsupportedEncodingException e) {
            log.error("URL编码类型错误", e);
        }

        return queryStr.replace(key + "=" + urlValue, "")
                .replace("&" + key + "=" + urlValue, "");
    }

}
