package net.bncloud.saas.user.strategy.userInfo;

import lombok.AllArgsConstructor;
import net.bncloud.common.security.LoginInfo;
import net.bncloud.common.security.SecurityUtils;
import net.bncloud.saas.authorize.domain.Menu;
import net.bncloud.saas.authorize.domain.Role;
import net.bncloud.saas.authorize.domain.User;
import net.bncloud.saas.authorize.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component("platformUserInfoStrategy")
@AllArgsConstructor
public class PlatformUserInfoStrategy implements IUserInfoStrategy {
    private final UserRepository userRepository;

    @Override
    public RoleMenuMass getRoleMenuMass() {
        RoleMenuMass roleMenuMass = RoleMenuMass.create();
        LoginInfo loginInfo = SecurityUtils.getLoginInfoOrThrow();
        //加载 组织成员菜单信息
        Optional<User> userOptional = userRepository.findById(loginInfo.getId());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            List<Role> roles = user.getRoles();
            roleMenuMass.getRoleList().addAll(roles);
            user.getRoles().forEach(r -> {
                roleMenuMass.getMenuList().addAll(r.getMenus());
            });
        }
        List<Menu> menuList = roleMenuMass.getMenuList().stream().filter(menu -> !menu.isHidden()).distinct().collect(Collectors.toList());
        roleMenuMass.setMenuList(menuList);
        return roleMenuMass;
    }
}
