package net.bncloud.saas.user.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRelateDetailDTO implements Serializable {

    private static final long serialVersionUID = -7062940557998229678L;

    @ApiModelProperty(value = "用户id")
    private Long userId;
    @ApiModelProperty(value = "用户名字")
    private String userName;
    @ApiModelProperty(value = "组织/供应商")
    private String orgName; //供应商或组织
    @ApiModelProperty(value = "主体类型")
    private String subjectType;
    @ApiModelProperty(value = "主体id")
    private Long subjectId;
    @ApiModelProperty(value = "手机")
    private String mobile;
    @ApiModelProperty(value = "工号")
    private String jobNo;
    @ApiModelProperty(value = "岗位")
    private String position;
    @ApiModelProperty(value = "启用")
    private boolean enabled;
}
