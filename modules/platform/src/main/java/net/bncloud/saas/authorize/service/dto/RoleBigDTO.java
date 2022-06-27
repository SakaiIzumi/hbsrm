package net.bncloud.saas.authorize.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.bncloud.convert.base.BaseDTO;
import net.bncloud.saas.authorize.domain.User;

import java.util.List;

@Getter
@Setter
public class RoleBigDTO extends BaseDTO {
    private static final long serialVersionUID = 1616568798867848290L;
    private Long id;
    @ApiModelProperty("角色名称")
    private String name;

    @ApiModelProperty(value = "描述")
    private String description;

    private boolean enabled;

    private RoleGroupDTO group;

    private List<MenuDTO> menus;

    private List<User> users;
}
