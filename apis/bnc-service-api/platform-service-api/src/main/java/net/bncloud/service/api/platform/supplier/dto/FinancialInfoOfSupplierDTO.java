package net.bncloud.service.api.platform.supplier.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import java.io.Serializable;

/**
 * 供应商的对账信息
 */
@Data
@Getter
@Setter
@Accessors(chain = true)
public class FinancialInfoOfSupplierDTO implements Serializable {
    private static final long serialVersionUID = 3297949418937375611L;

    @ApiModelProperty(value = "银行名称")
    private String bankAccountName;
    @ApiModelProperty(value = "银行账号")
    private String bankAccount;
    @ApiModelProperty(value = "税号号")
    private String taxpayerNo;
}
