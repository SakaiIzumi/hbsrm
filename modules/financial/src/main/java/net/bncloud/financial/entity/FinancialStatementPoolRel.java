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
 * 对账单与结算单池单据关联关系表
 * </p>
 *
 * @author Auto-generator
 * @since 2021-12-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_financial_statement_pool_rel")

public class FinancialStatementPoolRel extends BaseEntity {

    private static final long serialVersionUID = 1L;


    /**
     * 结算池单据ID;t_account_settlement_pool.id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "结算池单据ID;t_account_settlement_pool.id")
    private Long settlementPoolId;

    /**
     * 对账单ID;t_account_statement.id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "对账单ID;t_account_statement.id")
    private Long statementId;

}
