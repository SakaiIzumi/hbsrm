package net.bncloud.saas.tenant.web;

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import net.bncloud.common.api.R;
import net.bncloud.common.util.ObjectUtil;
import net.bncloud.saas.tenant.domain.OrgEmployee;
import net.bncloud.saas.tenant.domain.OrgEmployeeMangeScope;
import net.bncloud.saas.tenant.repository.OrgEmployeeManageScopeRepository;
import net.bncloud.saas.tenant.repository.OrgEmployeeRepository;
import net.bncloud.service.api.platform.tenant.enums.ScopeEnum;
import net.bncloud.service.api.platform.tenant.feign.TenantRoleForQuotationFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/tenant/org/role/quotation")
@AllArgsConstructor
public class TenantRoleForQuotationApi implements TenantRoleForQuotationFeign {

    @Autowired
    private OrgEmployeeRepository orgEmployeeRepository;

    @Autowired
    private OrgEmployeeManageScopeRepository orgEmployeeManageScopeRepository;

    @Override
    @PostMapping("/getRoleForQuotation")
    public R<String> getRoleForQuotation(Long userId) {
        Optional<OrgEmployee> byUserId = orgEmployeeRepository.findByUserId(userId);
        if(byUserId.isPresent()){
            OrgEmployee orgEmployee = byUserId.get();
            OrgEmployeeMangeScope scope = orgEmployeeManageScopeRepository.findByEmployeeId(orgEmployee.getId());
            if(ObjectUtil.isNotEmpty(scope)&& StrUtil.isNotEmpty(scope.getScope()) ){
                return R.data(scope.getScope());
            }else{
                //没有查询到默认查询本人
                return R.data(ScopeEnum.SELF.getCode());
            }

        }
        return R.fail("获取当前用户询价单权限异常");
    }
}
