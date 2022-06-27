package net.bncloud.saas.user.web;

import io.swagger.annotations.ApiOperation;
import net.bncloud.api.feign.saas.user.SubjectType;
import net.bncloud.common.api.R;
import net.bncloud.saas.user.service.LoginUserService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user-center/login/user")
public class LoginUserResource {

    private final LoginUserService loginUserService;

    public LoginUserResource(LoginUserService loginUserService) {
        this.loginUserService = loginUserService;
    }

    @ApiOperation(value = "[首页]切换组织")
    @PostMapping("/switchOrg/{orgId}")
    public R switchOrg(@PathVariable Long orgId) {
        loginUserService.switchCurrentSubject(SubjectType.org, orgId);
        return R.success();
    }

    @ApiOperation(value = "切换供应商")
    @PostMapping("/switchSupplier/{supplierId}")
    public R switchSupplier(@PathVariable Long supplierId) {
        loginUserService.switchCurrentSubject(SubjectType.supplier, supplierId);
        return R.success();
    }


}
