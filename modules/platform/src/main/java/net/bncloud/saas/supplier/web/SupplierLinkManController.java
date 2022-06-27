package net.bncloud.saas.supplier.web;

import io.swagger.annotations.ApiOperation;
import net.bncloud.common.api.R;
import net.bncloud.saas.supplier.domain.SupplierLinkMan;
import net.bncloud.saas.supplier.service.SupplierLinkManService;
import net.bncloud.saas.supplier.service.dto.SupplierLinkManDTO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/supplier/linkman")
public class SupplierLinkManController {

    private final SupplierLinkManService supplierLinkManService;

    public SupplierLinkManController(SupplierLinkManService supplierLinkManService){
        this.supplierLinkManService = supplierLinkManService;
    }

    /**
     * 新增
     */
    @PostMapping("/save")
    @ApiOperation(value = "新增", notes = "传入SupplierLinkMan")
    public R save(@RequestBody @Validated SupplierLinkManDTO supplierLinkManDTO){
        supplierLinkManService.saveSupplierLinkMan(supplierLinkManDTO);
        return R.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    @ApiOperation(value = "删除", notes = "传入SupplierLinkMan")
    public R delete(@RequestBody @Validated SupplierLinkManDTO supplierLinkManDTO){
        supplierLinkManService.delete(supplierLinkManDTO);
        return R.success();
    }
    /**
     * 删除
     */
    @DeleteMapping("/deleteById/{id}")
    @ApiOperation(value = "删除", notes = "传入SupplierLinkMan")
    public R<Void> deleteById(@PathVariable(value = "id") String ids){
        String[] idsStrs = ids.split(",");
        for (String id:idsStrs){
            supplierLinkManService.deleteById(Long.parseLong(id));
        }
        return R.success();
    }
}
