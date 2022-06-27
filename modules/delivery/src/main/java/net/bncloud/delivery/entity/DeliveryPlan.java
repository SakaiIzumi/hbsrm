package net.bncloud.delivery.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import net.bncloud.base.BaseEntity;
import net.bncloud.common.util.DateUtil;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * <p>
 * 送货计划基础信息表
 * </p>
 *
 * @author Auto-generator
 * @since 2022-01-15
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_delivery_plan")
public class DeliveryPlan extends BaseEntity {

    private static final long serialVersionUID = 1L;


    /**
     * 计划编号
     */
    @ApiModelProperty(value = "计划编号")
    @Length(min = 0, max = 255)
    private String planNo;

    /**
     * 单据编号
     */
    @ApiModelProperty(value = "单据编号")
    @Length(min = 0, max = 255)
    private String billNo;
    /**
     * 采购订单号
     */
    @ApiModelProperty(value = "采购订单号")
    @Length(min = 0, max = 255)
    private String purchaseOrderNo;

    /**
     * 协作组织号
     */
    @ApiModelProperty(value = "协作组织号")
    private Long orgId;

    /**
     * 采购方编码
     */
    @ApiModelProperty(value = "采购方编码")
    @Length(min = 0, max = 255)
    private String purchaseCode;

    /**
     * 采购方名称
     */
    @ApiModelProperty(value = "采购方名称")
    @Length(min = 0, max = 255)
    private String purchaseName;

    /**
     * 供应方编码
     */
    @ApiModelProperty(value = "供应方编码")
    @Length(min = 0, max = 255)
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
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATE)
    @JsonFormat(pattern = DateUtil.PATTERN_DATE)
    @ApiModelProperty(value = "发布时间")
    private LocalDateTime publishDate;

    /**
     * 计划区间-开始时间
     */
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
    @JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
    @ApiModelProperty(value = "计划区间-开始时间")
    private LocalDateTime planStartDate;

    /**
     * 计划区间-结束时间
     */
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
    @JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
    @ApiModelProperty(value = "计划区间-结束时间")
    private LocalDateTime planEndDate;

    /**
     * 计划状态：0待发布、1待确认、2已确认、3差异待采购方确认、4差异待供应商确认
     */
    @ApiModelProperty(value = "计划状态：0待发布、1待确认、2已确认、3差异待采购方确认、4差异待供应商确认")
    @Length(min = 0, max = 255)
    private String planStatus;

    /**
     *订单类型 ERP获取类型
     */
    @ApiModelProperty(value = "订单类型 ERP获取类型")
    @Length(min = 0, max = 255)
    private String orderType;

    /**
     * 计划说明
     */
    @ApiModelProperty(value = "计划说明")
    private String planDescription;

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
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
    @JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
    private LocalDateTime purchaseTime;

    /**
     * 数据来源类型 mrp/purchaseOrder
     * 字典 ：delivery_source_type
     */
    private String sourceType;



    /**
     * mrp运算编号/版号
     */
    private String mrpComputerNo;

    public DeliveryPlan(String planNo) {
        this.planNo = planNo;
    }
}
