package net.bncloud.saas.tenant.web;

import io.swagger.annotations.ApiOperation;
import net.bncloud.common.api.R;
import net.bncloud.common.security.LoginInfo;
import net.bncloud.common.security.Org;
import net.bncloud.common.security.SecurityUtils;
import net.bncloud.saas.tenant.domain.OrgDepartment;
import net.bncloud.saas.tenant.service.DepartmentService;
import net.bncloud.saas.tenant.service.command.BulkUpdateParentCommand;
import net.bncloud.saas.tenant.service.command.CreateDeptCommand;
import net.bncloud.saas.tenant.service.dto.OrgDepartmentDTO;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tenant/org/dept")
public class OrgDepartmentResource {
    private final DepartmentService departmentService;
    public OrgDepartmentResource(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @ApiOperation("新增部门")
    @PostMapping
    public R<Void> create(@RequestBody CreateDeptCommand command){
        departmentService.createDept(command);
        return R.success();
    }

    @DeleteMapping("/{id}")
    public R<Void> deleteById(@PathVariable Long id) {
        departmentService.deleteById(id);
        return R.success();
    }

    @DeleteMapping("/bulk")
    public R<Void> bulkDelete(@RequestBody List<Long> ids) {
        departmentService.bulkDelete(ids);
        return R.success();
    }

    @ApiOperation ("修改部门")
    @PutMapping
    public R<Void> update(@RequestBody OrgDepartment resources){
        if (resources.getId() == null) {
            throw new IllegalArgumentException("id不能为空");
        }
        departmentService.update(resources);
        return R.success();
    }

    @ApiOperation ("批量修改父级部门")
    @PutMapping("/bulkUpdateParent")
    public R<Void> bulkUpdateParent(@RequestBody BulkUpdateParentCommand command){
        departmentService.bulkUpdateParent(command);
        return R.success();
    }

    @GetMapping("/invite")
    public R<String> inviteUrl() {
        LoginInfo loginInfo = SecurityUtils.getLoginInfoOrThrow();
        Org org = loginInfo.getCurrentOrg();
        Long id = org.getId();
        return R.success();
    }

    /**
     * 根据客户（采购方）编码查询部门列表
     * @return
     */
    @ApiOperation("根据采购方编码查询部门列表")
    @GetMapping("getListByCompanyCode/{companyCode}")
    public R<List<OrgDepartmentDTO>> getListByCompanyCode(@PathVariable String companyCode){
        List<OrgDepartmentDTO> departmentList = departmentService.getListByCompanyCode(companyCode);
        return R.data(departmentList);
    }

    /**
     * 根据部门parentID查询部门列表
     * @return 部门列表
     */
    @ApiOperation("根据部门parentID查询部门列表")
    @GetMapping("getListByParentId")
    public R<List<OrgDepartmentDTO>> getListByParentId(@RequestParam(value = "parentId",required = false) Long parentId){
        List<OrgDepartmentDTO> departmentList = departmentService.getListByParentId(parentId);
        return R.data(departmentList);
    }
}