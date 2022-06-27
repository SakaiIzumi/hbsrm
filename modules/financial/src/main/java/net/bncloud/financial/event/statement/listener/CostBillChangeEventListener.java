package net.bncloud.financial.event.statement.listener;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.common.util.CalculateUtil;
import net.bncloud.common.util.ThrowableUtils;
import net.bncloud.financial.entity.*;
import net.bncloud.financial.event.statement.CostBillChangeEvent;
import net.bncloud.financial.event.statement.StatementDetailChangeEvent;
import net.bncloud.financial.service.*;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author Toby
 */
@Component
@Slf4j
public class CostBillChangeEventListener {

    private final FinancialCostBillService costBillService;

    private final FinancialCostBillLineService costBillLineService;

    private final FinancialSettlementPoolService settlementPoolService;

    private final FinancialStatementService statementService;

    private final FinancialCostDetailService costDetailService;

    private final FinancialStatementPoolRelService financialStatementPoolRelService;

    @Resource
    private ApplicationEventPublisher applicationEventPublisher;

    public CostBillChangeEventListener(FinancialCostBillService costBillService, FinancialCostBillLineService costBillLineService, FinancialSettlementPoolService settlementPoolService, FinancialStatementService statementService, FinancialCostDetailService costDetailService, FinancialStatementPoolRelService financialStatementPoolRelService) {
        this.costBillService = costBillService;
        this.costBillLineService = costBillLineService;
        this.settlementPoolService = settlementPoolService;
        this.statementService = statementService;
        this.costDetailService = costDetailService;
        this.financialStatementPoolRelService = financialStatementPoolRelService;
    }

    /**
     * 费用单更新后需要更新费用单明细行、结算池、对账单、对账单费用明细,供应商编码跟采购方编码更新的情况不考虑
     * @param changeEvent
     */
    @EventListener(CostBillChangeEvent.class)
    public void statementDetailChangeListener(CostBillChangeEvent changeEvent) {
        try {
            Long billId = changeEvent.getBillId();
            FinancialCostBill bill = costBillService.getById(billId);
            if (bill == null) {
                log.warn("未查到费用单信息");
                return;
            }
            Long statementId = null;
            List<FinancialCostBillLine> list = costBillLineService.list(Wrappers.<FinancialCostBillLine>query()
                    .lambda().eq(FinancialCostBillLine::getCostBillId, bill.getId()));
            FinancialSettlementPool pool = settlementPoolService.getOne(Wrappers.<FinancialSettlementPool>query()
                    .lambda().eq(FinancialSettlementPool::getErpBillId, bill.getErpBillId()));
            if(Objects.nonNull(pool)){
                pool.setErpBillId(bill.getErpBillId());
                pool.setErpBillType(bill.getErpBillType());
                pool.setErpBillNo(bill.getErpBillNo());
                pool.setBillId(bill.getId());
                pool.setBillNo(bill.getCostBillNo());
                pool.setBillType(pool.getBillType());
                pool.setCurrencyCode(bill.getCurrencyCode());
                pool.setCurrencyName(bill.getCurrencyName());
                pool.setStatementCreated(pool.getStatementCreated());
                pool.setAmount(BigDecimal.ZERO);
                if (CollectionUtils.isNotEmpty(list)) {
                    BigDecimal taxIncludedAmount = BigDecimal.ZERO;
                    for (FinancialCostBillLine line : list) {
                        taxIncludedAmount = CalculateUtil.add(taxIncludedAmount, line.getTaxIncludedAmount());
                    }
                    pool.setAmount(taxIncludedAmount);
                }
                BigDecimal fEntryTaxRate = list.get(0).getTaxRate();
                pool.setTaxRate(fEntryTaxRate);
                boolean includeTax = Objects.nonNull(fEntryTaxRate) && fEntryTaxRate.compareTo(BigDecimal.ZERO) > 0;
                pool.setHaveTax(includeTax);
                pool.setConfirmTime(bill.getConfirmTime());
                settlementPoolService.updateById(pool);

                List<FinancialStatementPoolRel> list1 = financialStatementPoolRelService.list(Wrappers.<FinancialStatementPoolRel>query()
                        .lambda().eq(FinancialStatementPoolRel::getSettlementPoolId, pool.getId()));

                if(CollectionUtils.isNotEmpty(list1)){
                    statementId = list1.get(0).getStatementId();
                }
            }

            if(Objects.nonNull(statementId)){
                FinancialCostDetail one = costDetailService.getOne(Wrappers.<FinancialCostDetail>query()
                        .lambda().eq(FinancialCostDetail::getCostBillId, bill.getId())
                        .eq(FinancialCostDetail::getIsDeleted, 0));
                FinancialCostDetail costDetail = new FinancialCostDetail();

                BigDecimal costIncludingTax = new BigDecimal(0);
                BigDecimal costNotIncludingTax = new BigDecimal(0);
                BigDecimal costTaxAmount = new BigDecimal(0);
                for(FinancialCostBillLine billLine : list){
                    costIncludingTax = CalculateUtil.add(costIncludingTax, billLine.getTaxIncludedAmount());
                    costNotIncludingTax = CalculateUtil.add(costNotIncludingTax, billLine.getNotTaxAmount());
                    costTaxAmount = CalculateUtil.add(costTaxAmount, billLine.getTaxAmount());
                }
                costDetail.setStatementId(statementId);
                costDetail.setDocumentTypeCode(bill.getCostBillType());
                costDetail.setDocumentTypeName("费用单");
                costDetail.setErpBillId(bill.getErpBillId());
                costDetail.setErpBillNo(bill.getErpBillNo());
                costDetail.setCostBillId(bill.getId());
                costDetail.setBillNo(bill.getCostBillNo());
                costDetail.setCostAmount(bill.getAllAmount());
                costDetail.setCheckIncludeTax(costIncludingTax);
                costDetail.setCheckNotTaxAmount(costNotIncludingTax);
                costDetail.setCheckTaxAmount(costTaxAmount);
                costDetail.setRemark(bill.getRemark());
                costDetail.setItemNo(costDetailService.queryNextItemNo(statementId));
                if(Objects.nonNull(one)){
                    costDetail.setId(one.getId());
                    costDetail.setCustomerConfirm(one.getCustomerConfirm());
                    costDetail.setCostRemark(one.getCostRemark());
                    costDetailService.updateById(costDetail);
                }else {
                    costDetailService.save(costDetail);
                    financialStatementPoolRelService.batchSave(Collections.singleton(pool.getId()),statementId);
                }
                applicationEventPublisher.publishEvent(new StatementDetailChangeEvent(this, statementId));
            }
        }catch (Exception e){
            log.debug("费用单{}更新报错:{}",changeEvent.getBillId(), ThrowableUtils.toString(e));
        }





    }


}
