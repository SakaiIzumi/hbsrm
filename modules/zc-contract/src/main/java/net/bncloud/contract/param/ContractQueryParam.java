package net.bncloud.contract.param;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import net.bncloud.contract.entity.Contract;
import net.bncloud.base.BaseEntity;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.math.BigDecimal;


/**
 * <p>
 * 合同信息表
 * </p>
 *
 * @author huangtao
 * @since 2021-03-12
 */
@Data
@NoArgsConstructor
@ApiModel
public class ContractQueryParam extends Contract implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 含税金额最小值
     */
    @ApiModelProperty(value = "含税金额最小值")
    private BigDecimal taxIncludedAmountMin;

    /**
     * 含税金额最大值
     */
    @ApiModelProperty(value = "含税金额最大值")
    private BigDecimal taxIncludedAmountMax;

    /**
     * 不含税金额最小值
     */
    @ApiModelProperty(value = "不含税金额最小值")
    private BigDecimal excludingTaxAmountMin;

    /**
     * 不含税金额最大值
     */
    @ApiModelProperty(value = "不含税金额最大值")
    private BigDecimal excludingTaxAmountMax;

    /**
     * 合同类型
     *//*
    @ApiModelProperty(value = "合同类型")
    private String contractTypeId;

    *//**
     * 合同状态
     *//*
    @ApiModelProperty(value = "合同状态")
    private String contractStatusCode;

    *//**
     * 供应商编码
     *//*
    @ApiModelProperty(value = "供应商编码")
    private Long supplierCode;

    *//**
     * 供应商名字
     *//*
    @ApiModelProperty(value = "供应商名字")
    private String supplierName;

    *//**
     * 采购商编码
     *//*
    @ApiModelProperty(value = "采购商编码")
    private Long customerCode;

    *//**
     * 采购商名字
     *//*
    @ApiModelProperty(value = "采购商名字")
    private String customerName;*/

    /**
     * 签订时间,查询开始时间
     */
    private String signedTimeBegin;

    /**
     * 签订时间,查询结束时间
     */
    private String signedTimeEnd;


    /**
     * 搜索内容
     */
    @ApiModelProperty(value = "搜索内容")
    private String searchContent;

    /**
     * tab栏分类,0 所有，1 草稿，2 待答交/待确认，3 被拒，4 待网签 ，5 异常，6 有效，6.1临近到期，
     * 7 无效，8 过期
     */
    @ApiModelProperty(value = "tab栏分类")
    private String tabCategory;


}
