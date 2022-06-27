package net.bncloud.service.api.platform.supplier.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SupplierInfoDTO implements Serializable {
    @ApiModelProperty(value = "供应商Id")
    private Long id;
    @ApiModelProperty(value = "供应商名称")
    private String name;
    @ApiModelProperty(value = "合作状态")
    private String relevanceStatus;
}
