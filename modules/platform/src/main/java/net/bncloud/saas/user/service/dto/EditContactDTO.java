package net.bncloud.saas.user.service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditContactDTO {

    private String email;
    /**
     * 微信号
     */
    private String weChatCode;

    /**
     * QQ号
     */
    private String qqCode;
}
