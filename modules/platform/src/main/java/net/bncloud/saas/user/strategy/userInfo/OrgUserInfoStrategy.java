package net.bncloud.saas.user.strategy.userInfo;

import lombok.AllArgsConstructor;
import net.bncloud.common.security.LoginInfo;
import net.bncloud.common.security.Org;
import net.bncloud.common.security.SecurityUtils;
import net.bncloud.saas.authorize.domain.Menu;
import net.bncloud.saas.authorize.domain.Role;
import net.bncloud.saas.authorize.domain.User;
import net.bncloud.saas.authorize.repository.UserRepository;
import net.bncloud.saas.tenant.domain.OrgEmployee;
import net.bncloud.saas.tenant.domain.OrgManagerRecord;
import net.bncloud.saas.tenant.repository.OrgEmployeeRepository;
import net.bncloud.saas.tenant.repository.OrgManagerRecordRepository;
import net.bncloud.saas.user.repository.UserInfoRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component("orgUserInfoStrategy")
@AllArgsConstructor
public class OrgUserInfoStrategy implements IUserInfoStrategy {
    private final UserRepository userRepository;
    private final OrgEmployeeRepository orgEmployeeRepository;
    private final OrgManagerRecordRepository orgManagerRecordRepository;

    @Override
    public RoleMenuMass getRoleMenuMass() {
        RoleMenuMass roleMenuMass = RoleMenuMass.create();
        LoginInfo loginInfo = SecurityUtils.getLoginInfoOrThrow();
        //加载 组织成员菜单信息
        Org currentOrg = loginInfo.getCurrentOrg();
        if (currentOrg != null) {
            Optional<OrgEmployee> orgEmployeeOptional = orgEmployeeRepository.findByUser_UserIdAndOrg_Id(loginInfo.getId(), currentOrg.getId());
            if (orgEmployeeOptional.isPresent()) {
                OrgEmployee orgEmployee = orgEmployeeOptional.get();
                List<Role> roles = orgEmployee.getRoles();
                roleMenuMass.getRoleList().addAll(roles);
                orgEmployee.getRoles().forEach(r -> {
                    roleMenuMass.getMenuList().addAll(r.getMenus());
                });
            }
        } else {
            //存在未创建组织的管理员
            Optional<OrgManagerRecord> orgManagerRecordOptional = orgManagerRecordRepository.findByUserId(loginInfo.getId());
            orgManagerRecordOptional.ifPresent(orgManagerRecord -> {
                Optional<User> userOptional = userRepository.findById(orgManagerRecord.getUser().getId());
                userOptional.ifPresent(user -> {
                    List<Role> roles = user.getRoles();
                    roleMenuMass.getRoleList().addAll(roles);
                    roles.forEach(r -> {
                        roleMenuMass.getMenuList().addAll(r.getMenus());
                    });
                });
            });
        }
        List<Menu> menuList = roleMenuMass.getMenuList().stream().filter(menu -> !menu.isHidden()).distinct().collect(Collectors.toList());
        roleMenuMass.setMenuList(menuList);
        return roleMenuMass;
    }
}
