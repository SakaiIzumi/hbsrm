package net.bncloud.saas.supplier.web;

import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.saas.supplier.domain.vo.SupplierStaffVO;
import net.bncloud.saas.supplier.service.SupplierStaffService;
import net.bncloud.saas.supplier.service.query.SupplierStaffQuery;
import net.bncloud.saas.supplier.vo.UserStaffVO;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/supplier/staff")
@AllArgsConstructor
public class SupplierStaffResource {
    private final SupplierStaffService supplierStaffService;


    @ApiOperation(value = "查询供应商成员信息", notes = "根据类型查询管理员/普通成员,全部用户")
    @PostMapping("/supplierMemberTable")
    public R supplierMemberTable(@RequestBody QueryParam<SupplierStaffQuery> query, Pageable pageable) {
        return R.data(supplierStaffService.supplierMemberTable(query, pageable));
    }


    @ApiOperation(value = "根据关联组织查询供应商成员列表", notes = "根据类型查询管理员/普通成员,全部用户")
    @PostMapping("/supplierStaffTableByOrgId")
    public R supplierStaffTableByOrgId(@RequestBody QueryParam<SupplierStaffQuery> query, Pageable pageable) {
        return R.data(supplierStaffService.supplierStaffTableByOrgId(query, pageable));
    }


    @ApiOperation(value = "加载供应商列表")
    @GetMapping("/loadRelateSuppliers")
    public R loadRelateSuppliers() {
        return R.data(supplierStaffService.loadRelateSuppliers());
    }


    @ApiOperation(value = "新增成员(供应商)")
    @PostMapping("/createMember")
    public R createMember(@RequestBody SupplierStaffVO vo) {
        supplierStaffService.createMember(vo);
        return R.success();
    }


    @ApiOperation(value = "新增成员(供应商)-并赋予角色")
    @PostMapping("/createMemberWithRole")
    public R createMemberWithRole(@RequestBody SupplierStaffVO vo) {
        supplierStaffService.createMemberWithRole(vo);
        return R.success();
    }


    @ApiOperation(value = "供应商成员详情")
    @GetMapping("/memberDetail/{id}")
    public R memberDetail(@PathVariable Long id) {
        return R.data(supplierStaffService.memberDetail(id));
    }

    @ApiOperation(value = "[人员管理]供应商成员编辑详情回显")
    @GetMapping("/editInfo")
    public R editInfo(@RequestParam Long id) {
        return R.data(supplierStaffService.editInfo(id));
    }

    @ApiOperation(value = "[人员管理]更新供应商成员编辑详情页")
    @PutMapping("/update/info")
    public R updateInfo(@RequestBody UserStaffVO vo) {
        supplierStaffService.updateInfo(vo);
        return R.success();
    }


}
