package net.bncloud.saas.user.service.command;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.bncloud.saas.user.domain.AccountStatus;
import net.bncloud.saas.user.domain.UserInfo;
import net.bncloud.saas.user.domain.UserPassword;
import net.bncloud.saas.user.domain.UserSourceType;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class UserCreateCommand {

    private String name;
    private String mobile;
    private String stateCode;
    private String password;
    private boolean initPassword = false;

    public static UserCreateCommand of(String name, String mobile, String stateCode, String password) {
        return new UserCreateCommand(name, mobile, stateCode, password, false);
    }

    public static UserCreateCommand of(String name, String mobile, String stateCode, boolean initPassword) {
        return new UserCreateCommand(name, mobile, stateCode, null, initPassword);
    }

    public UserInfo createInactiveUser() {
        final UserInfo user = createUser();
        user.setStatus(AccountStatus.inactive());
        return user;
    }

    private UserInfo createUser() {
        UserInfo user = new UserInfo();
        user.setMobile(mobile);
        user.setStateCode(stateCode);
        user.setName(name);
        user.setSourceType(UserSourceType.CREATE);
        user.setStatus(AccountStatus.inactive());
        if (StringUtils.isNotBlank(password)) {
            user.setPassword(UserPassword.init(password));
        } else {
            if (initPassword) {
                String initPassword = RandomStringUtils.randomAlphanumeric(10);
                user.setPassword(UserPassword.plain(initPassword));
            }
        }
        return user;
    }
    public UserInfo createActiveUser() {
        final UserInfo user = createUser();
        user.setStatus(AccountStatus.defaultStatus());
        return user;
    }
}
