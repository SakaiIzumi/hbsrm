package net.bncloud.financial.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import net.bncloud.base.BaseServiceImpl;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.util.CollectionUtil;
import net.bncloud.financial.entity.FinancialSettlementPool;
import net.bncloud.financial.mapper.FinancialSettlementPoolMapper;
import net.bncloud.financial.param.FinancialSettlementPoolParam;
import net.bncloud.financial.service.FinancialSettlementPoolService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 结算池单据信息表 服务实现类
 * </p>
 *
 * @author Auto-generator
 * @since 2021-12-15
 */
@Service
public class FinancialSettlementPoolServiceImpl extends BaseServiceImpl<FinancialSettlementPoolMapper, FinancialSettlementPool> implements FinancialSettlementPoolService {


    @Override
    public IPage<FinancialSettlementPool> selectPage(IPage<FinancialSettlementPool> page, QueryParam<FinancialSettlementPoolParam> queryParam) {
        return page.setRecords(baseMapper.selectListPage(page, queryParam));
    }

    /**
     * 更新是否已生成对账单标志
     *
     * @param billIdList
     * @param statementCreated
     */
    @Override
    public boolean updateStatementCreatedByBillIdList(List<Long> billIdList, String statementCreated) {
        if (CollectionUtil.isNotEmpty(billIdList)) {
            return update(Wrappers.<FinancialSettlementPool>update().lambda()
                    .set(FinancialSettlementPool::getStatementCreated, statementCreated)
                    .in(FinancialSettlementPool::getBillId, billIdList)
            );
        }
        return false;
    }

    /**
     * 更新是否已生成对账单标志
     *
     * @param idList
     * @param statementCreated
     */
    @Override
    public boolean updateStatementCreatedByIdList(List<Long> idList, String statementCreated) {
        if (CollectionUtil.isNotEmpty(idList)) {
            return update(Wrappers.<FinancialSettlementPool>update().lambda()
                    .set(FinancialSettlementPool::getStatementCreated, statementCreated)
                    .in(FinancialSettlementPool::getId, idList)
            );
        }
        return false;
    }
    /**
     * 根据结算池id，更新生成对账单标志
     *
     * @param settlementId     结算池id
     * @param statementCreated 生成对账单标志
     * @return 更新结果
     */
    @Override
    public boolean updateStatementCreatedBySettlementId(Long settlementId, String statementCreated) {
        return update(Wrappers.<FinancialSettlementPool>update().lambda()
                .set(FinancialSettlementPool::getStatementCreated, statementCreated)
                .eq(FinancialSettlementPool::getId, settlementId)
        );
    }
}
