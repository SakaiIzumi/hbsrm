package net.bncloud.saas.authorize.service.dto;

import net.bncloud.api.feign.saas.user.RoleGroupType;
import net.bncloud.convert.base.BaseDTO;

import java.util.List;

public class RoleGroupTreeDTO extends BaseDTO {
    private static final long serialVersionUID = -3745776575958743681L;
    private Long id;
    private String name;
    private RoleGroupType type;
    private List<RoleSmallDTO> roles;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public List<RoleSmallDTO> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleSmallDTO> roles) {
        this.roles = roles;
    }
}
