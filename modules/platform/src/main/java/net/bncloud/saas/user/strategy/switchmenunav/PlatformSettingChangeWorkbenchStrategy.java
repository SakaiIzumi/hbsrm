package net.bncloud.saas.user.strategy.switchmenunav;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import net.bncloud.api.feign.saas.user.SubjectType;
import net.bncloud.common.constants.MenuNavType;
import net.bncloud.common.security.LoginInfo;
import net.bncloud.common.security.LoginInfoContextPersistenceFilter;
import net.bncloud.common.security.Role;
import net.bncloud.common.security.SecurityUtils;
import net.bncloud.saas.authorize.domain.User;
import net.bncloud.saas.authorize.repository.UserRepository;
import net.bncloud.saas.user.domain.UserInfo;
import net.bncloud.saas.user.service.UserInfoService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component("platformSettingChangeWorkbenchStrategy")
@AllArgsConstructor
public class PlatformSettingChangeWorkbenchStrategy implements IChangeWorkbenchStrategy {
    private final UserInfoService userInfoService;
    private final UserRepository userRepository;
    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public void changeWorkbench() {
        LoginInfo loginInfo = SecurityUtils.getLoginInfoOrThrow();
        UserInfo userInfo = userInfoService.getUserById(loginInfo.getId());
        Set<Role> roleSet = Sets.newHashSet();
        Optional<User> userOptional = userRepository.findById(loginInfo.getId());
        userOptional.ifPresent(user -> {
            Set<Role> collect = user.getRoles().stream().map(r -> {
                Role role = new Role();
                role.setId(r.getId());
                role.setName(r.getName());
                return role;
            }).collect(Collectors.toSet());
            roleSet.addAll(collect);
        });
        loginInfo.setCurrentSubjectType(SubjectType.platform.name());
        loginInfo.setRoles(roleSet);
        loginInfo.setCurrentMenuNav(MenuNavType.platformSetting.name());
        userInfo.getCurrentInfo().setCurrentMenuNavType(MenuNavType.platformSetting.name());
        userInfoService.saveUser(userInfo);
        cacheLoginInfo(loginInfo);
    }

    private void cacheLoginInfo(LoginInfo loginInfo) {
        stringRedisTemplate.opsForValue().set(LoginInfoContextPersistenceFilter.CACHE_KEY_PREFIX + loginInfo.getId(), JSON.toJSONString(loginInfo));
    }
}
