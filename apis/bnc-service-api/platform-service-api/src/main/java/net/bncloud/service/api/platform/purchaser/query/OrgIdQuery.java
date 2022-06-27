package net.bncloud.service.api.platform.purchaser.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Accessors(chain = true)
@Data
public class OrgIdQuery implements Serializable {
    private static final long serialVersionUID = 4445567678003968003L;
    @ApiModelProperty(value = "采购编码")
    private String purchaseCode;
    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;
}
