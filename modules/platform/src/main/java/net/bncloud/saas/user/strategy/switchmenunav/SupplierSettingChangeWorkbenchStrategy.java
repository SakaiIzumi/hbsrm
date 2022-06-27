package net.bncloud.saas.user.strategy.switchmenunav;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import net.bncloud.api.feign.saas.user.SubjectType;
import net.bncloud.common.constants.MenuNavType;
import net.bncloud.common.security.*;
import net.bncloud.saas.authorize.repository.UserRepository;
import net.bncloud.saas.supplier.domain.SupplierStaff;
import net.bncloud.saas.supplier.repository.SupplierStaffRepository;
import net.bncloud.saas.user.domain.UserInfo;
import net.bncloud.saas.user.service.UserInfoService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component("supplierSettingChangeWorkbenchStrategy")
@AllArgsConstructor
public class SupplierSettingChangeWorkbenchStrategy implements IChangeWorkbenchStrategy {
    private final UserInfoService userInfoService;
    private final SupplierStaffRepository supplierStaffRepository;
    private final StringRedisTemplate stringRedisTemplate;
    @Override
    public void changeWorkbench() {
        LoginInfo loginInfo = SecurityUtils.getLoginInfoOrThrow();
        UserInfo userInfo = userInfoService.getUserById(loginInfo.getId());
        Set<Role> roleSet = Sets.newHashSet();
        loginInfo.setCurrentSubjectType(SubjectType.supplier.name());
        Supplier currentSupplier = loginInfo.getCurrentSupplier();
        if (currentSupplier != null) {
            Optional<SupplierStaff> supplierStaff = supplierStaffRepository.findAllByUser_UserIdAndSupplier_Id(loginInfo.getId(), currentSupplier.getSupplierId());
            supplierStaff.ifPresent(staff -> {
                Set<Role> collect = staff.getRoles().stream().map(r -> {
                    Role role = new Role();
                    role.setId(r.getId());
                    role.setName(r.getName());
                    return role;
                }).collect(Collectors.toSet());
                roleSet.addAll(collect);
            });
        }
        loginInfo.setRoles(roleSet);
        loginInfo.setCurrentMenuNav(MenuNavType.supplierSetting.name());
        userInfo.getCurrentInfo().setCurrentMenuNavType(MenuNavType.supplierSetting.name());
        userInfoService.saveUser(userInfo);
        cacheLoginInfo(loginInfo);
    }

    private void cacheLoginInfo(LoginInfo loginInfo) {
        stringRedisTemplate.opsForValue().set(LoginInfoContextPersistenceFilter.CACHE_KEY_PREFIX + loginInfo.getId(), JSON.toJSONString(loginInfo));
    }
}
