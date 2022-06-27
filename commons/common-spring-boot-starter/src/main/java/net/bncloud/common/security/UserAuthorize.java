package net.bncloud.common.security;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class UserAuthorize implements Serializable {
	
	private static final long serialVersionUID = -3344348679832147058L;
	private final Set<Role> roles = new HashSet<>();
	private final Set<GrantedMenu> menus = new HashSet<>();
	private final Set<RoleGroupPO> roleGroup = new HashSet<>();
	
	public UserAuthorize() {
	}
	
	public UserAuthorize(Set<Role> roles, Set<Privilege> privileges, Set<GrantedMenu> menus, Set<RoleGroupPO> roleGroup) {
		this.roles.addAll(roles);
		this.menus.addAll(menus);
		this.roleGroup.addAll(roleGroup);
	}
	
	public Set<Role> getRoles() {
		return roles;
	}
	
	public Set<GrantedMenu> getMenus() {
		return menus;
	}
	
	public void addRole(Long roleId, String roleName) {
		this.roles.add(new Role(roleId, roleName));
	}
	
	public void addMenu(GrantedMenu menu) {
		this.menus.add(menu);
	}
    
    public Set<RoleGroupPO> getRoleGroup() {
        return roleGroup;
    }
    
    public void addRoleGroup(RoleGroupPO getRoleGroup) {
        this.roleGroup.add(getRoleGroup);
    }
    
}
