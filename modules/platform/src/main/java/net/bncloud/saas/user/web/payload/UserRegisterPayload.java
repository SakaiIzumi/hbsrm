package net.bncloud.saas.user.web.payload;

import lombok.Getter;
import lombok.Setter;
import net.bncloud.saas.user.service.command.UserRegisterCommand;

@Getter
@Setter
public class UserRegisterPayload {

    private String mobile;
    private String stateCode;
    private String code;
    private String password;
    private String email;
    private String name;

    public UserRegisterCommand toCommand() {
        return UserRegisterCommand.of(this.mobile, this.stateCode, this.code, this.password, this.email, this.name);
    }
}
