package net.bncloud.service.api.platform.supplier.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class OaSupplierAccountDTO implements Serializable {
    @ApiModelProperty(value = "开户行")
    private String bankOutlet;

    @ApiModelProperty(value = "开户银行网点")
    private String bankDeposit;

    @ApiModelProperty(value = "银行账号")
    private String bankAccount;

    @ApiModelProperty(value = "账户名称")
    private String bankAccountName;
}
