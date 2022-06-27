package net.bncloud.logging.web;

import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.logging.domain.LoginLog;
import net.bncloud.logging.service.LoginLogService;
import net.bncloud.logging.service.query.LoginLogQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/logging/login/logs")
public class LoginLogResource {

    private final LoginLogService loginLogService;

    public LoginLogResource(LoginLogService loginLogService) {
        this.loginLogService = loginLogService;
    }

    @PostMapping
    public R<Page<LoginLog>> pageQuery(@RequestBody QueryParam<LoginLogQuery> queryParam, Pageable pageable) {
        return R.data(loginLogService.pageQuery(queryParam, pageable));
    }

}
