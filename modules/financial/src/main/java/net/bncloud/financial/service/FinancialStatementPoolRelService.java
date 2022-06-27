package net.bncloud.financial.service;

import net.bncloud.base.BaseService;
import net.bncloud.financial.entity.FinancialStatementPoolRel;

import java.util.Set;

/**
 * <p>
 * 对账单与结算单池单据关联关系表 服务类
 * </p>
 *
 * @author Auto-generator
 * @since 2021-12-15
 */
public interface FinancialStatementPoolRelService extends BaseService<FinancialStatementPoolRel> {

    void batchSave(Set<Long> poolIdSet, Long statementId);

    void deleteByStatementId(Long statementId);
}
