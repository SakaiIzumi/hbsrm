package net.bncloud.saas.sys.web;

import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.saas.sys.domain.ElectricSignatureConfigType;
import net.bncloud.saas.sys.domain.SupplierElectricSignatureConfig;
import net.bncloud.saas.sys.service.SupplierElectricSignatureConfigService;
import net.bncloud.saas.sys.service.command.EditSignatureConfigCommand;
import net.bncloud.saas.sys.service.query.SupplierElectricSignatureConfigQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sys/electric/signature/config")
public class SupplierElectricSignatureConfigResource {

    private final SupplierElectricSignatureConfigService supplierElectricSignatureConfigService;

    public SupplierElectricSignatureConfigResource(SupplierElectricSignatureConfigService supplierElectricSignatureConfigService) {
        this.supplierElectricSignatureConfigService = supplierElectricSignatureConfigService;
    }

    @PostMapping("/page")
    public R<Page<SupplierElectricSignatureConfig>> pageQuery(@RequestBody QueryParam<SupplierElectricSignatureConfigQuery> query, Pageable pageable) {
        return R.data(supplierElectricSignatureConfigService.pageQuery(query, pageable));
    }

    @PostMapping("/edit")
    public R<Void> edit(@RequestBody EditSignatureConfigCommand command) {
        supplierElectricSignatureConfigService.edit(command);
        return R.success();
    }

    @GetMapping("/getByCode/{code}/{type}")
    public R<SupplierElectricSignatureConfig> getByCodeAndType(@PathVariable("code") String code,@PathVariable("type") ElectricSignatureConfigType type) {
        return R.data(supplierElectricSignatureConfigService.getByCodeAndType(code,type));
    }

}
