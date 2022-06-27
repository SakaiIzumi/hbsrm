package net.bncloud.oem.service;

import net.bncloud.bis.service.api.vo.PurchaseInStockOrderCreateVo;
import net.bncloud.common.api.R;
import net.bncloud.oem.domain.entity.PurchaseOrder;
import net.bncloud.oem.domain.entity.PurchaseOrderMaterial;
import net.bncloud.oem.domain.entity.PurchaseOrderReceiving;
import net.bncloud.oem.domain.vo.PurchaseOrderMaterialVo;
import net.bncloud.oem.domain.vo.PurchaseOrderReceivingVo;
import net.bncloud.oem.domain.vo.PurchaseOrderVo;

import java.util.List;

/**
 * @author liyh
 * @description 订单同步erp服务接口
 * @since 2022/4/24
 */
public interface CreatePurchaseInStockOrderService {
    void createPurchaseInStockOrder(Long id);
    PurchaseInStockOrderCreateVo buildVo(List<PurchaseOrderReceivingVo> PurchaseOrderReceiving, PurchaseOrderMaterialVo purchaseOrderMaterial, PurchaseOrderVo purchaseOrder );
    void batchCreateStockOrder(List<PurchaseInStockOrderCreateVo> purchaseInStockOrderCreateVoList,List<PurchaseOrderReceivingVo> purchaseOrderReceivingVoList);
    R<PurchaseInStockOrderCreateVo> batchCreateStockOrderV2(PurchaseInStockOrderCreateVo purchaseInStockOrderCreateVoList,PurchaseOrderReceivingVo purchaseOrderReceiving,PurchaseOrderVo purchaseOrder);
}

