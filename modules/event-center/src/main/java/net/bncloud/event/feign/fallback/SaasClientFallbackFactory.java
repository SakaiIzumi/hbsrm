package net.bncloud.event.feign.fallback;

import feign.hystrix.FallbackFactory;
import net.bncloud.api.event.PublishEventRequest;
import net.bncloud.common.api.R;
import net.bncloud.event.feign.SaasClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class SaasClientFallbackFactory implements FallbackFactory<SaasClient> {
    @Override
    public SaasClient create(Throwable throwable) {
        return new SaasClient() {
            @Override
            public R<Set<Long>> getUserIdListByRoleIdList(Set<Long> roleIdList) {
                return R.fail("用户服务暂时不可用，获取用户信息失败，请稍后再试");
            }

            @Override
            public R<Set<Long>> getRolesByUserId(Set<Long> roleIdList) {
                return R.fail("用户服务暂时不可用，获取用户信息失败，请稍后再试");
            }

            @Override
            public R<Map<String,Object>> getRolesByOrgId(Long orgId, Set<Long> roleIdList) {
                return R.fail("用户服务暂时不可用，获取用户信息失败，请稍后再试");
            }

            @Override
            public R<Map<String,Object>> getRolesBySupplierCode(String supplierCode, Set<Long> roleIdList) {
                return R.fail("用户服务暂时不可用，获取用户信息失败，请稍后再试");
            }

            @Override
            public R<List<Map<String,Object>>> getUserInfoBySupplierIdAndUid(String supplierCode,Set<Long> roleIdList) {
                return R.fail("用户服务暂时不可用，获取用户信息失败，请稍后再试");
            }
        };
    }
}
