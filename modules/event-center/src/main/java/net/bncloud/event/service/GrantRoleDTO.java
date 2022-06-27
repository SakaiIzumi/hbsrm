package net.bncloud.event.service;

import lombok.Getter;
import lombok.Setter;
import net.bncloud.event.domain.vo.RoleVO;

import java.util.List;

@Getter
@Setter
public class GrantRoleDTO {
    private Long id;
    private List<RoleVO> roles;
}
