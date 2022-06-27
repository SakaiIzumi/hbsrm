package net.bncloud.saas.authorize.service.command;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GrantRoleToUsers {
    private Long roleId;
    private List<Long> userIds;
}
