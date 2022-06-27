package net.bncloud.financial.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.bncloud.base.BaseEntity;

/**
 * <p>
 * 对账单退回原因信息表
 * </p>
 *
 * @author Auto-generator
 * @since 2021-12-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_financial_back_reason")

public class FinancialBackReason extends BaseEntity {

    private static final long serialVersionUID = 1L;


    /**
     * 单据ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "单据ID")
    private Long billId;

    /**
     * 单据类型，0对账单，1费用单
     */
    @ApiModelProperty(value = "单据类型，0对账单，1费用单")
    private String billType;

    /**
     * 退回原因
     */
    @ApiModelProperty(value = "退回原因")
    private String reason;

}
