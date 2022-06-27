package net.bncloud.bis.srm.financial.model.vo;


import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.bncloud.base.BaseEntity;
import net.bncloud.common.util.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 费用明细信息表
 * </p>
 *
 * @author Auto-generator
 * @since 2021-12-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_financial_cost_bill_line")
public class FinancialCostBillLine extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "费用单据id")
    private String costBillId;
    /**
     * 费用摘要
     */
    @ApiModelProperty(value = "费用摘要")
    private String costSummary;

    /**
     * 费用名称
     */
    @ApiModelProperty(value = "费用名称")
    private String costName;

    /**
     * 费用类型;字典account_cost_type
     */
    @ApiModelProperty(value = "费用类型;字典account_cost_type")
    private String costType;

    /**
     * 费用原因
     */
    @ApiModelProperty(value = "费用原因")
    private String costReason;

    /**
     * 总账科目
     */
    @ApiModelProperty(value = "总账科目")
    private String generalAccount;

    /**
     * 费用金额
     */
    @ApiModelProperty(value = "费用金额")
    private BigDecimal costAmount;

    /**
     * 费用日期
     */
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
    @JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
    @ApiModelProperty(value = "费用日期")
    private LocalDateTime costDate;

    /**
     * 成本中心
     */
    @ApiModelProperty(value = "成本中心")
    private String costCenter;

    /**
     * 内部订单ID
     */
    @ApiModelProperty(value = "内部订单ID")
    private String sourceBillId;

    /**
     * 来源系统标志
     */
    @ApiModelProperty(value = "来源系统标志")
    private String sourceSystemType;

    /**
     * 备注说明
     */
    @ApiModelProperty(value = "备注说明")
    private String remark;

}
