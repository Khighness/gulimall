package top.parak.gulimall.search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import top.parak.gulimall.search.service.MallSearchService;
import top.parak.gulimall.search.vo.SearchParam;
import top.parak.gulimall.search.vo.SearchResult;

import javax.servlet.http.HttpServletRequest;

/**
 * @author KHighness
 * @since 2021-12-13
 */
@Controller
public class SearchController {

    @Autowired
    private MallSearchService mallSearchService;

    @GetMapping("/list.html")
    public String listPage(HttpServletRequest request, SearchParam searchParam, Model model) {
        searchParam.set_queryString(request.getQueryString());
        SearchResult result = mallSearchService.search(searchParam);
        model.addAttribute("result", result);

        return "list";
    }

}
