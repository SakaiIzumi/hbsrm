package net.bncloud.financial.param;


import lombok.Data;
import net.bncloud.financial.entity.FinancialCostDetail;
import net.bncloud.financial.entity.FinancialDeliveryDetail;
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
public class FinancialStatementSaveParam extends FinancialStatement implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 出货明细表信息表
     */
    private List<FinancialDeliveryDetail> deliveryDetailList;

    /**
     * 费用信息表
     */
    private List<FinancialCostDetail> costDetailList;

}
