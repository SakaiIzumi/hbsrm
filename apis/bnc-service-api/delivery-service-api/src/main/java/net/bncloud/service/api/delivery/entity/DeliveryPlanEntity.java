package net.bncloud.service.api.delivery.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import net.bncloud.common.util.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 * 送货计划基础信息表
 * </p>
 *
 * @author Auto-generator
 * @since 2022-01-15
 */
@Data
@Accessors(chain = true)
public class DeliveryPlanEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 订单类型 ERP获取类型
     */
    @ApiModelProperty("订单类型 ERP获取类型")
    private String orderType;

    /**
     * 计划编号
     */
    @ApiModelProperty(value = "计划编号")
    private String planNo;

    /**
     * 协作组织id
     */
    @ApiModelProperty(value = "协作组织id")
    private Long orgId;

    /**
     * 采购订单号
     */
    @ApiModelProperty(value = "采购订单号")
    private String purchaseOrderNo;


    /**
     * 单据编号
     */
    @ApiModelProperty(value = "单据编号")
    private String billNo;

    /**
     * 采购方编码
     */
    @ApiModelProperty(value = "采购方编码")
    private String purchaseCode;

    /**
     * 采购方名称
     */
    @ApiModelProperty(value = "采购方名称")
    private String purchaseName;

    /**
     * 供应方名称
     */
    @ApiModelProperty(value = "供应方名称")
    private String supplierCode;

    /**
     * 供应方名称
     */
    @ApiModelProperty(value = "供应方名称")
    private String supplierName;

    /**
     * 发布人
     */
    @ApiModelProperty(value = "发布人")
    private String publisher;

    /**
     * 发布时间
     */
    @ApiModelProperty(value = "发布时间")
    private LocalDateTime publishDate;

    /**
     * 计划区间-开始时间
     */
    @ApiModelProperty(value = "计划区间-开始时间")
    private LocalDateTime planStartDate;

    /**
     * 计划区间-结束时间
     */
    @ApiModelProperty(value = "计划区间-结束时间")
    private LocalDateTime planEndDate;

    /**
     * 计划状态：待确认、已确认
     */
    @ApiModelProperty(value = "计划状态：待确认、已确认")
    private String planStatus;

    /**
     * 计划说明
     */
    @ApiModelProperty(value = "计划说明")
    private String planDescription;


    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private Long createdBy;


    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createdDate;

    /**
     * 更新人
     */
    @ApiModelProperty(value = "更新人")
    private Long lastModifiedBy;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private Date lastModifiedDate;


    /**
     * 状态[0:未删除,1:删除]
     */
    @ApiModelProperty(value = "是否已删除")
    private Integer isDeleted;

    /**
     * 来源系统主键ID
     */
    @ApiModelProperty(value = "来源系统主键ID")
    private String sourceId;

    /**
     * 结算币种
     */
    @ApiModelProperty(value = "结算币种")
    private String currency;

    /**
     * 入货仓
     */
    @ApiModelProperty(value = "入货仓")
    private String warehousing;

    /**
     * 物料分类
     */
    @ApiModelProperty(value = "物料分类")
    private String materialClassification;


    /**
     * 采购日期
     */
    private LocalDateTime purchaseTime;


}
