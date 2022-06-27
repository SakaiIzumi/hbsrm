package net.bncloud.saas.supplier.service.command;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class BindPurchaserCommand {
    @ApiModelProperty("采购方id")
    private Long id;

    @ApiModelProperty("采购方ids")
    private List<Long> supplierId;
}
