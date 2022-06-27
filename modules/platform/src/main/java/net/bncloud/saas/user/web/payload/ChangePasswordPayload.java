package net.bncloud.saas.user.web.payload;

import lombok.Getter;
import lombok.Setter;
import net.bncloud.saas.user.service.command.UserRegisterCommand;

@Getter
@Setter
public class ChangePasswordPayload {

    private String oldPassword;
    private String password;
    private String passwordSecond;



}
