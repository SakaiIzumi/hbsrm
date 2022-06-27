package net.bncloud.common.security;

import net.bncloud.common.constants.LoginTarget;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * Utility class for Spring Security.
 */
public final class SecurityUtils {

    private SecurityUtils() {
    }

    public static Optional<BncUserDetails> getCurrentUser() {
        return Optional.ofNullable(extractCurrentUser(SecurityContextHolder.getContext().getAuthentication()));
    }


    public static Optional<LoginInfo> getLoginInfo() {
        return LoginInfoContextUtils.get();
    }

    public static LoginInfo getLoginInfoOrThrow() {
        return getLoginInfo().orElseThrow(() -> {
            return new AccessDeniedException("登录信息失效！");
        });
    }

    public static Optional<Company> getCurrentCompany() {
        return getLoginInfo().map(LoginInfo::getCurrentCompany);
    }

    /**
     * 获取当前选择的 组织
     * @return
     */
    public static Optional<Org> getCurrentOrg() {
        return getLoginInfo().map(LoginInfo::getCurrentOrg);
    }

    /**
     * 获取当前登录选择的 供应商
     * @return
     */
    public static Optional<Supplier> getCurrentSupplier() {
        return getLoginInfo().map(LoginInfo::getCurrentSupplier);
    }

    private static BncUserDetails extractCurrentUser(Authentication authentication) {
        if (authentication == null) {
            return null;
        } else if (authentication.getPrincipal() instanceof BncUserDetails) {
            return (BncUserDetails) authentication.getPrincipal();
        }
        return null;
    }
}
