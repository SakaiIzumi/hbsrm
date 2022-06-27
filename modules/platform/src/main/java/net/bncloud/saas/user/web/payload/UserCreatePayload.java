package net.bncloud.saas.user.web.payload;

import lombok.Getter;
import lombok.Setter;
import net.bncloud.saas.user.service.command.UserCreateCommand;
@Getter
@Setter
public class UserCreatePayload {
    private String name;
    private String mobile;
    private String stateCode;
    private String password;

    public UserCreateCommand toCommand() {

        return null;
    }
}
