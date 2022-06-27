package net.bncloud.api.feign.delivery;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import net.bncloud.common.util.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Toby
 */
@Data
public class DeliveryNote  implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 客户编码
     */
    private String customerCode;

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 供应商编码
     * */
    private String supplierCode ;

    /**
     * 供应商名称
     * */
    private String supplierName ;

    /**
     * 收货时间
     */
    private Date receipt;

    /**
     * 送货日期
     */
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATE)
    @JsonFormat(pattern = DateUtil.PATTERN_DATE)
    private Date deliveryDate;

    /**
     * 预计到厂
     */
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
    @JsonFormat(pattern = DateUtil.PATTERN_DATE)
    private Date estimatedTime;

    /**
     * 条形码版本
     */
    private String barcodeVersion;

    /**
     * 送货单号
     */
    private String deliveryNo;

    /**
     * 收货部门编码
     */
    private String receiptDeptCode;

    /**
     * 收货部门名称
     */
    private String receiptDeptName;

    /**
     * 采购说明
     */
    private String purchaseRemark;

    /**
     * 内部单号
     */
    private String innerOrderNo;

    /**
     * 送货方式 由字典编码delivery_method定义
     */
    private String deliveryMethod;

    /**
     * 送货车牌
     */
    private String deliveryCarNo;

    /**
     * 自提地址
     */
    private String selfMentionAddress;

    /**
     * 送货地址
     */
    private String deliveryAddress;

    /**
     * 物流单号
     */
    private String shipmentNumber;

    /**
     * 送货备注
     */
    private String deliveryRemark;

    /**
     * 运输方式 由字典编码delivery_transport_method定义
     */
    private String transportMethod;

    /**
     * 司机姓名
     */
    private String driverName;

    /**
     * 司机电话
     */
    private String driverTelephone;

    /**
     * 收货联系人
     */
    private String receivingContact;

    /**
     * 整单毛重
     */
    private BigDecimal grossWeight;

    /**
     * 包装方式
     */
    private String packageMethod;

    /**
     * 包装总数
     */
    private BigDecimal packageNum;

    /**
     * 整单净重
     */
    private BigDecimal netWeight;

    /**
     * 重量单位 由字典编码weight_unit定义
     */
    private String weightUnitCode;

    /**
     * 明细包装汇总
     */
    private String packageTotal;

    /**
     * 实际送货总数量
     */
    private Integer realDeliveryNum;

    /**
     * 送货单金额
     */
    private BigDecimal deliveryAmount;

    /**
     * 送货状态 由字典delivery_status_code定义
     */
    private String deliveryStatusCode;

    /**
     * 校验状态 由字典delivery_check_status定义
     */
    private String deliveryCheckStatus;

    /**
     * 采购类型 由字典delivery_purchase_type定义
     */
    private String deliveryPurchaseType;

    /**
     * 送货类型 由字典delivery_type_code定义
     */
    private String deliveryTypeCode;

    /**
     * 随货送票 1是，2否
     */
    private String deliveryTicket;

    /**
     * 送货申请开启状态 N 关闭，Y 开启
     */
    private Boolean deliveryApplication;

    /**
     * 优先校验 Y是，N否
     */
    private String priorityCheck;

    /**
     * 物流状态 由字典logistics_status 定义
     */
    private String logisticsStatus;

    /**
     * 到货时间
     */
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
    @JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
    private Date arrivalTime;

    /**
     * 签收时间
     */
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
    @JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
    private Date signingTime;

    /**
     * 签收时间
     */
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
    @JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
    private Date erpSigningTime;

    /**
     * ERP签收状态
     */
    private String erpSigningStatus;

    /**
     * 结算池，入池状态，N未入池，Y已入池
     */
    private String settlementPoolSyncStatus;

    /**
     * 检验结果
     */
    private String checkResults;

    /**
     * 签收差异
     */
    private String signInDiscrepancy;

    /**
     * 创建人名称
     */
    private String createdByName;

    /**
     * 创建人
     */
    private Long createdBy;


    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATE)
    @JsonFormat(pattern = DateUtil.PATTERN_DATE)
    private Date createdDate;

    /**
     * 更新人
     */
    private Long lastModifiedBy;

    /**
     * 更新时间
     */
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATE)
    @JsonFormat(pattern = DateUtil.PATTERN_DATE)
    private Date lastModifiedDate;


    /**
     * 状态[0:未删除,1:删除]
     */
    private Integer isDeleted;


}
