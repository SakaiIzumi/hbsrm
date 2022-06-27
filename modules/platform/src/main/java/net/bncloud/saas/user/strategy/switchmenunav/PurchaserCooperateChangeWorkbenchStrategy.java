package net.bncloud.saas.user.strategy.switchmenunav;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import net.bncloud.api.feign.saas.user.SubjectType;
import net.bncloud.common.constants.MenuNavType;
import net.bncloud.common.security.*;
import net.bncloud.saas.authorize.domain.User;
import net.bncloud.saas.authorize.repository.UserRepository;
import net.bncloud.saas.tenant.domain.OrgEmployee;
import net.bncloud.saas.tenant.repository.OrgEmployeeRepository;
import net.bncloud.saas.user.domain.UserInfo;
import net.bncloud.saas.user.service.UserInfoService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component("purchaserCooperateChangeWorkbenchStrategy")
@AllArgsConstructor
public class PurchaserCooperateChangeWorkbenchStrategy implements IChangeWorkbenchStrategy {
    private final UserRepository userRepository;
    private final UserInfoService userInfoService;
    private final OrgEmployeeRepository orgEmployeeRepository;
    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public void changeWorkbench() {
        LoginInfo loginInfo = SecurityUtils.getLoginInfoOrThrow();
        UserInfo userInfo = userInfoService.getUserById(loginInfo.getId());
        Set<Role> roleSet = Sets.newHashSet();
        loginInfo.setCurrentSubjectType(SubjectType.org.name());
        Org currentOrg = loginInfo.getCurrentOrg();
        if (currentOrg != null) {
            Optional<OrgEmployee> orgEmployeeOptional = orgEmployeeRepository.findByUser_UserIdAndOrg_Id(loginInfo.getId(), currentOrg.getId());
            orgEmployeeOptional.ifPresent(orgEmployee -> {
                Set<Role> collect = orgEmployee.getRoles().stream().map(r -> {
                    Role role = new Role();
                    role.setId(r.getId());
                    role.setName(r.getName());
                    return role;
                }).collect(Collectors.toSet());
                roleSet.addAll(collect);
            });
        } else {
            //加载没创建组织的组织管理员的平台角色权限
            Optional<User> userOptional = userRepository.findById(userInfo.getId());
            userOptional.ifPresent(user -> {
                Set<Role> collect = user.getRoles().stream().map(r -> {
                    Role role = new Role();
                    role.setId(r.getId());
                    role.setName(r.getName());
                    return role;
                }).collect(Collectors.toSet());
                roleSet.addAll(collect);
            });
        }
        loginInfo.setRoles(roleSet);
        loginInfo.setCurrentMenuNav(MenuNavType.purchaserCooperate.name());
        userInfo.getCurrentInfo().setCurrentMenuNavType(MenuNavType.purchaserCooperate.name());
        userInfoService.saveUser(userInfo);
        cacheLoginInfo(loginInfo);
    }

    private void cacheLoginInfo(LoginInfo loginInfo) {
        stringRedisTemplate.opsForValue().set(LoginInfoContextPersistenceFilter.CACHE_KEY_PREFIX + loginInfo.getId(), JSON.toJSONString(loginInfo));
    }
}
