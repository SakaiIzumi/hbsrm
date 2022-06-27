package net.bncloud.service.api.platform.purchaser.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class OrgIdDTO implements Serializable {
    @ApiModelProperty(value = "组织id")
    private Long orgId;
    @ApiModelProperty(value = "采购编码")
    private String purchaseCode;
    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;
    @ApiModelProperty(value = "采购名称")
    private String purchaseName;
    @ApiModelProperty(value = "供应商名称")
    private String supplierame;


}
