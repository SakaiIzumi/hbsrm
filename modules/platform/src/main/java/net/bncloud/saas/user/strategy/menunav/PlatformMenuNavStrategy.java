package net.bncloud.saas.user.strategy.menunav;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import net.bncloud.common.constants.MenuNavType;
import net.bncloud.saas.authorize.domain.MenuNav;
import net.bncloud.saas.authorize.domain.User;
import net.bncloud.saas.authorize.repository.UserRepository;
import net.bncloud.saas.authorize.service.MenuNavService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component("platformMenuNavStrategy")
@AllArgsConstructor
public class PlatformMenuNavStrategy implements IMenuNavStrategy {
    private final MenuNavService menuNavService;
    private final UserRepository userRepository;

    @Override
    public List<MenuNav> getMeuNavs(Long userId) {
        //是否有平台配置管理
        //TODO 暂时通过角色判断是否是管理员
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            List<net.bncloud.saas.authorize.domain.Role> roles = user.getRoles();
            long count = roles.stream().filter(role -> ("平台管理员".equals(role.getName()) || "组织管理员".equals(role.getName()))).count();
            if (count > 0) {
                ArrayList<MenuNavType> menuNavTypes = Lists.newArrayList(MenuNavType.platformSetting);
                return menuNavService.findAllByMenuNavTypeIn(menuNavTypes);
            }
        }

        return new ArrayList<>();
    }

}
