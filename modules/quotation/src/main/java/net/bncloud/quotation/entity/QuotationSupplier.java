package net.bncloud.quotation.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import net.bncloud.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import net.bncloud.common.util.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.OffsetDateTime;
import java.util.Date;

/**
 * <p>
 * 询价供应商信息
 * </p>
 *
 * @author Auto-generator
 * @since 2022-02-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_rfq_quotation_supplier")

public class QuotationSupplier extends BaseEntity {

    private static final long serialVersionUID = 1L;


    /**
     * 询价单主键ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "询价单主键ID")
    private Long quotationBaseId;

    /**
     * 供应商ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "供应商ID")
    private Long supplierId;

    /**
     * 供应商账号
     */
    @ApiModelProperty(value = "供应商账号")
    private String supplierAccount;

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
     * 供应商类型，formal正式，potential潜在
     */
    @ApiModelProperty(value = "供应商类型，formal正式，potential潜在")
    private String supplierType;

    /**
     * 平台类型
     */
    @ApiModelProperty(value = "平台类型")
    private String platformType;

    /**
     * 响应状态
     */
    @ApiModelProperty(value = "响应状态")
    private String responseStatus;

    /**
     * 响应时间
     */
    @ApiModelProperty(value = "响应时间")
    private Date responseTime;

    /**
     * 接收人姓名
     */
    @ApiModelProperty(value = "接收人姓名")
    private String receiver;

    /**
     * 联系人手机号
     */
    @ApiModelProperty(value = "联系人手机号")
    private String phone;

    /**
     * 邮箱
     */
    @ApiModelProperty(value = "邮箱")
    private String email;

}
