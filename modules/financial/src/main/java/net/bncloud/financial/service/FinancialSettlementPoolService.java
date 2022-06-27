package net.bncloud.financial.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.base.BaseService;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.financial.entity.FinancialSettlementPool;
import net.bncloud.financial.param.FinancialSettlementPoolParam;

import java.util.List;

/**
 * <p>
 * 结算池单据信息表 服务类
 * </p>
 *
 * @author Auto-generator
 * @since 2021-12-15
 */
public interface FinancialSettlementPoolService extends BaseService<FinancialSettlementPool> {

    /**
     * 自定义分页
     *
     * @param page
     * @param queryParam
     * @return
     */
    IPage<FinancialSettlementPool> selectPage(IPage<FinancialSettlementPool> page, QueryParam<FinancialSettlementPoolParam> queryParam);

    /**
     * 更新是否已生成对账单标志
     *
     * @param billIdList
     * @param statementCreated
     */
    boolean updateStatementCreatedByBillIdList(List<Long> billIdList, String statementCreated);

    /**
     * 更新是否已生成对账单标志
     *
     * @param idList
     * @param statementCreated
     */
    boolean updateStatementCreatedByIdList(List<Long> idList, String statementCreated);

    /**
     * 根据结算池id，更新生成对账单标志
     *
     * @param settlementId     结算池
     * @param statementCreated
     * @return
     */
    boolean updateStatementCreatedBySettlementId(Long settlementId, String statementCreated);
}
