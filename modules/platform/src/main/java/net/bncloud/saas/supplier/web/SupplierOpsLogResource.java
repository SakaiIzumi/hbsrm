package net.bncloud.saas.supplier.web;

import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.logging.annotation.SysLog;
import net.bncloud.saas.supplier.domain.SupplierOpsLog;
import net.bncloud.saas.supplier.service.SupplierOpsLogService;
import net.bncloud.saas.supplier.service.query.SupplierOpsLogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/supplier/ops/OpsLog")
@AllArgsConstructor
public class SupplierOpsLogResource {

    @Autowired
    private SupplierOpsLogService supplierOpsLogService;


    @GetMapping("/selectList")
    @ApiOperation(value = "查询", notes = "")
    public R selectList() {
        return R.data(supplierOpsLogService.selectList());
    }

    @GetMapping("/getById/{id}")
    @ApiOperation(value = "查询", notes = "传入id")
    public R<SupplierOpsLog> getById(@PathVariable(value = "id") Long id) {
        return R.data(supplierOpsLogService.getById(id));
    }

    /**
     * 新增
     */
    @PostMapping("/save")
    @ApiOperation(value = "新增", notes = "传入SupplierOpsLog")
    public R save(@RequestBody @Validated SupplierOpsLog supplierOpsLog) {
        supplierOpsLogService.save(supplierOpsLog);
        return R.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    @ApiOperation(value = "删除", notes = "传入id")
    public R delete(@RequestBody @Validated Long id) {
        supplierOpsLogService.deleteById(id);
        return R.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/deleteById/{id}")
    @ApiOperation(value = "根据id删除", notes = "传入多个id")
    public R<Void> deleteById(@PathVariable(value = "id") String ids) {
        String[] idsStrs = ids.split(",");
        for (String id : idsStrs) {
            supplierOpsLogService.deleteById(Long.parseLong(id));
        }
        return R.success();
    }


    @SysLog(action = "操作记录分页查询")
    @PostMapping("/pageQuery")
    public R pageQuery(@RequestBody QueryParam<SupplierOpsLogQuery> queryParam , Pageable pageable) {
        return R.data(supplierOpsLogService.queryPage(queryParam, pageable));
    }

}
