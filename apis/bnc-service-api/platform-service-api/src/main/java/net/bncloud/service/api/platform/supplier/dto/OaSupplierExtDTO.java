package net.bncloud.service.api.platform.supplier.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class OaSupplierExtDTO implements Serializable {

    @ApiModelProperty(value = "供应商简称")
    private String supplierNickName;

    @ApiModelProperty(value = "开户行")
    @Deprecated
    private String bankOutlet;

    @ApiModelProperty(value = "开户银行网点")
    @Deprecated
    private String bankDeposit;

    @ApiModelProperty(value = "银行账号")
    @Deprecated
    private String bankAccount;

    @ApiModelProperty(value = "账户名称")
    @Deprecated
    private String bankAccountName;

    @ApiModelProperty(value = "纳税登记号")
    private String taxpayerNo;

    @ApiModelProperty(value = "付款条件")
    private String paymentClause;

    @ApiModelProperty(value = "交易币别")
    private String bourseCurrency;

    @ApiModelProperty(value = "交易条件")
    private String termsExchange;
}
