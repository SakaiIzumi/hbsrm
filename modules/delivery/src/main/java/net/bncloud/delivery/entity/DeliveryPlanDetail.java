package net.bncloud.delivery.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.bncloud.base.BaseEntity;
import net.bncloud.common.util.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author Auto-generator
 * @since 2022-01-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_delivery_plan_detail")

public class DeliveryPlanDetail extends BaseEntity {

    private static final long serialVersionUID = 1L;


    /**
     * 送货计划id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "送货计划id")
    private Long deliveryPlanId;

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
    private String productSpecification;

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
     * 采购单号
     */
    @ApiModelProperty(value = "采购单号")
    private String purchaseOrderNo;

    /**
     * 单据编号
     */
    @ApiModelProperty(value = "单据编号")
    private String billNo;


    /**
     * 商家编码
     */
    @ApiModelProperty(value = "商家编码")
    private String merchantCode;

    /**
     * 送货地址
     */
    @ApiModelProperty(value = "送货地址")
    private String deliveryAddress;

    /**
     * 计划单位
     */
    @ApiModelProperty(value = "计划单位")
    private String planUnit;

    /**
     * 计划数量
     */
    @ApiModelProperty(value = "计划数量")
    private String planQuantity;

    /**
     * 确认数量
     */
    @ApiModelProperty(value = "确认数量")
    private String confirmQuantity;

    /**
     * 状态
     */
    @ApiModelProperty(value = "状态")
    private String status;

    /**
     * 最近交货日期
     */
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATE)
    @JsonFormat(pattern = DateUtil.PATTERN_DATE)
    @ApiModelProperty(value = "最近交货日期")
    private LocalDateTime latestDeliveryDate;

    /**
     * 最近计划数量
     */
    @ApiModelProperty(value = "最近计划数量")
    private String latestPlanQuantity;

    /**
     * 最近确认数量
     */
    @ApiModelProperty(value = "最近确认数量")
    private String latestConfirmQuantity;

    /**
     * 来源系统主键ID
     */
    @ApiModelProperty(value = "来源系统主键ID")
    private String sourceId;

    /**
     * 数据来源类型 mrp/purchaseOrder
     */
    private String sourceType;

    /**
     * 计划明细状态：1待供应商确认、2待采购方确认、3已确认
     */
    private String detailStatus;


    /**
     * MRP计划数量
     */
    private Long mrpPlanQuantity;
    /**
     * 在途数量
     */
    private Long transitQuantity;
    /**
     * 采购方备注
     */
    private String purchaseRemark;
    /**
     * 供应商备注
     */
    private String supplierRemark;
    /**
     * 净需求数
     */
    private Long netDemand;
    /**
     * 差异数
     */
    private Long varianceNumber;
}
