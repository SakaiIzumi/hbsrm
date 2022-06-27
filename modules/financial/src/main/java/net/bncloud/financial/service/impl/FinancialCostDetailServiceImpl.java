package net.bncloud.financial.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import net.bncloud.base.BaseServiceImpl;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.exception.BizException;
import net.bncloud.common.util.BeanUtil;
import net.bncloud.common.util.CalculateUtil;
import net.bncloud.common.util.CollectionUtil;
import net.bncloud.financial.entity.*;
import net.bncloud.financial.enums.FinancialBillTypeEnum;
import net.bncloud.financial.enums.FinancialResultCode;
import net.bncloud.financial.event.statement.StatementDetailChangeEvent;
import net.bncloud.financial.mapper.FinancialCostDetailMapper;
import net.bncloud.financial.param.FinancialCostDetailBatchSaveParam;
import net.bncloud.financial.param.FinancialCostDetailParam;
import net.bncloud.financial.service.*;
import net.bncloud.financial.vo.FinancialCostDetailVo;
import net.bncloud.support.Condition;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 费用信息表 服务实现类
 * </p>
 *
 * @author Auto-generator
 * @since 2021-12-15
 */
@Service
public class FinancialCostDetailServiceImpl extends BaseServiceImpl<FinancialCostDetailMapper, FinancialCostDetail> implements FinancialCostDetailService {

    private final FinancialSettlementPoolService financialSettlementPoolService;

    private final FinancialStatementPoolRelService financialStatementPoolRelService;

    private final FinancialCostBillService financialCostBillService;

    private final FinancialCostBillLineService financialCostBillLineService;

    @Lazy
    @Autowired
    private FinancialStatementService financialStatementService;

    public FinancialCostDetailServiceImpl(FinancialSettlementPoolService financialSettlementPoolService, FinancialStatementPoolRelService financialStatementPoolRelService, FinancialCostBillService financialCostBillService, FinancialCostBillLineService financialCostBillLineService) {
        this.financialSettlementPoolService = financialSettlementPoolService;
        this.financialStatementPoolRelService = financialStatementPoolRelService;
        this.financialCostBillService = financialCostBillService;
        this.financialCostBillLineService = financialCostBillLineService;
    }

    /**
     * 保存费用明细
     *
     * @param statementId             对账单ID
     * @param financialCostDetailList 费用明细
     * @return
     */
    @Override
    public boolean saveDetails(Long statementId, List<FinancialCostDetail> financialCostDetailList) {
        if (CollectionUtils.isNotEmpty(financialCostDetailList)) {
            for (FinancialCostDetail financialCostDetail : financialCostDetailList) {
                financialCostDetail.setId(null);
                financialCostDetail.setStatementId(statementId);
            }

            return saveBatch(financialCostDetailList);
        }

        return false;

    }

    @Override
    public IPage<FinancialCostDetail> selectPage(IPage<FinancialCostDetail> page, QueryParam<FinancialCostDetailParam> pageParam) {
        return page.setRecords(baseMapper.selectListPage(page, pageParam));
    }

    /**
     * 生成对账单-费用明细
     *
     * @param financialStatement 对账单信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void generateCostDetail(FinancialStatement financialStatement) {
        List<Long> costIdList = getPoolIdList(financialStatement);
        assembleCostDetail(financialStatement.getId(),costIdList);
    }

    /**
     * 组装对账单费用明细
     * @param statementId
     * @param billId
     * @return
     */
    @Override
    public FinancialCostDetailVo getBuildCostDetail(Long statementId, Long billId){
        FinancialCostBill one = financialCostBillService.getById(billId);
        if(Objects.isNull(one)){
            return null;
        }
        FinancialCostDetail costDetail = buildCostDetail(one);
        costDetail.setStatementId(statementId);
        FinancialCostDetailVo costDetailVo = BeanUtil.copy(costDetail, FinancialCostDetailVo.class);
        return costDetailVo;
    }

    private void assembleCostDetail(Long statementId,List<Long> costIdList){
        if (CollectionUtil.isEmpty(costIdList)) {
            return;
        }
        List<FinancialCostBill> costBillList = financialCostBillService.list(Condition.getQueryWrapper(new FinancialCostBill()).lambda().in(FinancialCostBill::getId, costIdList));
        if (CollectionUtil.isNotEmpty(costBillList)) {
            List<FinancialCostDetail> costDetailList = costBillList.stream().map(this::buildCostDetail).collect(Collectors.toList());
            saveDetails(statementId, costDetailList);
        }

        financialSettlementPoolService.updateStatementCreatedByBillIdList(costIdList, "Y");
    }

    /**
     * 批量保存
     *保存费用明细时，将费用明细的 费用含税金额、对账含税金额、未税金额和税额 累加到对账单中
     * @param batchSaveParam 折让明细保存参数
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void batchSave(FinancialCostDetailBatchSaveParam batchSaveParam) {
        long statementId = batchSaveParam.getStatementId();
        List<Long> costIdList = batchSaveParam.getCostIdList();
        assembleCostDetail(statementId,costIdList);

        List<FinancialSettlementPool> list = financialSettlementPoolService.list(Wrappers
                .<FinancialSettlementPool>lambdaQuery()
                .in(FinancialSettlementPool::getBillId, costIdList));
        if (CollectionUtil.isNotEmpty(list)) {
            Set<Long> set = list.stream().map(FinancialSettlementPool::getId).collect(Collectors.toSet());
            financialStatementPoolRelService.batchSave(set, statementId);
        }
        //统计金额
        Collection<FinancialCostDetail> costDetails = listByIds(costIdList);
        FinancialStatement statement = financialStatementService.getById(statementId);
        //含税金额
        Optional<BigDecimal> checkIncludeTaxOpt = costDetails.stream().map(FinancialCostDetail::getCheckIncludeTax).reduce(BigDecimal::add);
        checkIncludeTaxOpt.ifPresent(checkIncludeTax->{
            //费用含税金额
            statement.setCostIncludingTax(checkIncludeTax.add(statement.getCostIncludingTax()));
            //对账含税金额
            statement.setStatementIncludingTax(checkIncludeTax.add(statement.getStatementIncludingTax()));
        });

        //未税金额
        Optional<BigDecimal> checkNotTaxAmountOpt = costDetails.stream().map(FinancialCostDetail::getCheckNotTaxAmount).reduce(BigDecimal::add);
        checkNotTaxAmountOpt.ifPresent(checkNotTaxAmount->{
            //对账未税金额
            statement.setStatementNotTax(checkNotTaxAmount.add(statement.getStatementNotTax()));
            //费用未税金额
            statement.setCostNotIncludingTax(checkNotTaxAmount.add(statement.getCostNotIncludingTax()));
        });

        //税额
        Optional<BigDecimal> checkTaxAmountOpt = costDetails.stream().map(FinancialCostDetail::getCheckTaxAmount).reduce(BigDecimal::add);
        checkTaxAmountOpt.ifPresent(checkTaxAmount->{
            //对账税额
            statement.setStatementTaxAmount(checkTaxAmount.add(statement.getStatementTaxAmount()));
            //费用税额
            statement.setCostTaxAmount(checkTaxAmount.add(statement.getCostTaxAmount()));
        });

        financialStatementService.updateById(statement);

        applicationEventPublisher.publishEvent(new StatementDetailChangeEvent(this, batchSaveParam.getStatementId()));
    }

    /**
     * @param id
     */
    @Override
    public void deleteById(long id) {
        FinancialCostDetail costDetail = getById(id);
        if (costDetail == null) {
            throw new BizException(FinancialResultCode.RESOURCE_NOT_FOUND);
        }
        removeById(id);
        financialSettlementPoolService.updateStatementCreatedByBillIdList(Collections.singletonList(costDetail.getCostBillId()), "N");
    }

    /**
     * @param statementId
     */
    @Override
    public void deleteByStatementId(long statementId) {
        List<FinancialCostDetail> list = list(Wrappers.<FinancialCostDetail>query().lambda().eq(FinancialCostDetail::getStatementId,statementId));
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        List<Long> collect = list.stream().map(FinancialCostDetail::getId).collect(Collectors.toList());
        List<Long> costBillIds = list.stream().map(FinancialCostDetail::getCostBillId).collect(Collectors.toList());
        removeByIds(collect);
        financialSettlementPoolService.updateStatementCreatedByBillIdList(costBillIds, "N");
    }

    /**
     * 查询金额汇总信息
     *
     * @param statementId 对账单ID
     * @return 对账单金额信息
     */
    @Override
    public FinancialStatement querySummaryAmountByStatementId(Long statementId) {
        return this.baseMapper.querySummaryAmountByStatementId(statementId);
    }

    /**
     * 构建对账单 。业务调整，已过时，推荐使用 buildDiscountDetail(FinancialSettlementPool settlementPool)
     *
     * @param bill 费用单
     * @return
     */
    private FinancialCostDetail buildCostDetail(FinancialCostBill bill) {
        if (bill != null) {
            List<FinancialCostBillLine> costBillLineList = financialCostBillLineService.list(Condition.getQueryWrapper(new FinancialCostBillLine()).lambda().eq(FinancialCostBillLine::getCostBillId, bill.getId()));
            if(CollectionUtils.isEmpty(costBillLineList)){
                return null;
            }
            FinancialCostDetail costDetail = new FinancialCostDetail();
            BigDecimal costIncludingTax = new BigDecimal(0);
            BigDecimal costNotIncludingTax = new BigDecimal(0);
            BigDecimal costTaxAmount = new BigDecimal(0);
            for(FinancialCostBillLine billLine : costBillLineList){
                costIncludingTax = CalculateUtil.add(costIncludingTax, billLine.getTaxIncludedAmount());
                costNotIncludingTax = CalculateUtil.add(costNotIncludingTax, billLine.getNotTaxAmount());
                costTaxAmount = CalculateUtil.add(costTaxAmount, billLine.getTaxAmount());
            }
            costDetail.setErpBillId(bill.getErpBillId());
            costDetail.setBillNo(bill.getCostBillNo());
            costDetail.setErpBillNo(bill.getErpBillNo());
            costDetail.setCostBillId(bill.getId());
            costDetail.setDocumentTypeCode(bill.getCostBillType());
            costDetail.setDocumentTypeName("费用单");
            costDetail.setCostAmount(bill.getAllAmount());
            costDetail.setCheckIncludeTax(costIncludingTax);
            costDetail.setCheckNotTaxAmount(costNotIncludingTax);
            costDetail.setCheckTaxAmount(costTaxAmount);
            costDetail.setCustomerConfirm(null);
            costDetail.setRemark(bill.getRemark());
            return costDetail;
        }
        return null;
    }

    private List<Long> getPoolIdList(FinancialStatement financialStatement) {
        LambdaQueryWrapper<FinancialStatementPoolRel> queryWrapper = Condition.getQueryWrapper(new FinancialStatementPoolRel()).lambda();
        queryWrapper.eq(FinancialStatementPoolRel::getStatementId, financialStatement.getId());
        List<FinancialStatementPoolRel> poolRelList = financialStatementPoolRelService.list(queryWrapper);
        if (CollectionUtils.isEmpty(poolRelList)) {
            return null;
        }

        Set<Long> poolIds = poolRelList.stream().map(FinancialStatementPoolRel::getSettlementPoolId).collect(Collectors.toSet());
        LambdaQueryWrapper<FinancialSettlementPool> queryWrapper2 = Condition.getQueryWrapper(new FinancialSettlementPool()).lambda();
        queryWrapper2.in(FinancialSettlementPool::getId, poolIds);
        queryWrapper2.eq(FinancialSettlementPool::getBillType, FinancialBillTypeEnum.COST.name());

        List<FinancialSettlementPool> list = financialSettlementPoolService.list(queryWrapper2);
        if (CollectionUtil.isNotEmpty(list)) {
            return list.stream().map(FinancialSettlementPool::getBillId).collect(Collectors.toList());
        }

        return null;
    }

    /**
     * 查询下一个项次
     *
     * @param statementId 对账单ID
     * @return 下一个项次编号
     */
    @Override
    public String queryNextItemNo(Long statementId){
        return this.baseMapper.queryNextItemNo(statementId);
    }
}
