package net.bncloud.saas.tenant.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class OrgEmpDTO implements Serializable {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "userId")
    private Long userId;

    @ApiModelProperty(value = "帐号")
    private String code;

    @ApiModelProperty(value = "名字")
    private String name;

    @ApiModelProperty(value = "手机")
    private String mobile;

    @ApiModelProperty(value = "组织成员Id")
    private Long memberId;

    @ApiModelProperty(value = "组织Id")
    private Long orgId;

    @ApiModelProperty(value = "组织名称")
    private String orgName;

    @ApiModelProperty(value = "是否启用")
    private Boolean enabled;
}
