package net.bncloud.saas.supplier.feign;

import net.bncloud.api.feign.saas.org.CreateSupplierDeptRequestDTO;
import net.bncloud.saas.tenant.service.dto.CreateSupplierDeptResultDTO;
import net.bncloud.common.api.R;
import net.bncloud.security.client.AuthorizedFeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@AuthorizedFeignClient(name = "platform", contextId = "supplierOrgFeignClient")
public interface SupplierOrgFeignClient {

    @PostMapping("/tenant/org/dept/add_supplier_dept")
    R<CreateSupplierDeptResultDTO> createOrgBySupplier(@RequestBody CreateSupplierDeptRequestDTO request);
}
