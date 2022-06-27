package net.bncloud.financial.vo.event;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.bncloud.common.util.DateUtil;
import net.bncloud.financial.entity.FinancialCostBill;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * 费用单信息表
 */
@Data
@NoArgsConstructor
public class FinancialCostBillEvent extends FinancialCostBill implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 可操作按钮
     */
    private Map<String, Boolean> permissionButton;

    private Long accountCostBillId;

    private Long businessId;


    private String addTime = DateUtil.formatDateTime(new Date());
}
