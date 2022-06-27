package net.bncloud.quotation.entity;


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

/**
 * <p>
 * 报价记录信息
 * </p>
 *
 * @author Auto-generator
 * @since 2022-02-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_rfq_quotation_record")

public class TRfqQuotationRecord extends BaseEntity {

	private static final long serialVersionUID = 1L;


    /**
     * 询价单主键ID
     */
    @ApiModelProperty(value = "询价单主键ID")
    private Long quotationBaseId;

    /**
     * 询价单号
     */
    @ApiModelProperty(value = "询价单号")
    private String quotationNo;

    /**
     * 供应商ID
     */
    @ApiModelProperty(value = "供应商ID")
    private Long supplierId;

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
     * 报价轮次
     */
    @ApiModelProperty(value = "报价轮次")
    private Integer roundNumber;

    /**
     * 本轮报价，招标行信息
     */
    @ApiModelProperty(value = "本轮报价，招标行信息")
    private String extContent;

    /**
     * 报价时间
     */
@DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
@JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
    @ApiModelProperty(value = "报价时间")
    private OffsetDateTime quoteTime;

    /**
     * 报价人
     */
    @ApiModelProperty(value = "报价人")
    private String tenderer;

    /**
     * 状态
     */
    @ApiModelProperty(value = "状态")
    private String status;

}
