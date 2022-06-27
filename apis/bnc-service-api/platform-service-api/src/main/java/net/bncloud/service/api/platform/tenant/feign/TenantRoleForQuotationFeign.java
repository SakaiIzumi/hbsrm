package net.bncloud.service.api.platform.tenant.feign;

import net.bncloud.common.api.R;
import net.bncloud.security.client.AuthorizedFeignClient;
import net.bncloud.service.api.platform.sys.feign.fallback.CfgParamFallbackFactory;
import net.bncloud.service.api.platform.tenant.feign.fallback.TenantRoleForQuotationFallbackFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * @author liyh
 * @version 1.0.0
 * @description 获取采购方员工访问询价单的权限接口
 * @since 2022/4/12
 */
@AuthorizedFeignClient(name = "platform", contextId = "tenantRoleForQuotationFeign", fallbackFactory = TenantRoleForQuotationFallbackFactory.class, decode404 = true)
public interface TenantRoleForQuotationFeign {

    @PostMapping("/tenant/org/role/quotation/getRoleForQuotation")
    R<String> getRoleForQuotation(@RequestParam Long userId);

}
