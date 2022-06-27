package net.bncloud.financial.param;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.bncloud.financial.entity.FinancialStatement;

import java.io.Serializable;
import java.util.List;


/**
 * <p>
 * 对账单信息表
 * </p>
 *
 * @author Auto-generator
 * @since 2021-12-15
 */
@Data
public class FinancialStatementParam extends FinancialStatement implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "分栏：全部、待发送、待确认、已确认")
    private List<String> statementStatusList;

    /**
     * 对账周期开始时间
     */
    @ApiModelProperty(value = "对账周期开始时间")
    private String periodTimeStart;

    /**
     * 对账周期结束时间
     */
    @ApiModelProperty(value = "对账周期结束时间")
    private String periodTimeEnd;


    /**
     * 供应商
     */
    @ApiModelProperty(value = "供应商")
    private String supplier;

    /**
     * 采购方
     */
    @ApiModelProperty(value = "采购方")
    private String customer;

    /**
     * 查询工作台
     */
    @ApiModelProperty(value = "查询工作台")
    private String workBench;

}
