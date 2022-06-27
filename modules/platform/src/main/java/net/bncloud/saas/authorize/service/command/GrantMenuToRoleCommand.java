package net.bncloud.saas.authorize.service.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class GrantMenuToRoleCommand {
    private Long roleId;
    private List<Long> menuIds;

    public static GrantMenuToRoleCommand of(Long roleId, List<Long> menuIds) {
        return new GrantMenuToRoleCommand(roleId, menuIds);
    }
}
