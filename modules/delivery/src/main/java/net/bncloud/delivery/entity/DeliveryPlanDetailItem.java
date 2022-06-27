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
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 送货计划明细批次表
 * </p>
 *
 * @author Auto-generator
 * @since 2022-01-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_delivery_plan_detail_item")
public class DeliveryPlanDetailItem extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 送货计划明细id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "送货计划明细id")
    private Long deliveryPlanDetailId;
    /**
     * 送货单id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "送货单id")
    private Long deliveryNoteId;

    /**
     * 送货单号
     */
    @ApiModelProperty(value = "送货单号")
    private String deliveryNoteNo;

    /**
     * 送货状态
     */
    @ApiModelProperty(value = "送货状态：0草稿，1待签收，2已签收")
    private String deliveryStatus;

    /**
     * 送货日期(预计到货日期)
     */
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATE)
    @JsonFormat(pattern = DateUtil.PATTERN_DATE)
    @ApiModelProperty(value = "送货日期(预计到货日期)")
    private LocalDateTime deliveryDate;

    /**
     * 送货数量
     */
    @ApiModelProperty(value = "送货数量")
    private String deliveryQuantity;


    /**
     * 确认日期（确认到货日期）
     */
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATE)
    @JsonFormat(pattern = DateUtil.PATTERN_DATE)
    @ApiModelProperty(value = "确认日期（确认到货日期）")
    private LocalDateTime confirmDate;

    /**
     * 确认数量
     */
    @ApiModelProperty(value = "确认数量")
    private String confirmQuantity;


    /**
     * 来源系统主键ID
     */
    @ApiModelProperty(value = "来源系统主键ID")
    private String sourceId;


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
     * 剩余数量
     */
    @ApiModelProperty(value = "剩余数量")
    private String remainingQuantity;

    /**
     * 数据来源类型 mrp/purchaseOrder
     */
    private String sourceType;


    /**
     * 建议发货日期
     */
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATE)
    @JsonFormat(pattern = DateUtil.PATTERN_DATE)
    @ApiModelProperty(value = "建议发货日期")
    private LocalDate suggestedDeliveryDate;

    /**
     * 确认建议发货日期
     */
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATE)
    @JsonFormat(pattern = DateUtil.PATTERN_DATE)
    @ApiModelProperty(value = "确认建议发货日期")
    private LocalDate confirmSuggestedDeliveryDate;

    /**
     * 差异原因
     */
    private String differenceReason;

    /**
     * 差异数
     */
    private Long varianceNumber;

    /**
     * 上一版的计划数量
     */
    private BigDecimal previousEditionPlanQuantity;


}
