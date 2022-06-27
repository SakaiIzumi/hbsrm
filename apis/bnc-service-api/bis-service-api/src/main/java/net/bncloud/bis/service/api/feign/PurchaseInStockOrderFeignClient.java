package net.bncloud.bis.service.api.feign;

import net.bncloud.bis.service.api.fallbackfactory.PurchaseInStockOrderFeignClientFallbackFactory;
import net.bncloud.bis.service.api.fallbackfactory.PurchaseOrderFeignClientFallbackFactory;
import net.bncloud.bis.service.api.vo.PurchaseInStockOrderCreateVo;
import net.bncloud.common.api.R;
import net.bncloud.security.client.AuthorizedFeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

/**
 * desc: 采购入库单同步
 *
 * @author Rao
 * @Date 2022/02/08
 **/
@AuthorizedFeignClient(name = "bis",path = "bis", contextId = "purchaseInStockOrderFeignClient", fallbackFactory = PurchaseInStockOrderFeignClientFallbackFactory.class)
public interface PurchaseInStockOrderFeignClient {

    @PostMapping("purchaseInStockOrder/syncData")
    R<Object> syncData();

    /**
     * 创建采购入库单
     * @return
     */
    @PostMapping("purchaseInStockOrder/createPurchaseInStockOrder")
    R<String> createPurchaseInStockOrder(@RequestBody @Validated @Valid PurchaseInStockOrderCreateVo purchaseInStockOrderCreateVo);

    /**
     * 批量创建采购入库单
     * @return
     */
    @PostMapping("purchaseInStockOrder/batchCreatePurchaseInStockOrder")
    R<List<String>> batchCreatePurchaseInStockOrder(@RequestBody @Validated @Valid List<PurchaseInStockOrderCreateVo> purchaseInStockOrderCreateVoList);

}
