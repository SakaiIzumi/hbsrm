package net.bncloud.uaa.security.oauth2.mobile;

import net.bncloud.common.constants.LoginTarget;
import net.bncloud.uaa.security.oauth2.LoginPrincipal;
import org.apache.commons.lang3.StringUtils;

public class MobilePrincipal extends LoginPrincipal {
    private final String mobile;
    private final String stateCode;

    public MobilePrincipal(String mobile, String stateCode) {
        this.mobile = mobile;

        if (StringUtils.isBlank(stateCode)) {
            stateCode = "86";
        }
        this.stateCode = stateCode;
    }

    public String getMobile() {
        return mobile;
    }

    public String getStateCode() {
        return stateCode;
    }

    public String toLogin() {
        return this.stateCode + "-" + this.mobile;
    }


}
