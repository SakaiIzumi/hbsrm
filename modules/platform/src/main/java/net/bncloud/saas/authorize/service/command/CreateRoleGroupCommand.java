package net.bncloud.saas.authorize.service.command;

import net.bncloud.api.feign.saas.user.RoleGroupType;

public class CreateRoleGroupCommand {
    private String name;
    
    private RoleGroupType type;
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public RoleGroupType getType() {
        return type;
    }
    
    public void setType(RoleGroupType type) {
        this.type = type;
    }
}
