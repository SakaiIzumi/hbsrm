package net.bncloud.uaa.security.oauth2;

import net.bncloud.common.constants.LoginTarget;

public abstract class LoginPrincipal {
    protected LoginClient loginClient;

    public LoginPrincipal() {
    }


    public LoginClient getLoginClient() {
        return loginClient;
    }

    public void setLoginClient(LoginClient loginClient) {
        this.loginClient = loginClient;
    }

    public abstract String toLogin();

    public static class DefaultEmptyLoginPrincipal extends LoginPrincipal {

        public DefaultEmptyLoginPrincipal() {
            super();
        }

        @Override
        public String toLogin() {
            return null;
        }
    }

    public static LoginPrincipal empty() {
        return new DefaultEmptyLoginPrincipal();
    }
}
