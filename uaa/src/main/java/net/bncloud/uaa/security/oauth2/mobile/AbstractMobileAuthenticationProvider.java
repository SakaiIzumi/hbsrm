package net.bncloud.uaa.security.oauth2.mobile;

import net.bncloud.common.constants.LoginTarget;
import net.bncloud.common.exception.BizException;
import net.bncloud.uaa.security.AuthResultCode;
import net.bncloud.uaa.security.oauth2.AbstractAuthenticationProvider;

public abstract class AbstractMobileAuthenticationProvider extends AbstractAuthenticationProvider {

    protected MobilePrincipal toMobilePrincipal(Object principal) {
        MobilePrincipal mobilePrincipal = null;
        if (principal instanceof MobilePrincipal) {
            mobilePrincipal = (MobilePrincipal) principal;
        }
        if (mobilePrincipal == null && principal instanceof String) {
            mobilePrincipal = new MobilePrincipal((String) principal, null);
        }
        if (mobilePrincipal == null) {
            throw new BizException(AuthResultCode.LOGIN_ERROR);
        }
        return mobilePrincipal;
    }

}
