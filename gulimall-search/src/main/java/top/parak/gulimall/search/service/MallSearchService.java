package top.parak.gulimall.search.service;

import top.parak.gulimall.search.vo.SearchParam;
import top.parak.gulimall.search.vo.SearchResult;

/**
 * @author KHighness
 * @since 2021-12-13
 */
public interface MallSearchService {

    /**
     * 商品检索
     * @param searchParam 检索条件
     * @return 检索结果
     */
    SearchResult search(SearchParam searchParam);

}
