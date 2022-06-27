package net.bncloud.saas.supplier.web;

import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.saas.supplier.domain.vo.BatchSupplierManagerVO;
import net.bncloud.saas.supplier.service.SupplierManagerService;
import net.bncloud.saas.supplier.service.query.SupplierManagerQuery;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/supplier/manager")
@AllArgsConstructor
public class SupplierManagerResource {


    private final SupplierManagerService supplierManagerService;


    @ApiOperation(value = "[管理员设置]批量添加管理员")
    @PostMapping("/batchAddSupplierManager")
    public R batchAddSupplierManager(@RequestBody BatchSupplierManagerVO vo) {
        supplierManagerService.batchAddSupplierManager(vo);
        return R.success();
    }

    @ApiOperation(value = "[管理员设置]转让管理员详情回显")
    @GetMapping("/checkTransfer/{supplierManagerId}")
    public R checkTransfer(@PathVariable Long supplierManagerId) {
        return R.data(supplierManagerService.checkTransfer(supplierManagerId));
    }

    @ApiOperation(value = "查询供应商管理员")
    @PostMapping("/supplierManagerTable")
    public R supplierManagerTable(@RequestBody QueryParam<SupplierManagerQuery> query, Pageable pageable) {
        return R.data(supplierManagerService.supplierManagerTable(query, pageable));
    }

    @ApiOperation(value = "根据关联组织查询供应商成员列表", notes = "根据类型查询管理员/普通成员,全部用户")
    @PostMapping("/supplierManagerTableByOrgId")
    public R supplierManagerTableByOrgId(@RequestBody QueryParam<SupplierManagerQuery> query, Pageable pageable) {
        return R.data(supplierManagerService.supplierManagerTableByOrgId(query, pageable));
    }


}
