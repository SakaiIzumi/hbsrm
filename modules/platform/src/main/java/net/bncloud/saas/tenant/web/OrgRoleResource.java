package net.bncloud.saas.tenant.web;


import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.ApiOperation;
import net.bncloud.api.feign.saas.user.SubjectType;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.security.SecurityUtils;
import net.bncloud.common.util.ObjectUtil;
import net.bncloud.saas.authorize.service.RoleService;
import net.bncloud.saas.tenant.domain.OrgEmployeeMangeScope;
import net.bncloud.saas.tenant.domain.vo.BatchRoleMenuVO;
import net.bncloud.saas.tenant.domain.vo.BatchUserRoleVO;
import net.bncloud.saas.tenant.domain.vo.RoleCopyVO;
import net.bncloud.saas.tenant.service.dto.OrgEmployeeMangeScopeDTO;
import net.bncloud.saas.tenant.service.query.UserRoleQuery;
import net.bncloud.saas.tenant.web.param.OrgEmployeeMangeScopeListParam;
import net.bncloud.saas.tenant.web.param.OrgEmployeeMangeScopeParam;
import net.bncloud.saas.tenant.web.param.OrgEmployeeMangeUserIdsListAndScopeParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 询报价权限控制
 *
 * @author Auto-generator
 * @since 2022-02-14
 */
@RestController
@RequestMapping("/tenant/org/role")
public class OrgRoleResource {

    @Autowired
    private RoleService roleService;

    @ApiOperation("查询角色树")
    @GetMapping("/tree/{orgId}")
    public R tree(@PathVariable Long orgId) {
        return R.data(roleService.findRoleTreeBySubjectIdAndSubjectType(orgId, SubjectType.org));
    }

    @ApiOperation("查询角色详情")
    @GetMapping("/info/{roleId}")
    public R info(@PathVariable Long roleId) {
        return R.data(roleService.info(roleId));
    }

    @ApiOperation(value = "复制角色")
    @PostMapping("/copy")
    public R copy(@RequestBody RoleCopyVO vo) {
        SecurityUtils.getCurrentOrg().ifPresent(org -> {
            roleService.copy(vo, org.getId(), true);
        });
        return R.success();
    }

    @ApiOperation("根据角色查询相关成员")
    @PostMapping("/memberInRoleTree")
    public R memberInRoleTree(@RequestBody QueryParam<UserRoleQuery> queryParam, Pageable pageable) {
        return R.data(roleService.orgMemberInRoleTree(queryParam, pageable));
    }


    @ApiOperation("批量赋予角色")
    @PostMapping("/batchGrantRole")
    public R batchGrantRole(@RequestBody BatchUserRoleVO vo) {
        roleService.batchGrantRole(vo, SubjectType.org);

        /*//保存询价单权限
        if(StrUtil.isNotEmpty(vo.getBoundary())){
            List<Long> userIds = vo.getUserIds();
            String boundary = vo.getBoundary();
            List<OrgEmployeeMangeScopeParam> orgEmployeeMangeScopeParamList =new ArrayList<>();
            for (Long userId : userIds) {
                OrgEmployeeMangeScopeParam orgEmployeeMangeScopeParam = new OrgEmployeeMangeScopeParam();
                orgEmployeeMangeScopeParam.setEmployeeId(userId);
                orgEmployeeMangeScopeParam.setBoundary(boundary);
                orgEmployeeMangeScopeParamList.add(orgEmployeeMangeScopeParam);
            }
            OrgEmployeeMangeScopeListParam orgEmployeeMangeScopeListParam = new OrgEmployeeMangeScopeListParam();
            orgEmployeeMangeScopeListParam.setOrgEmployeeMangeScopeParamList(orgEmployeeMangeScopeParamList);
            roleService.saveQuotationRoleBatch(orgEmployeeMangeScopeListParam);
        }*/

        return R.success();
    }

    @ApiOperation("批量赋予菜单")
    @PostMapping("/batchGrantMenu")
    public R batchGrantMenu(@RequestBody BatchRoleMenuVO vo) {
        roleService.batchGrant(vo);
        return R.success();
    }


    @ApiOperation("批量解除角色")
    @DeleteMapping("/batchRemoveRole")
    public R batchRemoveRole(@RequestBody BatchUserRoleVO vo) {
        roleService.batchRemoveRole(vo, SubjectType.org);
        return R.success();
    }

    @ApiOperation("采购方员工添加询价单查询权限")
    @PostMapping("/saveQuotationRole")
    public R saveQuotationRole(@RequestBody OrgEmployeeMangeScopeParam param) {
        OrgEmployeeMangeScope scope = OrgEmployeeMangeScope
                .builder()
                .employeeId(param.getEmployeeId())
                .scope(param.getBoundary())
                .build();
        roleService.saveQuotationRole(scope);
        return R.success();
    }

    @ApiOperation("查询采购方员工拥有的询价单查询权限")
    @PostMapping("/queryQuotationRole/{id}")
    public R<OrgEmployeeMangeScopeDTO> queryQuotationRole(@PathVariable Long id) {
        OrgEmployeeMangeScope orgEmployeeMangeScope=roleService.findByEmployeeId(id);
        if(ObjectUtil.isEmpty(orgEmployeeMangeScope)){
            return R.data(new OrgEmployeeMangeScopeDTO());
        }
        OrgEmployeeMangeScopeDTO scopeDto = OrgEmployeeMangeScopeDTO
                .builder()
                .employeeId(orgEmployeeMangeScope.getEmployeeId())
                .scope(orgEmployeeMangeScope.getScope())
                .boundary(orgEmployeeMangeScope.getScope())
                .id(orgEmployeeMangeScope.getId())
                .build();

        return R.data(scopeDto);
    }

    @ApiOperation("批量保存采购方员工拥有的询价单权限")
    @PostMapping("/saveQuotationRoleBatch")
    public R saveQuotationRoleBatch(@RequestBody OrgEmployeeMangeUserIdsListAndScopeParam param) {



        roleService.saveQuotationRoleBatch(param);
        return R.success();
    }


    @ApiOperation("批量查询")
    @PostMapping("/queryQuotationRoleAll")
    public R<List<OrgEmployeeMangeScope>> queryQuotationRoleAll() {
        List<OrgEmployeeMangeScope> orgEmployeeMangeScopeList=roleService.findAllRoleQuotation();

        return R.data(orgEmployeeMangeScopeList);
    }


}
