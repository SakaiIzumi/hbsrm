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
public class EditOrgEmployeeCommand {

    /**
     * 员工ID
     */
    @NotNull(message = "员工ID不能为空")
    private Long employeeId;


    /**
     * 启用状态
     */
    private Boolean enabled;
}
