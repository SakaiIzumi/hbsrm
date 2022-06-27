package net.bncloud.service.api.platform.tenant.feign.fallback;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.common.api.R;
import net.bncloud.service.api.platform.tenant.feign.TenantRoleForQuotationFeign;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;

/**
 * @author liyh
 * @version 1.0.0
 * @description 获取采购方员工访问询价单的权限的fallback方法
 * @since 2022/4/12
 */
@Slf4j
@Component
public class TenantRoleForQuotationFallbackFactory implements FallbackFactory<TenantRoleForQuotationFeign>  {
    @Override
    public TenantRoleForQuotationFeign create(Throwable throwable) {
        log.error(" tenantRoleForQuotationFeign open feign ", throwable);
        return new TenantRoleForQuotationFeign() {
            @Override
            public R<String> getRoleForQuotation(Long userId) {
                    throwable.printStackTrace();
                    return R.fail(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"获取当前采购商用户的询价单查询权限异常");
            }
        };

    }



}
