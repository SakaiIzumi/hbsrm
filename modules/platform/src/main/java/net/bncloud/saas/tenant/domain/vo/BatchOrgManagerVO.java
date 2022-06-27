package net.bncloud.saas.tenant.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

@Data
public class BatchOrgManagerVO implements Serializable {

    @ApiModelProperty(value = "组织id")
    @NotBlank(message = "组织不能为空")
    private List<Long> orgId;

    @ApiModelProperty(value = "用户id")
    @NotBlank(message = "用户不能为空")
    private List<Long> userIds;

}
