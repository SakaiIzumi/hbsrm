package net.bncloud.saas.supplier.web;

import net.bncloud.common.api.R;
import net.bncloud.common.security.SecurityUtils;
import net.bncloud.logging.annotation.SysLog;
import net.bncloud.saas.supplier.domain.Tag;
import net.bncloud.saas.supplier.domain.TagConfigItem;
import net.bncloud.saas.supplier.domain.Type;
import net.bncloud.saas.supplier.domain.TypeConfigItem;
import net.bncloud.saas.supplier.service.SupplierConfigService;
import net.bncloud.saas.supplier.service.query.ConfigQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/supplier/config")
public class SupplierConfigResource {

    private final SupplierConfigService supplierConfigService;

    public SupplierConfigResource(SupplierConfigService supplierConfigService) {
        this.supplierConfigService = supplierConfigService;
    }

    @SysLog(action = "标签分页查询")
    @PostMapping("/tags/pageQuery")
    public R<Page<Tag>> tags(@RequestBody(required = false) ConfigQuery param, Pageable pageable) {
        final Page<Tag> tags = SecurityUtils.getCurrentOrg()
                .map(org -> supplierConfigService.pageQueryTagConfig(org.getId(), param, pageable))
                .orElse(Page.empty());
        return R.data(tags);
    }

    @SysLog(action = "标签管理")
    @PostMapping("/tags")
    public R<Void> saveTagConfig(@RequestBody Tag tag) {
        supplierConfigService.saveTagConfig(tag);
        return R.success();
    }

    @DeleteMapping("/tags/{id}")
    public R<Void> deleteTagConfig(@PathVariable Long id) {
        supplierConfigService.deleteTagConfig(id);
        return R.success();
    }

    @SysLog(action = "修改供应商标签")
    @PutMapping("/saveTagConfigItem")
    public R<Void> saveTagConfigItem(@RequestBody TagConfigItem tagConfigItem) {
        supplierConfigService.saveTagConfig(tagConfigItem);
        return R.success();
    }

    @SysLog(action = "修改供应商类型")
    @PutMapping("/saveTypeConfigItem")
    public R<Void> saveTypeConfigItem(@RequestBody TypeConfigItem typeConfigItem) {
        supplierConfigService.saveTypeConfigItem(typeConfigItem);
        return R.success();
    }

    @SysLog(action = "删除供应商标签")
    @DeleteMapping("/tagItem/{id}")
    public R<Void> deleteTagItem(@PathVariable Long id) {
        supplierConfigService.deleteTagItem(id);
        return R.success();
    }

    @SysLog(action = "删除供应商类型")
    @DeleteMapping("/typeItem/{id}")
    public R<Void> deleteTypeItem(@PathVariable Long id) {
        supplierConfigService.deleteTypeItem(id);
        return R.success();
    }

    @SysLog(action = "类型分页查询")
    @PostMapping("/types/pageQuery")
    public R<Page<Type>> types(@RequestBody(required = false) ConfigQuery param, Pageable pageable) {
        final Page<Type> types = SecurityUtils.getCurrentOrg()
                .map(org -> supplierConfigService.pageQueryTypeConfig(org.getId(), param, pageable))
                .orElse(Page.empty());
        return R.data(types);
    }

    @PostMapping("/types")
    public R<Void> saveTypeConfig(@RequestBody Type type) {
        supplierConfigService.saveTypeConfig(type);
        return R.success();
    }

    @DeleteMapping("/types/{id}")
    public R<Void> deleteTypeConfig(@PathVariable Long id) {
        supplierConfigService.deleteTypeConfig(id);
        return R.success();
    }
}
