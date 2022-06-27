package net.bncloud.service.api.delivery.entity;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author Auto-generator
 * @since 2022-01-17
 */
@Data
@Accessors(chain = true)
public class DeliveryPlanDetailEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 商家编码
     */
    @ApiModelProperty(value = "商家编码")
    private String merchantCode;

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
     * 状态:0草稿，1待确认，2已确认
     */
    @ApiModelProperty(value = "状态:0草稿，1待确认，2已确认")
    private String status;

    /**
     * 最近交货日期
     */
    // @DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
    // @JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
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
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private Long createdBy;


    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createdDate;

    /**
     * 更新人
     */
    @ApiModelProperty(value = "更新人")
    private Long lastModifiedBy;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime lastModifiedDate;


    /**
     * 状态[0:未删除,1:删除]
     */
    @ApiModelProperty(value = "是否已删除")
    private Integer isDeleted;

    /**
     * 来源系统主键ID
     */
    private String sourceId;


    /**
     * 计划明细项次列表
     */
    private List<DeliveryPlanDetailItemEntity> planDetailItemList;

}
