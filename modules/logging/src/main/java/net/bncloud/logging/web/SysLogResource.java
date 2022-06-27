package net.bncloud.logging.web;

import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.logging.domain.SysLog;
import net.bncloud.logging.service.SysLogService;
import net.bncloud.logging.service.query.SysLogQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/logging/sys/logs")
public class SysLogResource {

    private final SysLogService sysLogService;


    public SysLogResource(SysLogService sysLogService) {
        this.sysLogService = sysLogService;
    }

    @net.bncloud.logging.annotation.SysLog(module = "操作日志", action = "操作日志查询", ignoreResponse = true)
    @PostMapping
    public R<Page<SysLog>> sysLogs(@RequestBody QueryParam<SysLogQuery> query,
                                   @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return R.data(sysLogService.pageQuery(query, pageable));
    }

    @net.bncloud.logging.annotation.SysLog(module = "操作日志", action = "操作日志详情", ignoreResponse = true)
    @GetMapping("{id}")
    public R<SysLog> findById(@PathVariable Long id) {
        return R.data(sysLogService.findById(id));
    }

}
