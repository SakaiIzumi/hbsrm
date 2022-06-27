package net.bncloud.financial.event.statement.listener;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.common.util.CalculateUtil;
import net.bncloud.common.util.ThrowableUtils;
import net.bncloud.financial.entity.*;
import net.bncloud.financial.enums.FinancialBillTypeEnum;
import net.bncloud.financial.event.statement.DeliveryBillChangeEvent;
import net.bncloud.financial.event.statement.StatementDetailChangeEvent;
import net.bncloud.financial.service.*;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author Toby
 */
@Component
@Slf4j
public class DeliveryBillChangeEventListener {

    private final FinancialDeliveryBillService deliveryBillService;

    private final FinancialDeliveryBillLineService deliveryBillLineService;

    private final FinancialSettlementPoolService settlementPoolService;

    private final FinancialStatementService statementService;

    private final FinancialDeliveryDetailService deliveryDetailService;

    private final FinancialStatementPoolRelService financialStatementPoolRelService;

    @Resource
    private ApplicationEventPublisher applicationEventPublisher;

    public DeliveryBillChangeEventListener(FinancialDeliveryBillLineService deliveryBillLineService, FinancialSettlementPoolService settlementPoolService, FinancialStatementService statementService, FinancialDeliveryDetailService deliveryDetailService, FinancialDeliveryBillService deliveryBillService, FinancialStatementPoolRelService financialStatementPoolRelService) {
        this.deliveryBillLineService = deliveryBillLineService;
        this.settlementPoolService = settlementPoolService;
        this.statementService = statementService;
        this.deliveryDetailService = deliveryDetailService;
        this.deliveryBillService = deliveryBillService;
        this.financialStatementPoolRelService = financialStatementPoolRelService;
    }

    @EventListener(DeliveryBillChangeEvent.class)
    public void statementDetailChangeListener(DeliveryBillChangeEvent changeEvent) {
        try {
            Long billId = changeEvent.getBillId();
            FinancialDeliveryBill bill = deliveryBillService.getById(billId);
            if (bill == null) {
                log.warn("未查到送货单信息");
                return;
            }

            Long statementId = null;
            List<FinancialDeliveryBillLine> list = deliveryBillLineService.list(Wrappers.<FinancialDeliveryBillLine>query()
                    .lambda().eq(FinancialDeliveryBillLine::getDeliveryBillId, bill.getId()));
            FinancialDeliveryBillLine deliveryBillLine = list.get(0);
            FinancialSettlementPool pool = settlementPoolService.getOne(Wrappers.<FinancialSettlementPool>query()
                    .lambda().eq(FinancialSettlementPool::getErpBillId, bill.getErpBillId()));
            if(Objects.nonNull(pool)){
                pool.setErpBillId(bill.getErpBillId());
                pool.setBillId(bill.getId());
                pool.setErpBillType(bill.getErpBillType());
                pool.setErpBillNo(bill.getErpBillNo());
                pool.setBillNo(bill.getDeliveryBillNo());
                pool.setDeliveryDate(bill.getDeliveryDate());
                pool.setDeliveryNum(bill.getDeliveryNum());
                pool.setBillType(FinancialBillTypeEnum.DELIVERY.name());
                pool.setCurrencyCode(bill.getCurrencyCode());
                pool.setCurrencyName(bill.getCurrencyName());
                pool.setStatementCreated(pool.getStatementCreated());
                if(org.apache.commons.collections4.CollectionUtils.isNotEmpty(list)){
                    BigDecimal taxIncludedAmount = BigDecimal.ZERO;
                    for(FinancialDeliveryBillLine line : list){
                        taxIncludedAmount = CalculateUtil.add(taxIncludedAmount, line.getTaxIncludedAmount());
                    }
                    pool.setAmount(taxIncludedAmount);
                }
                BigDecimal fEntryTaxRate = deliveryBillLine.getTaxRate();
                pool.setTaxRate(fEntryTaxRate);
                boolean includeTax = Objects.nonNull(fEntryTaxRate) && fEntryTaxRate.compareTo(BigDecimal.ZERO) > 0;
                pool.setHaveTax(includeTax);
                pool.setConfirmTime(bill.getSigningTime());
                settlementPoolService.updateById(pool);

                List<FinancialStatementPoolRel> list1 = financialStatementPoolRelService.list(Wrappers.<FinancialStatementPoolRel>query()
                        .lambda().eq(FinancialStatementPoolRel::getSettlementPoolId, pool.getId()));

                if(CollectionUtils.isNotEmpty(list1)){
                    statementId = list1.get(0).getStatementId();
                }
            }

            if(Objects.nonNull(statementId)){
                FinancialDeliveryDetail one = deliveryDetailService.getOne(Wrappers.<FinancialDeliveryDetail>query()
                        .lambda().eq(FinancialDeliveryDetail::getDeliveryBillId, bill.getId())
                        .eq(FinancialDeliveryDetail::getIsDeleted, 0));

                FinancialDeliveryDetail deliveryDetail = new FinancialDeliveryDetail();
                BigDecimal shipmentIncludingTax = new BigDecimal(0);
                BigDecimal shipmentNotTax = new BigDecimal(0);
                BigDecimal shipmentTaxAmount = new BigDecimal(0);
                BigDecimal taxRate = new BigDecimal(0);
                boolean haveTax = false;

                for(FinancialDeliveryBillLine billLine : list){
                    shipmentIncludingTax = CalculateUtil.add(shipmentIncludingTax, billLine.getTaxIncludedAmount());
                    shipmentNotTax = CalculateUtil.add(shipmentNotTax, billLine.getNotTaxAmount());
                    shipmentTaxAmount = CalculateUtil.add(shipmentTaxAmount, billLine.getTaxAmount());
                    taxRate = billLine.getTaxRate();
                    haveTax = billLine.getHaveTax();
                }
                deliveryDetail.setStatementId(statementId);
                deliveryDetail.setDeliveryBillId(bill.getId());
                deliveryDetail.setBillNo(bill.getDeliveryBillNo());
                deliveryDetail.setErpBillId(bill.getErpBillId());
                deliveryDetail.setErpBillNo(bill.getErpBillNo());
                deliveryDetail.setDeliveryNum(bill.getDeliveryNum());
                deliveryDetail.setDeliveryDate(bill.getDeliveryDate());
                deliveryDetail.setDeliveryAmount(bill.getDeliveryAmount());
                deliveryDetail.setConfirmedAmount(bill.getDeliveryAmount());
                deliveryDetail.setHaveTax(haveTax);
                deliveryDetail.setTaxRate(taxRate);
                deliveryDetail.setCheckAmount(bill.getDeliveryAmount());
                deliveryDetail.setCheckQuantity(bill.getDeliveryNum());
                deliveryDetail.setCheckIncludeTax(shipmentIncludingTax);
                deliveryDetail.setCheckNotTaxAmount(shipmentNotTax);
                deliveryDetail.setCheckTaxAmount(shipmentTaxAmount);
                deliveryDetail.setRemark(bill.getRemark());
                deliveryDetail.setItemNo(deliveryDetailService.queryNextItemNo(statementId));
                if(Objects.nonNull(one)){
                    deliveryDetail.setId(one.getId());
                    deliveryDetail.setCustomerConfirm(one.getCustomerConfirm());
                    deliveryDetail.setDeliveryRemark(one.getDeliveryRemark());
                    deliveryDetailService.updateById(deliveryDetail);
                }else {
                    deliveryDetailService.save(deliveryDetail);
                    financialStatementPoolRelService.batchSave(Collections.singleton(pool.getId()),statementId);
                }
                applicationEventPublisher.publishEvent(new StatementDetailChangeEvent(this, statementId));
            }


        }catch (Exception e){
            log.debug("送货单{}更新报错:{}",changeEvent.getBillId(), ThrowableUtils.toString(e));
        }


    }


}
