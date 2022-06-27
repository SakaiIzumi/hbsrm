package net.bncloud.saas.user.service.command;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.bncloud.saas.user.domain.AccountStatus;
import net.bncloud.saas.user.domain.UserInfo;
import net.bncloud.saas.user.domain.UserPassword;
import net.bncloud.saas.user.domain.UserSourceType;
import org.apache.commons.lang3.StringUtils;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class UserRegisterCommand {

    private String mobile;
    private String stateCode;
    private String code;
    private String password;
    private String email;
    private String name;

    public static UserRegisterCommand of(String mobile, String stateCode, String code, String password, String email, String name) {
        if (StringUtils.isBlank(stateCode)) {
            stateCode = "86";
        }
        return new UserRegisterCommand(mobile, stateCode, code, password, email, name);
    }

    public UserInfo createUserForRegister() {
        UserInfo u = new UserInfo();
        u.setMobile(mobile);
        u.setStateCode(stateCode);
        u.setEmail(email);
        u.setName(name);
        u.setSourceType(UserSourceType.REGISTER);
        u.setStatus(AccountStatus.defaultStatus());
        u.setPassword(UserPassword.init(password));
        return u;
    }
}
