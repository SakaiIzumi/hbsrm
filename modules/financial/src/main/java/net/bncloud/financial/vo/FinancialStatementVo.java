package net.bncloud.financial.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.bncloud.financial.entity.FinancialStatement;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 对账单信息表
 * </p>
 *
 * @author Auto-generator
 * @since 2021-12-15
 */
@Data
@ApiModel("对账单")
public class FinancialStatementVo extends FinancialStatement implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 采购方，可操作按钮
     */
    private Map<String, Boolean> purchasePermissionButton;

    /**
     * 供应商，可操作按钮
     */
    private Map<String, Boolean> supplierPermissionButton;


    @ApiModelProperty("费用明细")
    private List<FinancialCostDetailVo> FinancialReceivableDiscountDetailList;

    @ApiModelProperty("送货明细")
    private List<FinancialDeliveryDetailVo> FinancialDeliveryDetailList;

}
