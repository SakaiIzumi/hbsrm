package net.bncloud.financial.event.statement.listener;

import lombok.extern.slf4j.Slf4j;
import net.bncloud.common.util.CalculateUtil;
import net.bncloud.financial.entity.FinancialStatement;
import net.bncloud.financial.enums.StatementStatus;
import net.bncloud.financial.event.statement.StatementDetailChangeEvent;
import net.bncloud.financial.service.FinancialCostDetailService;
import net.bncloud.financial.service.FinancialDeliveryDetailService;
import net.bncloud.financial.service.FinancialStatementService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * @author Toby
 */
@Component
@Slf4j
public class StatementChangeEventListener {

    private final FinancialStatementService statementService;

    private final FinancialDeliveryDetailService deliveryDetailService;

    private final FinancialCostDetailService costDetailService;

    public StatementChangeEventListener(FinancialStatementService statementService, FinancialDeliveryDetailService deliveryDetailService, FinancialCostDetailService costDetailService) {
        this.statementService = statementService;
        this.deliveryDetailService = deliveryDetailService;
        this.costDetailService = costDetailService;
    }

    @EventListener(StatementDetailChangeEvent.class)
    public void statementDetailChangeListener(StatementDetailChangeEvent changeEvent) {
        Long statementId = changeEvent.getStatementId();
        FinancialStatement statement = statementService.getById(statementId);
        if (statement == null) {
            log.warn("未查询到对账单信息");
            return;
        }
        FinancialStatement deliveryAmountVo = deliveryDetailService.querySummaryAmountByStatementId(statementId);
        FinancialStatement costAmountVo = costDetailService.querySummaryAmountByStatementId(statementId);

        FinancialStatement statementUpdate = new FinancialStatement();
        statementUpdate.setId(statementId);
        statementUpdate.setShipmentIncludingTax(deliveryAmountVo.getShipmentIncludingTax());
        statementUpdate.setShipmentNotTax(deliveryAmountVo.getShipmentNotTax());
        statementUpdate.setShipmentTaxAmount(deliveryAmountVo.getShipmentTaxAmount());
        statementUpdate.setCostIncludingTax(costAmountVo.getCostIncludingTax());
        statementUpdate.setCostNotIncludingTax(costAmountVo.getCostNotIncludingTax());
        statementUpdate.setCostTaxAmount(costAmountVo.getCostTaxAmount());

        statementUpdate.setStatementIncludingTax(CalculateUtil.add(costAmountVo.getCostIncludingTax(), deliveryAmountVo.getShipmentIncludingTax()));
        statementUpdate.setStatementNotTax(CalculateUtil.add(costAmountVo.getCostNotIncludingTax(), deliveryAmountVo.getShipmentNotTax()));
        statementUpdate.setStatementTaxAmount(CalculateUtil.add(costAmountVo.getCostTaxAmount(), deliveryAmountVo.getShipmentTaxAmount()));

        //如果对账单已经确认
        List<StatementStatus> statusList = Arrays.asList(StatementStatus.CONFIRMED,StatementStatus.TO_BE_CUSTOMER_CONFIRM, StatementStatus.TO_BE_SUPPLIER_CONFIRM);
        if (statusList.contains(StatementStatus.getTypeByCode(statement.getStatementStatus()))) {
            statementUpdate.setCheckShipmentAmount(statementUpdate.getShipmentIncludingTax());
            statementUpdate.setCheckCostAmount(statementUpdate.getCostIncludingTax());
            statementUpdate.setCheckStatementIncludingTax(statementUpdate.getStatementIncludingTax());
            statementUpdate.setCheckStatementNotTax(statementUpdate.getStatementNotTax());
            statementUpdate.setCheckStatementTaxAmount(statementUpdate.getStatementTaxAmount());
        }

        statementService.updateById(statementUpdate);
    }


}
