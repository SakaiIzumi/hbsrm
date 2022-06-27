package net.bncloud.delivery.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import net.bncloud.common.util.DateUtil;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 送货计划明细批次表
 * </p>
 *
 * @author Auto-generator
 * @since 2022-01-17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryPlanDetailItemVo  implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 送货计划明细项次的主键ID
     */
    private Long id;

    /**
     * 商家编码
     */
    @ApiModelProperty(value = "商家编码")
    private String merchantCode;
    /**
     * 计划明细项次id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "计划明细项次id")
    private Long deliveryPlanDetailItemId;

    /**
     * 送货计划id
     */
    @ApiModelProperty(value = "送货计划id")
    private Long deliveryPlanId;
    /**
     * 送货计划id/planDetailItemSourceId
     */
    @ApiModelProperty(value = "送货计划id/planDetailItemSourceId")
    private String planDetailItemSourceId;
    /**
     * 计划数量/供应商交货数量
     */
    @ApiModelProperty(value = "计划数量/供应商交货数量")
    private String planQuantity;

    /**
     * 产品编码
     */
    @ApiModelProperty(value = "产品编码")
    private String productCode;

    /**
     * 送货单id
     */
    @ApiModelProperty(value = "送货单id")
    private String deliveryNoteId;

    /**
     *订单类型 ERP获取类型
     */
    @ApiModelProperty(value = "订单类型 ERP获取类型")
    private String orderType;

    /**
     * 送货单号
     */
    @ApiModelProperty(value = "送货单号")
    private String deliveryNoteNo;

    /**
     * 送货状态
     */
    @ApiModelProperty(value = "送货状态")
    private String deliveryStatus;

    /**
     * 产品名称
     */
    @ApiModelProperty(value = "产品名称")
    private String productName;

    /**
     * 产品规格
     */
    @ApiModelProperty(value = "产品规格")
    private String productSpecifications;

    /**
     * 产品单价
     */
    @ApiModelProperty(value = "产品单价")
    private BigDecimal productUnitPrice;


    /**
     * 产品含税单价
     */
    @ApiModelProperty(value = "产品含税单价")
    private BigDecimal taxUnitPrice;


    /**
     * 采购编码
     */
    @ApiModelProperty(value = "采购编码")
    private String purchaseCode;
    /**
     * 采购名称
     */
    @ApiModelProperty(value = "采购名称")
    private String purchaseName;

    /**
     * 供应商编码
     */
    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;
    /**
     * 供应商名称
     */
    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    /**
     * 计划编号
     */
    @ApiModelProperty(value = "计划编号")
    private String deliveryPlanNo;

    /**
     * 单据编号
     */
    @ApiModelProperty(value = "单据编号")
    private String billNo;


    /**
     * 送货日期
     */
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATE)
    @JsonFormat(pattern = DateUtil.PATTERN_DATE)
    @ApiModelProperty(value = "送货日期")
    private Date deliveryDate;

    /**
     * 送货数量
     */
    @ApiModelProperty(value = "送货数量")
    private String deliveryQuantity;
    /**
     * 计划单位
     */
    @ApiModelProperty(value = "计划单位")
    private String planUnit;


    /**
     * 结算币种
     */
    @ApiModelProperty(value = "结算币种")
    private String currency;


    /**
     * 物料分类
     */
    @ApiModelProperty(value = "物料分类")
    private String materialClassification;
    /**
     * 入货仓
     */
    @ApiModelProperty(value = "入货仓")
    private String warehousing;

    /**
     * 送货地址
     */
    @ApiModelProperty(value = "送货地址")
    private String deliveryAddress;

    /**
     * 计划送货总数
     */
    @ApiModelProperty(value = "计划送货总数")
    private String totalDeliveryQuantity;

    /**
     * 计划送货总数SourceId
     */
    @ApiModelProperty(value = "计划送货总数")
    private String planSourceId;

    /**
     * 计划送货detail的SourceId
     */
    @ApiModelProperty(value = "计划送货总数")
    private String detailPlanSourceId;

    /**
     * 订单编号
     */
    @ApiModelProperty(value = "订单编号")
    private String purchaseOrderCode;

    /**
     * 订单编号(计划看板详情用)
     */
    @ApiModelProperty(value = "订单编号")
    private String purchaseOrderNo;

    /**
     * 剩余数量
     */
    @ApiModelProperty(value = "剩余数量")
    private String remainingQuantity;



}
