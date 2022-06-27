package net.bncloud.oem.controller;

import net.bncloud.common.api.R;
import net.bncloud.oem.service.PurchaseOrderService;
import net.bncloud.oem.service.api.feign.OemPurchaseOrderFeignClient;
import net.bncloud.oem.service.api.vo.OemPurchaseOrderVo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * desc:
 *
 * @author Rao
 * @Date 2022/04/24
 **/
@RestController
@RequestMapping("sync")
public class PurchaseOrderFeignController implements OemPurchaseOrderFeignClient {

    @Resource
    private PurchaseOrderService purchaseOrderService;

    @Override
    public R<String> syncOemPurchaseOrder(List<OemPurchaseOrderVo> oemPurchaseOrderVoList) {
        purchaseOrderService.syncOemPurchaseOrder( oemPurchaseOrderVoList);
        return R.success();
    }
}
