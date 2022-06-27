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
 * 支付方式信息表
 * </p>
 *
 * @author Auto-generator
 * @since 2021-12-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_financial_pay_method")

public class FinancialPayMethod extends BaseEntity {

    private static final long serialVersionUID = 1L;


    /**
     * 支付方式名称
     */
    @ApiModelProperty(value = "支付方式名称")
    private String methodName;

    /**
     * 协作组织ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "协作组织ID")
    private Long orgId;

}
