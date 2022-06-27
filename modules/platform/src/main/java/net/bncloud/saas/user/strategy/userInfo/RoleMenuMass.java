package net.bncloud.saas.user.strategy.userInfo;

import com.google.common.collect.Lists;
import lombok.Data;
import net.bncloud.saas.authorize.domain.Menu;
import net.bncloud.saas.authorize.domain.Role;

import java.io.Serializable;
import java.util.List;

@Data
public class RoleMenuMass implements Serializable {

    private List<Menu> menuList = Lists.newArrayList();
    private List<Role> roleList = Lists.newArrayList();


    public static RoleMenuMass create() {
        RoleMenuMass roleMenuMass = new RoleMenuMass();
        roleMenuMass.setMenuList(Lists.newArrayList());
        roleMenuMass.setRoleList(Lists.newArrayList());
        return roleMenuMass;
    }

}
