package net.bncloud.bis.controller;

import net.bncloud.bis.manager.PurchaseReceiveBillOrderManager;
import net.bncloud.bis.service.api.dto.PurchaseReceiveBillCreateOrderDto;
import net.bncloud.bis.service.api.feign.PurchaseReceiveBillOrderFeignClient;
import net.bncloud.bis.service.api.vo.PurchaseReceiveBillCallCreateVo;
import net.bncloud.common.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * desc: 收料通知单
 *
 * @author Rao
 * @Date 2022/01/22
 **/
@RestController
public class PurchaseReceiveBillController implements PurchaseReceiveBillOrderFeignClient {

    @Autowired
    private PurchaseReceiveBillOrderManager purchaseReceiveBillOrderManager;

    @Override
    public R<PurchaseReceiveBillCallCreateVo> createPurchaseReceiveBillOrder(PurchaseReceiveBillCreateOrderDto purchaseReceiveBillCreateOrderDto) {
        return purchaseReceiveBillOrderManager.createReceiveBillOrder(purchaseReceiveBillCreateOrderDto);
    }
}
