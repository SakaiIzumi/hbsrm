package net.bncloud.saas.tenant.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

@Data
public class RemoveOrgMemberVO implements Serializable {

    @ApiModelProperty(value = "成员id")
    @NotBlank(message = "成员不能为空")
    private List<Long> userIds;

    @ApiModelProperty(value = "组织id")
    @NotBlank(message = "组织不能为空")
    private Long orgId;


}
