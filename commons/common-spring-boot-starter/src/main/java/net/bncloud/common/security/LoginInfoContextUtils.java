package net.bncloud.common.security;

import java.util.Optional;

public class LoginInfoContextUtils {
    private static final ThreadLocal<LoginInfo> LOGIN_INFO_THREAD_LOCAL = new ThreadLocal<>();

    public static Optional<LoginInfo> get() {
        return Optional.ofNullable(LOGIN_INFO_THREAD_LOCAL.get());
    }
    public static void set(LoginInfo userInfo) {
        LOGIN_INFO_THREAD_LOCAL.set(userInfo);
    }

    public static void setIfAbsent(LoginInfo loginInfo) {
        if (!get().isPresent()) {
            set(loginInfo);
        }
    }

    public static void clear() {
        LOGIN_INFO_THREAD_LOCAL.remove();
    }
}
