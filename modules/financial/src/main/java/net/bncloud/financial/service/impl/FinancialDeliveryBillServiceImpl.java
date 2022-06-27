package net.bncloud.financial.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.base.BaseServiceImpl;
import net.bncloud.common.security.LoginInfo;
import net.bncloud.common.security.SecurityUtils;
import net.bncloud.event.publisher.DefaultEventPublisher;
import net.bncloud.financial.entity.FinancialDeliveryBill;
import net.bncloud.financial.entity.FinancialDeliveryBillLine;
import net.bncloud.financial.entity.FinancialSettlementPool;
import net.bncloud.financial.enums.FinancialBillTypeEnum;
import net.bncloud.financial.event.statement.DeliveryBillChangeEvent;
import net.bncloud.financial.mapper.FinancialDeliveryBillMapper;
import net.bncloud.financial.param.FinancialDeliveryBillSaveParam;
import net.bncloud.financial.service.FinancialDeliveryBillLineService;
import net.bncloud.financial.service.FinancialDeliveryBillService;
import net.bncloud.financial.service.FinancialSettlementPoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 * @author: liulu
 * @Date: 2022-02-28 10:53
 */
@Service
@Slf4j
public class FinancialDeliveryBillServiceImpl extends BaseServiceImpl<FinancialDeliveryBillMapper, FinancialDeliveryBill> implements FinancialDeliveryBillService {

    @Autowired
    private DefaultEventPublisher defaultEventPublisher;
    @Autowired
    private FinancialDeliveryBillLineService deliveryBillLineService;

    @Autowired
    private FinancialSettlementPoolService settlementPoolService;

    @Override
    public void batchSaveAccountDeliveryBill(List<FinancialDeliveryBillSaveParam> deliveryBillList) {
        for (FinancialDeliveryBillSaveParam param : deliveryBillList) {
            saveAccountDeliveryBill(param);
        }
    }

    /**
     * 保存送货单
     *
     * @param deliveryBill 保存费用单的请求参数
     * @return 返回这个费用单
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveAccountDeliveryBill(FinancialDeliveryBillSaveParam deliveryBill) {
        log.info("保存送货单接收参数：{}", JSON.toJSONString(deliveryBill));

        if(updateDeliveryBill(deliveryBill)){
            log.info("该单号存在，已更新：{}", deliveryBill.getErpBillNo());
            return;
        }
        //送货单信息
        deliveryBill.setId(null);
        if (SecurityUtils.getLoginInfo().isPresent()) {
            LoginInfo loginInfo = SecurityUtils.getLoginInfo().get();
            deliveryBill.setCreatedBy(loginInfo.getId());
        }
        save(deliveryBill);

        //送货明细
        List<FinancialDeliveryBillLine> deliveryBillLineList = deliveryBill.getDeliveryBillLineList();
        deliveryBillLineList.forEach(s -> {
            s.setDeliveryBillId(deliveryBill.getId().toString());
            deliveryBillLineService.save(s);
        });
        //放入结算池
        FinancialSettlementPool financialSettlementPool = buildFinancialSettlementPool(deliveryBill);
        settlementPoolService.save(financialSettlementPool);
    }

    private FinancialSettlementPool buildFinancialSettlementPool(FinancialDeliveryBillSaveParam deliveryBill) {

        FinancialSettlementPool settlementPool = new FinancialSettlementPool();
        settlementPool.setErpBillId(deliveryBill.getErpBillId());
        settlementPool.setBillId(deliveryBill.getId());
        settlementPool.setErpBillType(deliveryBill.getErpBillType());
        settlementPool.setErpBillNo(deliveryBill.getErpBillNo());
        settlementPool.setBillNo(deliveryBill.getDeliveryBillNo());
        settlementPool.setDeliveryDate(deliveryBill.getDeliveryDate());
        settlementPool.setDeliveryNum(deliveryBill.getDeliveryNum());
        settlementPool.setBillType(FinancialBillTypeEnum.DELIVERY.name());
        settlementPool.setCustomerCode(deliveryBill.getCustomerCode());
        settlementPool.setCustomerName(deliveryBill.getCustomerName());
        settlementPool.setSupplierCode(deliveryBill.getSupplierCode());
        settlementPool.setSupplierName(deliveryBill.getSupplierName());
        settlementPool.setCurrencyCode(deliveryBill.getCurrencyCode());
        settlementPool.setCurrencyName(deliveryBill.getCurrencyName());
        settlementPool.setStatementCreated("N");
        settlementPool.setAmount(deliveryBill.getDeliveryAmount());
        FinancialDeliveryBillLine billLine = deliveryBill.getDeliveryBillLineList().get(0);
        BigDecimal fEntryTaxRate = billLine.getTaxRate();
        settlementPool.setTaxRate(fEntryTaxRate);
        boolean includeTax = Objects.nonNull(fEntryTaxRate) && fEntryTaxRate.compareTo(BigDecimal.ZERO) > 0;
        settlementPool.setHaveTax(includeTax);
        settlementPool.setConfirmTime(deliveryBill.getSigningTime());
        return settlementPool;
    }

    private boolean updateDeliveryBill(FinancialDeliveryBillSaveParam deliveryBill){
        FinancialDeliveryBill bill = getOne(Wrappers.<FinancialDeliveryBill>query().lambda().eq(FinancialDeliveryBill::getErpBillId, deliveryBill.getErpBillId()));
        if(Objects.isNull(bill)){
            return false;
        }
        if (SecurityUtils.getLoginInfo().isPresent()) {
            LoginInfo loginInfo = SecurityUtils.getLoginInfo().get();
            deliveryBill.setCreatedBy(loginInfo.getId());
        }
        deliveryBill.setId(bill.getId());
        deliveryBill.setCustomerCode(bill.getCustomerCode());
        deliveryBill.setCustomerName(bill.getCustomerName());
        deliveryBill.setSupplierCode(bill.getSupplierCode());
        deliveryBill.setSupplierName(bill.getSupplierName());
        deliveryBill.setDeliveryBillNo(bill.getDeliveryBillNo());
        updateById(deliveryBill);

        //费用明细
        List<FinancialDeliveryBillLine> costBillLineList = deliveryBill.getDeliveryBillLineList();
        costBillLineList.forEach(s -> {
            FinancialDeliveryBillLine one = deliveryBillLineService.getOne(Wrappers.<FinancialDeliveryBillLine>query()
                    .lambda().eq(FinancialDeliveryBillLine::getErpLineId, s.getErpLineId()));
            s.setDeliveryBillId(bill.getId().toString());
            if(Objects.nonNull(one)){
                s.setId(one.getId());
                deliveryBillLineService.updateById(s);
            }else {
                s.setId(null);
                deliveryBillLineService.save(s);
            }
        });
        applicationEventPublisher.publishEvent(new DeliveryBillChangeEvent(this,bill.getId()));
        return true;
    }
}
