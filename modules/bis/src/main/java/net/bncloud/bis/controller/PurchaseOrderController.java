package net.bncloud.bis.controller;

import net.bncloud.bis.manager.ExperimentSupplierManager;
import net.bncloud.bis.manager.PurchaseOrderManager;
import net.bncloud.bis.service.api.feign.PurchaseOrderFeignClient;
import net.bncloud.common.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * desc: 采购订单
 *
 * @author Rao
 * @Date 2022/01/21
 **/
@RestController
public class PurchaseOrderController implements PurchaseOrderFeignClient {

    @Autowired
    private PurchaseOrderManager purchaseOrderManager;
    @Autowired
    private ExperimentSupplierManager experimentSupplierManager;

    /**
     * 同步数据
     * @return
     */
    @Override
    public R<Object> syncData() {
        experimentSupplierManager.experimentSupplierSyncPurchaseOrder(null);
        return R.success();
    }

    @Override
    public R<Object> syncOrder() {
        experimentSupplierManager.experimentSupplierSyncPurchaseOrder(null);
        return R.success();
    }

    @Override
    public R<Object> syncDeliveryPlan() {
        experimentSupplierManager.experimentSupplierSyncPurchaseOrder(null);
        return R.success();
    }
}
