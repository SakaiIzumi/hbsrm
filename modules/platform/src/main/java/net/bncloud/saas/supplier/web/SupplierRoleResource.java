package net.bncloud.saas.supplier.web;


import io.swagger.annotations.ApiOperation;
import net.bncloud.api.feign.saas.user.SubjectType;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.security.SecurityUtils;
import net.bncloud.saas.authorize.service.RoleService;
import net.bncloud.saas.tenant.domain.vo.BatchRoleMenuVO;
import net.bncloud.saas.tenant.domain.vo.BatchUserRoleVO;
import net.bncloud.saas.tenant.domain.vo.RoleCopyVO;
import net.bncloud.saas.tenant.service.query.UserRoleQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/supplier/role")
public class SupplierRoleResource {

    @Autowired
    private RoleService roleService;


    @ApiOperation("查询角色树")
    @GetMapping("/tree/{supplierId}")
    public R tree(@PathVariable Long supplierId) {
        return R.data(roleService.findRoleTreeBySubjectIdAndSubjectType(supplierId, SubjectType.supplier));
    }

    @ApiOperation("查询角色详情")
    @GetMapping("/info/{roleId}")
    public R info(@PathVariable Long roleId) {
        return R.data(roleService.info(roleId));
    }


//    @ApiOperation(value = "角色分配权限")
//    @PutMapping("/assignMenuPerms")
//    public R assignMenuPerms(@RequestBody RolePermVO vo) {
//        roleService.assignMenuPerms(vo);
//        return R.success();
//    }


    @ApiOperation(value = "复制角色")
    @PostMapping("/copy")
    public R copy(@RequestBody RoleCopyVO vo) {
        SecurityUtils.getCurrentSupplier().ifPresent(supplier -> {
            roleService.copy(vo, supplier.getSupplierId(), false);
        });
        return R.success();
    }

    @ApiOperation("根据角色查询相关成员")
    @PostMapping("/memberInRoleTree")
    public R memberInRoleTree(@RequestBody QueryParam<UserRoleQuery> queryParam, Pageable pageable) {
        return R.data(roleService.supplierMemberInRoleTree(queryParam, pageable));
    }


    @ApiOperation("批量赋予角色")
    @PostMapping("/batchGrantRole")
    public R batchGrantRole(@RequestBody BatchUserRoleVO vo) {
        roleService.batchGrantRole(vo, SubjectType.supplier);
        return R.success();
    }


    @ApiOperation("批量赋予权限菜单 数据权限")
    @PostMapping("/batchGrantMenu")
    public R batchGrant(@RequestBody BatchRoleMenuVO vo) {
        roleService.batchGrant(vo);
        return R.success();
    }


    @ApiOperation("批量移除角色")
    @DeleteMapping("/batchRemoveRole")
    public R batchRemoveRole(@RequestBody BatchUserRoleVO vo) {
        roleService.batchRemoveRole(vo, SubjectType.supplier);
        return R.success();
    }

}
