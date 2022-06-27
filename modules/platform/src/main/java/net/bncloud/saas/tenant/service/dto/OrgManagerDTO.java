package net.bncloud.saas.tenant.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class OrgManagerDTO implements Serializable {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "帐号")
    private String code;

    @ApiModelProperty(value = "名字")
    private String name;

    @ApiModelProperty(value = "手机")
    private String mobile;

    @ApiModelProperty(value = "管理员类型")
    private String managerType;

    @ApiModelProperty(value = "组织管理员Id")
    private Long managerId;

    @ApiModelProperty(value = "组织Id")
    private Long orgId;

    @ApiModelProperty(value = "组织名称")
    private String orgName;

    @ApiModelProperty(value = "用户状态")
    private Boolean enabled;

    private ManagerOperatePermission permissionButton = new ManagerOperatePermission();
}
