package net.bncloud.event.feign;

import net.bncloud.api.event.PublishEventRequest;
import net.bncloud.common.api.R;
import net.bncloud.event.feign.fallback.SaasClientFallbackFactory;
import net.bncloud.security.client.AuthorizedFeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@AuthorizedFeignClient(name = "platform", contextId = "platformEventClient", fallbackFactory = SaasClientFallbackFactory.class, decode404 = true)
public interface SaasClient {

    @PostMapping("/authorize/role/getUserIdListByRoleIdList")
    R<Set<Long>> getUserIdListByRoleIdList(@RequestBody Set<Long> roleIdList);

    @PostMapping("/user-center/users/getRolesByUserId")
    R<Set<Long>> getRolesByUserId(@RequestBody Set<Long> roleIdList);

    @PostMapping("/user-center/users/getRolesByOrgId")
    R<Map<String,Object>> getRolesByOrgId(@RequestParam("orgId")Long orgId, @RequestParam("roleIdList") Set<Long> roleIdList);

    @PostMapping("/user-center/users/getRolesBySupplierCode")
    R<Map<String,Object>> getRolesBySupplierCode(@RequestParam("supplierCode") String supplierCode, @RequestParam("roleIdList") Set<Long> roleIdList);

    @PostMapping("/user-center/users/getUserInfoBySupplierIdAndUid")
    R<List<Map<String,Object>>> getUserInfoBySupplierIdAndUid(@RequestParam("supplierCode") String supplierCode, @RequestParam("roleIdList") Set<Long> roleIdList);
}
