package net.bncloud.delivery.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import net.bncloud.base.BaseEntity;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import net.bncloud.common.util.DateUtil;
import net.bncloud.common.web.jackson.AmountSerializer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Date;

/**
 * <p>
 * 送货单信息表
 * </p>
 *
 * @author huangtao
 * @since 2021-03-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_delivery_note")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryNote extends BaseEntity  {

    private static final long serialVersionUID = 1L;

    /**
     * 客户编码
     */
    @NotBlank(message = "客户编码不能为空")
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
    @NotBlank(message = "供应商编码不能为空")
    @ApiModelProperty(value = "供应商编码")
    private String supplierCode ;

    /**
     * 供应商名称
     * */
    @NotBlank(message = "供应商名称不能为空")
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
     * 入库单号
     */
    @ApiModelProperty(value = "入库单号")
    private String receiptNo;
    /**
     * 签收时间
     */
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
    @JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
    @ApiModelProperty(value = "签收时间")
    private Date signingTime;

    /**
     * 签收时间
     */
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
    @JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
    @ApiModelProperty(value = "ERP签收时间")
    private Date erpSigningTime;

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
     * 结算池，入池状态，N未入池，Y已入池。
     */
    private String settlementPoolSyncStatus;

    /**
     * 组织id
     */
    @ApiModelProperty(value = "组织id")
    private Long orgId;

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
     * 收料通知单号
     *
     *
     * warning:用此字段接受参数可能不生效
     *
     */
    @ApiModelProperty(value = "收料通知单号")
    private String fNumber;

    /**
     * 单据编号(以这个为主，关联计划表的billNo字段)
     */
    @ApiModelProperty(value = "单据编号")
    private String billNo;

    /**
     * orderType
     */
    @ApiModelProperty(value = "单据编号")
    private String orderType;

    /**
     * 送货明细是否有附件：
     * 0表示改送货单下的明细都没有附件
     * 1表示部分有，部分没有
     * 2表示都有附件
     * */
    private Integer haveAttachment;

    /**
     * 送货单送货类型 deliveryPlan/order  按送货计划 /按订单送货
     */
    private String deliveryType;

    /**
     *mrp送货单明细 0-不是  1-是
     */
    @ApiModelProperty(value = "mrp送货单明细")
    private String mrpStatus;



}
