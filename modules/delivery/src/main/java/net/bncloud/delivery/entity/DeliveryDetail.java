package net.bncloud.delivery.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;
import net.bncloud.base.BaseEntity;

import java.math.BigDecimal;

/**
 * <p>
 * 送货明细表
 * </p>
 *
 * @author Auto-generator
 * @since 2022-01-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_delivery_detail")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryDetail extends BaseEntity {

    private static final long serialVersionUID = 1L;


    /**
     * 送货单ID t_delivery_note.delivery_id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "送货单ID t_delivery_note.delivery_id")
    private Long deliveryId;

    /**
     * 项次
     */
    @ApiModelProperty(value = "项次")
    private Integer itemNo;

    /**
     * 送货计划编号
     */
    @ApiModelProperty(value = "送货计划编号")
    private String planNo;

    /**
     * 送货计划明细项次来源系统ID
     */
    @ApiModelProperty(value = "送货计划明细项次来源系统ID")
    private String planDetailItemSourceId;


    /**
     * 采购单号
     */
    @ApiModelProperty(value = "采购单号")
    private String purchaseOrderCode;

    /**
     * 产品编码
     */
    @ApiModelProperty(value = "产品编码")
    private String productCode;

    /**
     * 产品名称
     */
    @ApiModelProperty(value = "产品名称")
    private String productName;

    /**
     * 产品规格
     */
    @ApiModelProperty(value = "产品规格")
    private String productSpecs;

    /**
     * 计划数量
     */
    @ApiModelProperty(value = "计划数量")
    private BigDecimal planQuantity;

    /**
     * 实际送货数量
     */
    @ApiModelProperty(value = "实际送货数量")
    private BigDecimal realDeliveryQuantity;

    /**
     * 条码
     */
    @ApiModelProperty(value = "条码")
    private String barCode;

    /**
     * 批次
     */
    @ApiModelProperty(value = "批次")
    private String batchNo;

    /**
     * 计划单位编码
     */
    @ApiModelProperty(value = "计划单位编码")
    private String deliveryUnitCode;

    /**
     * 计划单位名称
     */
    @ApiModelProperty(value = "计划单位名称")
    private String deliveryUnitName;

    /**
     * 收货数量
     */
    @ApiModelProperty(value = "收货数量")
    private BigDecimal receiptQuantity;

    /**
     * 入货仓
     */
    @ApiModelProperty(value = "入货仓")
    private String warehouse;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 计划单位/送货单位
     */
    @ApiModelProperty(value = "计划单位/送货单位")
    private String planUnit;

    /**
     * 物料分类
     */
    @ApiModelProperty(value = "物料分类")
    private String materialClassification;

    /**
     * 币别
     */
    @ApiModelProperty(value = "币别")
    private String currency;
    /**
     * erpId
     */
    @ApiModelProperty(value = "erpId")
    private Long erpId;

    /**
     * 计划明细项次id
     */
    @ApiModelProperty(value = "计划明细项次id")
    private Long deliveryPlanDetailItemId;

    /**
     * billNo
     */
    @ApiModelProperty(value = "billNo")
    private String billNo;

    /**
     * orderType
     */
    @ApiModelProperty(value = "orderType")
    private String orderType;

    /**
     * 产品单价
     */
    @ApiModelProperty(value = "产品单价")
    private BigDecimal productUnitPrice;

    /**
     * 含税单价
     */
    @ApiModelProperty(value = "含税单价")
    private BigDecimal taxUnitPrice;

    /**
     * 剩余数量
     */
    @ApiModelProperty(value = "剩余数量")
    private BigDecimal remainingQuantity;

    /**
     * 订单明细ID
     */
    private Long orderProductDetailId;

    /**
     *mrp送货单明细 0-不是  1-是
     */
    @ApiModelProperty(value = "mrp送货单明细")
    private String mrpStatus;

}
