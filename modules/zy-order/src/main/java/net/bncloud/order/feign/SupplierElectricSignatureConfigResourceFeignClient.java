package net.bncloud.order.feign;

import net.bncloud.common.api.R;
import net.bncloud.order.constants.ElectricSignatureConfigType;
import net.bncloud.order.entity.SupplierElectricSignatureConfig;
import net.bncloud.order.feign.fallback.SupplierElectricSignatureConfigFallbackFactory;
import net.bncloud.security.client.AuthorizedFeignClient;
import org.springframework.web.bind.annotation.*;

@AuthorizedFeignClient(name = "platform",contextId = "supplierElectricSignatureConfigResourceFeignClient" ,fallbackFactory = SupplierElectricSignatureConfigFallbackFactory.class, decode404 = true)
public interface SupplierElectricSignatureConfigResourceFeignClient {

    @GetMapping("/sys/electric/signature/config/getByCode/{code}/{type}")
    public R<SupplierElectricSignatureConfig> getByCodeAndType(@PathVariable("code") String code, @PathVariable("type") ElectricSignatureConfigType type);
}
