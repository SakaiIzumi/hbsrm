package net.bncloud.service.api.platform.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoDTO {

    private Long id;
    private String code;
    private String name;
    private String nickname;
    private String mobile;
    private String email;

    private Password password;
    private AccountStatus accountStatus;

}
