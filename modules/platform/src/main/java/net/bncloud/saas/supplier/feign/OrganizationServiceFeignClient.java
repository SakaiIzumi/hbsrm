package net.bncloud.saas.supplier.feign;

import net.bncloud.api.feign.saas.org.OrgDTO;
import net.bncloud.common.api.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "platform", contextId = "organizationServiceFeignClient")
public interface OrganizationServiceFeignClient {
    @GetMapping("/tenant/org/getById")
    R<OrgDTO> getById(@RequestParam(name = "orgId") Long orgId);
}
