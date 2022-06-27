package net.bncloud.uaa.web.rest.payload;

import io.swagger.annotations.ApiModelProperty;
import net.bncloud.common.constants.LoginTarget;
import net.bncloud.uaa.security.oauth2.mobile.MobilePrincipal;

public class MobileSmsCodeLogin {
    @ApiModelProperty("手机号，不包含国家代码")
    private String mobile;
    @ApiModelProperty("国家代码")
    private String stateCode = "86";
    @ApiModelProperty("手机验证码")
    private String smsCode;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }


    public MobilePrincipal toPrincipal() {
        return new MobilePrincipal(mobile, stateCode);
    }
}
