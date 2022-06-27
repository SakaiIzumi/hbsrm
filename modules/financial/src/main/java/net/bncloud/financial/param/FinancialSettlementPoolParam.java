package net.bncloud.financial.param;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.bncloud.financial.entity.FinancialSettlementPool;

import java.io.Serializable;


/**
 * <p>
 * 结算池单据信息表
 * </p>
 *
 * @author Auto-generator
 * @since 2021-12-15
 */
@Data
public class FinancialSettlementPoolParam extends FinancialSettlementPool implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 供应商搜索内容（按编码/名称）
     */
    @ApiModelProperty(value = "供应商搜索内容")
    private String supplierSearch;

    /**
     * 采购方搜索内容（按编码/名称）
     */
    @ApiModelProperty(value = "采购方搜索内容")
    private String customerSearch;

    /**
     * 确认开始时间
     */
    @ApiModelProperty(value = "确认开始时间")
    private String confirmTimeStart;

    /**
     * 确认结束时间
     */
    @ApiModelProperty(value = "确认结束时间")
    private String confirmTimeEnd;


}
