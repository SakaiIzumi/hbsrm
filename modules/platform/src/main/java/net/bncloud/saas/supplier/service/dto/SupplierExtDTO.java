package net.bncloud.saas.supplier.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.bncloud.convert.base.BaseDTO;

@Data
public class SupplierExtDTO extends BaseDTO {
    private static final long serialVersionUID = 1270716875115931922L;

    private Long id;

    @ApiModelProperty(value = "供应商id")
    private Long supplierId;

    @ApiModelProperty(value = "智采帐号")
    private String supplierAccount;

    @ApiModelProperty(value = "供应商简称")
    private String supplierNickName;

    @ApiModelProperty(value = "开户银行网点")
    private String bankOutlet;

    @ApiModelProperty(value = "开户行")
    private String bankDeposit;

    @ApiModelProperty(value = "银行账号")
    private String bankAccount;

    @ApiModelProperty(value = "银行账户名称")
    private String bankAccountName;

    @ApiModelProperty(value = "纳税人识别号/纳税登记号")
    private String taxpayerNo;

    @ApiModelProperty(value = "付款条件")
    private String paymentClause;

    @ApiModelProperty(value = "交易币别")
    private String bourseCurrency;

    @ApiModelProperty(value = "交易条件")
    private String termsExchange;

    @ApiModelProperty(value = "分类编码")
    private String classificationCode;

    @ApiModelProperty(value = "采购员Id")
    private Long purchasePersonId;

    @ApiModelProperty(value = "公司id")
    private Long companyId;

    @ApiModelProperty(value = "公司名称")
    private String companyName;

    @ApiModelProperty(value = "公司简称")
    private String companyNickName;

    @ApiModelProperty(value = "部门id")
    private Long deptId;

    @ApiModelProperty(value = "部门名称")
    private String deptName;

    @ApiModelProperty(value = "国家")
    private String country;

    @ApiModelProperty(value = "省份")
    private String province;

    @ApiModelProperty(value = "城市")
    private String city;


}
