package net.bncloud.saas.tenant.service.command;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;

/**
 * @ClassName EditOrgEmployeeCommand
 * @Description: 编辑员工请求参数
 * @Author Administrator
 * @Date 2021/4/26
 * @Version V1.0
 **/
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class EditUserInfoCommand {

    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    /**
     * 头像
     */
    private String avatar;

}
