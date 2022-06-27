package net.bncloud.uaa.web.rest.payload;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.bncloud.common.constants.LoginTarget;
import net.bncloud.uaa.security.oauth2.mobile.MobilePrincipal;

@Data
public class MobilePasswordLogin {
    @ApiModelProperty("手机号，不包含国家代码")
    private String mobile;
    @ApiModelProperty("国家代码")
    private String stateCode = "86";
    @ApiModelProperty("密码")
    private String password;


    public MobilePrincipal toPrincipal() {
        return new MobilePrincipal(mobile, stateCode);
    }
}
