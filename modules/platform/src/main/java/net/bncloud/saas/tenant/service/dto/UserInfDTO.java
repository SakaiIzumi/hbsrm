package net.bncloud.saas.tenant.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 关联信息 用户详情
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInfDTO {

    @JsonProperty("id")
    private Long userId;

    @JsonProperty("code")
    private String userCode;

    @JsonProperty("name")
    private String userName;

    private String avatar;

    private String mobile;

    private String wechatCode;

    private String qqCode;

    private String dingTalkCode;

    private String email;

}

