package net.bncloud.oem.domain.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;
import net.bncloud.base.BaseEntity;
import net.bncloud.common.util.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;


/**
 * @author ddh
 * @description
 * @since 2022/4/24
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value = "t_oem_purchase_order_material")
@Accessors(chain = true)
@Data
public class PurchaseOrderMaterial extends BaseEntity {


    private static final long serialVersionUID = 1140465048876944383L;
    /**
     * 采购订单id
     */
    @ApiModelProperty(value = "采购订单id")
    private Long purchaseOrderId;

    /**
     * 物料编码
     */
    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    /**
     * 物料名称
     */
    @ApiModelProperty(value = "物料名称")
    private String materialName;

    /**
     * 条码
     */
    private String barCode;

    /**
     * 交货日期
     */
    @ApiModelProperty(value = "交货日期")
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATE)
    @JsonFormat(pattern = DateUtil.PATTERN_DATE)
    private LocalDateTime deliveryDate;

    /**
     * 交货方式
     */
    @ApiModelProperty(value = "交货方式")
    private String deliveryType;

    /**
     * 答交状态
     */
    @ApiModelProperty(value = "答交状态")
    private Integer answerStatus;

    /**
     * 采购数量(订单数量)
     */
    @ApiModelProperty(value = "订单数量")
    private Long purchaseQuantity;
    /**
     * 剩余数量
     */
    private Long remainingQuantity;

    /**
     * 收货次数
     */
    private Long receivingTimes;

    /**
     * 已收货数量
     */
    private Long receivedQuantity;


    /**
     * 收货状态:
     * 1待收货：已收货数量=0
     * 2部分收货：0 < 已收货数量 < 订单数量
     * 3已收货：已收货数量 ≥ 订单数量
     */
    @ApiModelProperty(value = "收货状态:1待收货，2部分收货，3已收货")
    @JSONField(serialize = false)
    @JsonIgnore
    private String takeOverStatus;


    /**
     * ERPID
     */
    private String sourceErpId;

    /**
     * 货主类型
     */
    //private String ownerType;

    /**
     * 入库仓
     */
    private String warehouse;

    /**
     * 含税单价
     */
    @ApiModelProperty(value="含税单价")
    private String taxPrice;




}