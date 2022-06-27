package net.bncloud.saas.user.strategy.switchsub;

import net.bncloud.common.security.LoginInfo;
import net.bncloud.saas.user.domain.UserCurrent;

public class SwitchCurrentUserInfo {

    private LoginInfo loginInfo;

    private UserCurrent userCurrent;


    public LoginInfo getLoginInfo() {
        return loginInfo;
    }

    public void setLoginInfo(LoginInfo loginInfo) {
        this.loginInfo = loginInfo;
    }

    public UserCurrent getUserCurrent() {
        return userCurrent;
    }

    public void setUserCurrent(UserCurrent userCurrent) {
        this.userCurrent = userCurrent;
    }
}
