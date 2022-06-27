package net.bncloud.financial.service;

import net.bncloud.base.BaseService;
import net.bncloud.financial.entity.FinancialDeliveryBill;
import net.bncloud.financial.param.FinancialDeliveryBillSaveParam;

import java.util.List;

/**
 * @author: liulu
 * @Date: 2022-02-28 10:47
 */
public interface FinancialDeliveryBillService extends BaseService<FinancialDeliveryBill> {

    /**
     * 批量新增送货单
     *
     * @param deliveryBillList
     */
    void batchSaveAccountDeliveryBill(List<FinancialDeliveryBillSaveParam> deliveryBillList);

    /**
     * 新增送货单
     *
     * @param deliveryBill
     */
    void saveAccountDeliveryBill(FinancialDeliveryBillSaveParam deliveryBill);
}
