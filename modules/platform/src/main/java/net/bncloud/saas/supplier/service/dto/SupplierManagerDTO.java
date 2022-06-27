package net.bncloud.saas.supplier.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SupplierManagerDTO implements Serializable {

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

    @ApiModelProperty(value = "供应商管理员Id")
    private Long managerId;

    @ApiModelProperty(value = "供应商Id")
    private Long supplierId;

    @ApiModelProperty(value = "供应商名")
    private String supplierName;

    @ApiModelProperty(value = "用户状态")
    private Boolean enabled;
}
