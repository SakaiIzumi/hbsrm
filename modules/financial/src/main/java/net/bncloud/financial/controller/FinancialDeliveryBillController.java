package net.bncloud.financial.controller;

import io.swagger.annotations.ApiOperation;
import net.bncloud.common.api.R;
import net.bncloud.financial.param.FinancialDeliveryBillSaveParam;
import net.bncloud.financial.service.FinancialDeliveryBillService;
import net.bncloud.financial.service.FinancialOperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 对账模块-供应商送货单（采购方端）
 */
@RestController
@RequestMapping("/financial/delivery_bill")
public class FinancialDeliveryBillController {

    @Autowired
    private FinancialDeliveryBillService deliveryBillService;


    @Autowired
    private FinancialOperationLogService operationLogService;


    /**
     * 批量新增送货单信息
     *
     * @param paramList
     * @return
     */
    @PostMapping("/batchSave")
    @ApiOperation(value = "批量新增送货单信息")
    public R<Void> batchSave(@RequestBody List<FinancialDeliveryBillSaveParam> paramList) {
        deliveryBillService.batchSaveAccountDeliveryBill(paramList);
        return R.success();
    }

    /**
     * 新增送货单
     *
     * @param deliveryBillSaveParam
     * @return
     */
    @PostMapping("/save")
    @ApiOperation(value = "新增送货单")
    public R<Void> save(@RequestBody @Validated FinancialDeliveryBillSaveParam deliveryBillSaveParam) {
        deliveryBillService.saveAccountDeliveryBill(deliveryBillSaveParam);
        return R.success();
    }


}
