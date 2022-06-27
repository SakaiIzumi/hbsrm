package net.bncloud.saas.tenant.service.command;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.bncloud.saas.tenant.domain.OrgDepartment;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @ClassName EditOrgEmployeeCommand
 * @Description: 编辑员工参数
 * @Author Administrator
 * @Date 2021/4/25
 * @Version V1.0
 **/
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class EditBatchOrgEmployeeCommand {

    @NotEmpty(message = "员工ID集合不能为空")
    @ApiModelProperty(value = "员工ID集合")
    List<Long> employeeIds;

    @NotNull(message = "部门ID不能为空")
    @ApiModelProperty(value = "部门ID")
    private Long deptId;

    @ApiModelProperty(value = "职位")
    private String position;



    public OrgDepartment createDepartment() {
        OrgDepartment orgDepartment = new OrgDepartment();
        orgDepartment.setId(deptId);
        return orgDepartment;
    }


}
