package net.bncloud.saas.sys.web;

import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.saas.sys.domain.SearchHistory;
import net.bncloud.saas.sys.service.SearchHistoryService;
import net.bncloud.saas.sys.service.command.ClearSearchHistoryCommand;
import net.bncloud.saas.sys.service.command.CreateSearchHistoryCommand;
import net.bncloud.saas.sys.service.query.SearchHistoryQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName SearchHistoryResource
 * @Author Administrator
 * @Date 2021/5/10
 * @Version V1.0
 **/
@RestController
@RequestMapping("/sys/search/history")
public class SearchHistoryResource {

    private final SearchHistoryService searchHistoryService;

    public SearchHistoryResource(SearchHistoryService searchHistoryService) {
        this.searchHistoryService = searchHistoryService;
    }

    @PostMapping("/page")
    public R<Page<SearchHistory>> pageQuery(@RequestBody QueryParam<SearchHistoryQuery> query, Pageable pageable) {
        return R.data(searchHistoryService.pageQuery(query, pageable));
    }

    @PostMapping("/save")
    public R<Void> save(@Validated @RequestBody CreateSearchHistoryCommand command) {
        searchHistoryService.save(command);
        return R.success();
    }

    @DeleteMapping("/clear")
    public R<Void> clear(@Validated @RequestBody ClearSearchHistoryCommand command) {
        searchHistoryService.clear(command);
        return R.success();
    }


}
