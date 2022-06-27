package net.bncloud.saas.user.strategy.userInfo;

import lombok.AllArgsConstructor;
import net.bncloud.common.exception.ApiException;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserInfoStrategyContext {

    private final UserInfoStrategyFactory userInfoStrategyFactory;

    public RoleMenuMass getRoleMenuMass(String subjectType) {
        String key = subjectType + "UserInfoStrategy";
        IUserInfoStrategy iUserInfoStrategy = userInfoStrategyFactory.get(key);
        if (iUserInfoStrategy == null) {
            throw new ApiException(404, "找不到合适的策略类");
        }
        return iUserInfoStrategy.getRoleMenuMass();
    }
}
