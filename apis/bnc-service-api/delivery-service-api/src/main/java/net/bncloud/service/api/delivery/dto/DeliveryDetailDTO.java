package net.bncloud.service.api.delivery.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 送货详情 DTO
 * @author Toby
 */
@Data
public class DeliveryDetailDTO implements Serializable {

    /**
     * 送货详情Id
     */
    private Long id;

    /**
     * 送货单ID t_delivery_note.delivery_id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "送货单ID t_delivery_note.delivery_id")
    private Long deliveryId;


    /**
     * 项次顺序递增
     */
    @ApiModelProperty(value = "项次顺序递增")
    private Integer itemNo;

    /**
     * 送货计划单号
     */
    @ApiModelProperty(value = "送货计划单号")
    private Integer planNo;

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
    private String planQuantity;

    /**
     * 实际送货数量 默认带出订单待交数量，可修改
     */
    @ApiModelProperty(value = "实际送货数量 默认带出订单待交数量，可修改")
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
     * 送货单位编码
     */
    @ApiModelProperty(value = "送货单位编码")
    private String deliveryUnitCode;

    /**
     * 送货单位名称
     */
    @ApiModelProperty(value = "送货单位名称")
    private String deliveryUnitName;

    /**
     * 收货数量 ERP签收成功时，同步
     */
    @ApiModelProperty(value = "收货数量 ERP签收成功时，同步")
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


}
