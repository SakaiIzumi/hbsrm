package net.bncloud.service.api.delivery.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.bncloud.common.util.DateUtil;
import net.bncloud.common.web.jackson.AmountSerializer;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Toby
 */
@Data
public class DeliveryNoteDTO implements Serializable {

    private static final long serialVersionUID =  -1;



    /**
     * 客户编码
     */
    @ApiModelProperty(value = "客户编码")
    private String customerCode;

    /**
     * 客户名称
     */
    @ApiModelProperty(value = "客户名称")
    private String customerName;

    /**
     * 供应商编码
     * */
    @ApiModelProperty(value = "供应商编码")
    private String supplierCode ;

    /**
     * 供应商名称
     * */
    @ApiModelProperty(value = "供应商名称")
    private String supplierName ;

    /**
     * 收货时间
     */
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
    @JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
    @ApiModelProperty(value = "收货时间（废弃）")
    private Date receipt;

    /**
     * 送货日期
     */
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATE)
    @JsonFormat(pattern = DateUtil.PATTERN_DATE)
    @ApiModelProperty(value = "送货日期")
    private Date deliveryDate;

    /**
     * 预计到厂
     */
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
    @JsonFormat(pattern = DateUtil.PATTERN_DATE)
    @ApiModelProperty(value = "预计到厂")
    private Date estimatedTime;

    /**
     * 条形码版本
     */
    @ApiModelProperty(value = "条形码版本")
    private String barcodeVersion;

    /**
     * 送货单号
     */
    @ApiModelProperty(value = "送货单号")
    private String deliveryNo;

    /**
     * 收货部门编码
     */
    @ApiModelProperty(value = "收货部门编码")
    private String receiptDeptCode;

    /**
     * 收货部门名称
     */
    @ApiModelProperty(value = "收货部门名称")
    private String receiptDeptName;

    /**
     * 采购说明
     */
    @ApiModelProperty(value = "采购说明")
    private String purchaseRemark;

    /**
     * 内部单号
     */
    @ApiModelProperty(value = "内部单号")
    private String innerOrderNo;

    /**
     * 送货方式 由字典编码delivery_method定义
     */
    @ApiModelProperty(value = "送货方式 由字典编码delivery_method定义")
    private String deliveryMethod;

    /**
     * 送货车牌
     */
    @ApiModelProperty(value = "送货车牌")
    private String deliveryCarNo;

    /**
     * 自提地址
     */
    @ApiModelProperty(value = "自提地址")
    private String selfMentionAddress;

    /**
     * 送货地址
     */
    @ApiModelProperty(value = "送货地址")
    private String deliveryAddress;

    /**
     * 物流单号
     */
    @ApiModelProperty(value = "物流单号")
    private String shipmentNumber;

    /**
     * 送货备注
     */
    @ApiModelProperty(value = "送货备注")
    private String deliveryRemark;

    /**
     * 运输方式 由字典编码delivery_transport_method定义
     */
    @ApiModelProperty(value = "运输方式 由字典编码delivery_transport_method定义")
    private String transportMethod;

    /**
     * 司机姓名
     */
    @ApiModelProperty(value = "司机姓名")
    private String driverName;

    /**
     * 司机电话
     */
    @ApiModelProperty(value = "司机电话")
    private String driverTelephone;

    /**
     * 收货联系人
     */
    @ApiModelProperty(value = "收货联系人")
    private String receivingContact;

    /**
     * 整单毛重
     */
    @ApiModelProperty(value = "整单毛重")
    private BigDecimal grossWeight;

    /**
     * 包装方式
     */
    @ApiModelProperty(value = "包装方式")
    private String packageMethod;

    /**
     * 包装总数
     */
    @ApiModelProperty(value = "包装总数")
    private BigDecimal packageNum;

    /**
     * 整单净重
     */
    @ApiModelProperty(value = "整单净重")
    private BigDecimal netWeight;

    /**
     * 重量单位 由字典编码weight_unit定义
     */
    @ApiModelProperty(value = "重量单位 由字典编码weight_unit定义")
    private String weightUnitCode;

    /**
     * 明细包装汇总
     */
    @ApiModelProperty(value = "明细包装汇总")
    private String packageTotal;

    /**
     * 实际送货总数量
     */
    @ApiModelProperty(value = "实际送货总数量")
    private BigDecimal realDeliveryNum;

    /**
     * 送货单金额
     */
    @ApiModelProperty(value = "送货单金额")
    @JsonSerialize(using = AmountSerializer.class)
    private BigDecimal deliveryAmount;

    /**
     * 送货状态 由字典delivery_status_code定义
     */
    @ApiModelProperty(value = "送货状态 由字典delivery_status_code定义")
    private String deliveryStatusCode;

    /**
     * 校验状态 由字典delivery_check_status定义
     */
    @ApiModelProperty(value = "校验状态 由字典delivery_check_status定义")
    private String deliveryCheckStatus;

    /**
     * 采购类型 由字典delivery_purchase_type定义
     */
    @ApiModelProperty(value = "采购类型 由字典delivery_purchase_type定义")
    private String deliveryPurchaseType;

    /**
     * 送货类型 由字典delivery_type_code定义
     */
    @ApiModelProperty(value = "送货类型 由字典delivery_type_code定义")
    @NotBlank(message = "送货类型不能为空")
    private String deliveryTypeCode;

    /**
     * 随货送票 1是，2否
     */
    @ApiModelProperty(value = "随货送票 1是，2否")
    private String deliveryTicket;

    /**
     * 送货申请开启状态 N 关闭，Y 开启
     */
    @ApiModelProperty(value = "送货申请开启状态 N 关闭，Y 开启")
    private Boolean deliveryApplication;

    /**
     * 优先校验 Y是，N否
     */
    @ApiModelProperty(value = "优先校验 Y是，N否")
    private String priorityCheck;

    /**
     * 物流状态 由字典logistics_status 定义
     */
    @ApiModelProperty(value = "物流状态 由字典logistics_status 定义")
    private String logisticsStatus;

    /**
     * 到货时间
     */
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
    @JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
    @ApiModelProperty(value = "到货时间")
    private Date arrivalTime;

    /**
     * 签收时间
     */
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
    @JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
    @ApiModelProperty(value = "签收时间")
    private Date signingTime;


    /**
     * ERP签收状态
     */
    private String erpSigningStatus;

    /**
     * 检验结果
     */
    @ApiModelProperty(value = "检验结果")
    private String checkResults;

    /**
     * 签收差异
     */
    @ApiModelProperty(value = "签收差异")
    private String signInDiscrepancy;

    /**
     * 创建人名称
     */
    @ApiModelProperty(value = "创建人名称")
    private String createdByName;



    /**
     * 组织id
     */
    @ApiModelProperty(value = "组织id")
    private Long orgId;

    /**
     * 收料通知单回传的fNumber
     */
    @ApiModelProperty(value = "收料通知单号")
    private String fNumber;
}
