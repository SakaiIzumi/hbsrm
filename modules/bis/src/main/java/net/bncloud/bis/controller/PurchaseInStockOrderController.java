package net.bncloud.bis.controller;

import net.bncloud.bis.manager.ExperimentSupplierManager;
import net.bncloud.bis.manager.PurchaseInStockOrderManager;
import net.bncloud.bis.service.api.feign.PurchaseInStockOrderFeignClient;
import net.bncloud.bis.service.api.vo.PurchaseInStockOrderCreateVo;
import net.bncloud.common.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * desc: 采购入库单同步
 *
 * @author Rao
 * @Date 2022/02/08
 **/
@RestController
public class PurchaseInStockOrderController implements PurchaseInStockOrderFeignClient {

    @Autowired
    private PurchaseInStockOrderManager purchaseInStockOrderManager;
    @Autowired
    private ExperimentSupplierManager experimentSupplierManager;

    @Override
    public R<Object> syncData() {
        experimentSupplierManager.experimentSupplierSyncInStockOrder( null);
        return R.success();
    }

    @Override
    public R<String> createPurchaseInStockOrder(PurchaseInStockOrderCreateVo purchaseInStockOrderCreateVo) {
        return purchaseInStockOrderManager.createPurchaseInStockOrder(purchaseInStockOrderCreateVo);
    }

    @Override
    public R<List<String>> batchCreatePurchaseInStockOrder(List<PurchaseInStockOrderCreateVo> purchaseInStockOrderCreateVoList) {
        return purchaseInStockOrderManager.batchCreatePurchaseInStockOrder(purchaseInStockOrderCreateVoList);
    }


}
