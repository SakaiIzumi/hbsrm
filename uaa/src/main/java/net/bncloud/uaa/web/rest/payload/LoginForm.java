package net.bncloud.uaa.web.rest.payload;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.bncloud.uaa.security.oauth2.mobile.MobilePrincipal;

@Data
public class LoginForm {
    @ApiModelProperty("手机或帐号")
    private String identifier;
    @ApiModelProperty("密码")
    private String password;

    public MobilePrincipal toPrincipal() {
        return new MobilePrincipal(identifier, null);
    }
}
