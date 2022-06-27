package net.bncloud.uaa.util;

import net.bncloud.logging.context.LoginLogContext;

public class LoginLogContextUtil {

    private static final ThreadLocal<LoginLogContext> LOGIN_LOG_CONTEXT_THREAD_LOCAL = new ThreadLocal<>();

    public static void set(LoginLogContext context) {
        LOGIN_LOG_CONTEXT_THREAD_LOCAL.set(context);
    }

    public static LoginLogContext get() {
        LoginLogContext loginLogContext = LOGIN_LOG_CONTEXT_THREAD_LOCAL.get();
        if (loginLogContext == null) {
            loginLogContext = new LoginLogContext();
            set(loginLogContext);
        }
        return loginLogContext;
    }

    public static void clear() {
        LOGIN_LOG_CONTEXT_THREAD_LOCAL.remove();
    }
}
