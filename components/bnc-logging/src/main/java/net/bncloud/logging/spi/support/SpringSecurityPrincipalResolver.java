package net.bncloud.logging.spi.support;

import net.bncloud.common.security.LoginInfo;
import net.bncloud.common.security.SecurityUtils;
import net.bncloud.logging.spi.Principal;
import net.bncloud.logging.spi.PrincipalResolver;
import org.aspectj.lang.JoinPoint;

public class SpringSecurityPrincipalResolver implements PrincipalResolver {

    @Override
    public Principal resolveFrom(JoinPoint joinPoint, Object retVal) {
        return getPrincipal();
    }

    private Principal getPrincipal() {
        final LoginInfo loginInfo = SecurityUtils.getLoginInfo().orElse(null);
        if (loginInfo == null) {
            return Principal.LOG_UNKNOWN;
        }
        Principal principal = new Principal();
        principal.setUserId(loginInfo.getId());
        principal.setName(loginInfo.getName());
        principal.setLogin(loginInfo.getMobile());
        principal.setPlatform(loginInfo.getPlatform());
        return principal;
    }


    @Override
    public Principal resolveFrom(JoinPoint joinPoint, Exception exception) {
        return getPrincipal();
    }

    @Override
    public Principal resolve() {
        return getPrincipal();
    }
}
