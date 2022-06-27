package net.bncloud.financial.vo.event;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.bncloud.common.util.DateUtil;
import net.bncloud.financial.entity.FinancialStatement;

import java.util.Date;

/**
 * @author Toby
 */
@Data
@NoArgsConstructor
public class FinancialStatementEvent extends FinancialStatement {

    private Long businessId;

    private String addTime = DateUtil.formatDateTime(new Date());
}
