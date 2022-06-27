package net.bncloud.saas.supplier.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SupplierStaffDTO implements Serializable {

    @ApiModelProperty(value = "id")
    private Long id;


    @ApiModelProperty(value = "帐号")
    private String code;

    @ApiModelProperty(value = "名字")
    private String name;

    @ApiModelProperty(value = "手机")
    private String mobile;

    @ApiModelProperty(value = "供应商Id")
    private Long supplierId;

    @ApiModelProperty(value = "供应商名")
    private String supplierName;

    @ApiModelProperty(value = "用户状态")
    private Boolean enabled;

    private String position;

    private String jobNo;


    private List<Long> roleIds;
}
