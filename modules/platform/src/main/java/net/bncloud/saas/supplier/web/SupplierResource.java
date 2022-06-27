package net.bncloud.saas.supplier.web;

import io.swagger.annotations.ApiOperation;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.logging.annotation.SysLog;
import net.bncloud.saas.supplier.domain.Supplier;
import net.bncloud.saas.supplier.domain.Tag;
import net.bncloud.saas.supplier.domain.Type;
import net.bncloud.saas.supplier.service.SupplierService;
import net.bncloud.saas.supplier.service.command.CreateSupplierCommand;
import net.bncloud.saas.supplier.service.command.ManualSyncCommand;
import net.bncloud.saas.supplier.service.dto.SupplierDTO;
import net.bncloud.saas.supplier.service.query.SupplierQuery;
import net.bncloud.saas.supplier.web.vm.SupplierTags;
import net.bncloud.saas.supplier.web.vm.SupplierTypes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/supplier")
public class SupplierResource {

    private final SupplierService supplierService;


    public SupplierResource(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @PostMapping("/suppliers/add")
    public R<Supplier> create(@RequestBody CreateSupplierCommand command) {
        Supplier result = supplierService.createSupplier(command);
        return R.data(result);
    }

    @PutMapping("/suppliers/uploadAvatarUrl")
    public R<Void> uploadAvatarUrl(@RequestBody Supplier supplier) {
        supplierService.uploadAvatarUrl(supplier);
        return R.success();
    }

    @PostMapping("/suppliers/saveTags")
    public R<Void> saveSupplierTags(@RequestBody SupplierTags supplierTags) {
        supplierService.saveSupplierTags(supplierTags);
        return R.success();
    }

    @PostMapping("/suppliers/saveTypes")
    public R<Void> saveSupplierTypes(@RequestBody SupplierTypes supplierTypes) {
        supplierService.saveSupplierTypes(supplierTypes);
        return R.success();
    }


    @PostMapping("/suppliers/checkSupplier")
    public R<Void> checkSupplier(@RequestBody Supplier supplier) {
        return supplierService.checkSupplier(supplier);
    }

    @PostMapping("/suppliers/pageQuery")
    public R<Page<SupplierDTO>> pageQuery(Pageable pageable, @RequestBody QueryParam<SupplierQuery> queryParam) {
        SupplierQuery query = queryParam.getParam();
        Page<SupplierDTO> suppliers = supplierService.pageQuery(query, pageable);
        return R.data(suppliers);
    }

    /**
     * 查询所有的供应商
     * @param queryParam
     * @return
     */
    @PostMapping("/suppliers/allQuery")
    public R<List<SupplierDTO>> allQuery(@RequestBody QueryParam<SupplierQuery> queryParam){
        List<SupplierDTO> supplierDTOS = supplierService.allQuery(queryParam.getParam());
        return R.data(supplierDTOS);
    }

    @PostMapping("/suppliers/page")
    public R page(@RequestBody QueryParam<SupplierQuery> queryParam, Pageable pageable) {
        return R.data(supplierService.pageQuery(queryParam, pageable));
    }


    @GetMapping("/getByCode/{code}")
    public R<Supplier> getByCode(@PathVariable(value = "code") String code) {
        return R.data(supplierService.getByCode(code));
    }


    @PutMapping("/suppliers/disable/{id}")
    public R<Void> disable(@PathVariable Long id) {
        supplierService.disable(id);
        return R.success();
    }

    @PutMapping("/suppliers/enable/{id}")
    public R<Void> enable(@PathVariable Long id) {
        supplierService.enable(id);
        return R.success();
    }

    @GetMapping("/getById/{id}")
    public R<SupplierDTO> getById(@PathVariable(value = "id") Long id) {
        return R.data(supplierService.getById(id));
    }

    @SysLog(action = "标签查询")
    @GetMapping("/getAllTag")
    public R<List<Tag>> getAllTag() {
        return R.data(supplierService.getAllTag());
    }

    @SysLog(action = "类型查询")
    @GetMapping("/getAllType")
    public R<List<Type>> getAllType() {
        return R.data(supplierService.getAllType());
    }

    /**
     * 继续合作
     *
     * @param id
     * @return
     */
    @PutMapping("/continueToCooperate/{id}")
    public R<Void> continueToCooperate(@PathVariable(value = "id") Long id) {
        supplierService.relevance(id);
        return R.success();
    }

    /**
     * 暂停合作
     *
     * @param id
     * @return
     */
    @PutMapping("/suspendCooperation/{id}")
    public R<Void> suspendCooperation(@PathVariable(value = "id") Long id) {
        supplierService.suspendCooperation(id);
        return R.success();
    }

    /**
     * 冻结
     *
     * @param id
     * @return
     */
    @PutMapping("/frozen/{id}")
    public R<Void> frozen(@PathVariable(value = "id") Long id) {
        supplierService.frozen(id);
        return R.success();
    }

    /**
     * 取消冻结
     *
     * @param id
     * @return
     */
    @PutMapping("/cancelFrozen/{id}")
    public R<Void> cancelFrozen(@PathVariable(value = "id") Long id) {
        supplierService.relevance(id);
        return R.success();
    }

    /**
     * @return
     */
    @ApiOperation(value = "手动同步供应商")
    @PostMapping("/manualSync")
    public R<Void> manualSyncCommand(@RequestBody(required = false) ManualSyncCommand manualSyncCommand) {
        supplierService.manualSyncCommand(manualSyncCommand);
        return R.success();
    }
}
