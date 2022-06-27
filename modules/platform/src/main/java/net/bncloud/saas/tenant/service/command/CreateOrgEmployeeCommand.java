package net.bncloud.saas.tenant.service.command;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.bncloud.common.util.CollectionUtil;
import net.bncloud.saas.authorize.domain.Role;
import net.bncloud.saas.tenant.domain.OrgDepartment;
import net.bncloud.saas.tenant.domain.OrgEmployee;
import net.bncloud.saas.tenant.domain.vo.UserId;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName EmployeeCommand
 * @Description: 新增员工请求参数
 * @Author Administrator
 * @Date 2021/4/25
 * @Version V1.0
 **/
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class CreateOrgEmployeeCommand {

    @NotBlank(message = "姓名不能为空")
    @ApiModelProperty(value = "员工姓名")
    private String name;

    @NotBlank(message = "手机不能为空")
    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "角色ID")
    private List<Long> roleIds;

    @NotNull(message = "账号状态不能为空")
    @ApiModelProperty(value = "启用状态")
    private Boolean enabled;

    public OrgEmployee create(UserId user, String createdByName) {
        OrgEmployee orgEmployee = new OrgEmployee();
        orgEmployee.setName(name);
        orgEmployee.setMobile(mobile);
        orgEmployee.setCreatedByName(createdByName);
        orgEmployee.setUser(user);
        orgEmployee.setEnabled(enabled);
        return orgEmployee;
    }

}
