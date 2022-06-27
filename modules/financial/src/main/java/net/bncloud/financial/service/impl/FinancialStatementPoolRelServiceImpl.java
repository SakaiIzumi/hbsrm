package net.bncloud.financial.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import net.bncloud.base.BaseServiceImpl;
import net.bncloud.financial.entity.FinancialStatementPoolRel;
import net.bncloud.financial.mapper.FinancialStatementPoolRelMapper;
import net.bncloud.financial.service.FinancialStatementPoolRelService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 对账单与结算单池单据关联关系表 服务实现类
 * </p>
 *
 * @author Auto-generator
 * @since 2021-12-15
 */
@Service
public class FinancialStatementPoolRelServiceImpl extends BaseServiceImpl<FinancialStatementPoolRelMapper, FinancialStatementPoolRel> implements FinancialStatementPoolRelService {


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchSave(Set<Long> poolIdSet, Long statementId) {
        List<FinancialStatementPoolRel> poolRelList = new ArrayList<>();
        for (Long poolId : poolIdSet){
            FinancialStatementPoolRel poolRel = new FinancialStatementPoolRel();
            poolRel.setSettlementPoolId(poolId);
            poolRel.setStatementId(statementId);
            poolRelList.add(poolRel);
        }
        saveBatch(poolRelList);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByStatementId(Long statementId){
        remove(Wrappers.<FinancialStatementPoolRel>query()
                .lambda().eq(FinancialStatementPoolRel::getStatementId, statementId));
    }
}
