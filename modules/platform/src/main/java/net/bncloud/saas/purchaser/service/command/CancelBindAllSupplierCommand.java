package net.bncloud.saas.purchaser.service.command;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CancelBindAllSupplierCommand {

    @ApiModelProperty(value = "采购方id")
    @NotNull(message = "采购方id不能为空")
    private Long id;

    @ApiModelProperty(value = "组织id")
    @NotNull(message = "组织id不能为空")
    private Long orgId;

}
