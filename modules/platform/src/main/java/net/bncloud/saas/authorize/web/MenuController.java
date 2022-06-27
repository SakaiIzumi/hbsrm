package net.bncloud.saas.authorize.web;

import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import net.bncloud.api.feign.saas.user.SubjectType;
import net.bncloud.common.api.R;
import net.bncloud.common.constants.SysType;
import net.bncloud.common.security.LoginInfo;
import net.bncloud.common.security.SecurityUtils;
import net.bncloud.saas.authorize.domain.Menu;
import net.bncloud.saas.authorize.service.MenuService;
import net.bncloud.saas.authorize.service.dto.MenuDTO;
import net.bncloud.saas.authorize.service.query.MenuQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/sys/menu")
@AllArgsConstructor
public class MenuController {

    private final MenuService menuService;

    //TODO 后面统一api
    @ApiOperation(value = "按类型查询默认菜单")
    @GetMapping("/menuTree/{subjectType}")
    public R menuTree(@PathVariable SubjectType subjectType) {
        return R.data(menuService.menuTreeBySubjectType(subjectType));
    }
}
