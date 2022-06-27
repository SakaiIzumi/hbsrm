package net.bncloud.saas.sys.web;

import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import net.bncloud.api.feign.saas.user.SubjectType;
import net.bncloud.common.api.R;
import net.bncloud.saas.authorize.service.RoleService;
import net.bncloud.saas.sys.domain.vo.RolePermVO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sys/defrole")
@AllArgsConstructor
public class DefRoleController {

    private final RoleService roleService;

    @ApiOperation(value = "分别查询采购方和供应商默认角色分组")
    @GetMapping("/groups")
    public R groups() {
        return R.data(roleService.findDefTreeGroupBySubjectType());
    }

    @ApiOperation(value = "查询默认角色详情")
    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        return R.data(roleService.info(id));
    }


    @GetMapping("/tree/{subjectType}")
    public R queryGroupBySubject(@PathVariable SubjectType subjectType) {
        return R.data(roleService.queryGroupBySubject(subjectType));
    }

    @ApiOperation(value = "默认角色分配权限")
    @PutMapping("/assignMenuPerms")
    public R assignMenuPerms(@RequestBody RolePermVO vo) {
        roleService.assignMenuPerms(vo);
        return R.success();
    }

}
