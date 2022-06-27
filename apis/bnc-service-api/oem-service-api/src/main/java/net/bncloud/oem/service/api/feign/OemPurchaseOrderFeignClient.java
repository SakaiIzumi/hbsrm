package net.bncloud.oem.service.api.feign;

import net.bncloud.common.api.R;
import net.bncloud.oem.service.api.fallbackfactory.OemPurchaseOrderFeignClientFallbackFactory;
import net.bncloud.oem.service.api.vo.OemPurchaseOrderVo;
import net.bncloud.security.client.AuthorizedFeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * desc: OEM订单FeignClient
 *
 * @author Rao
 * @Date 2022/04/24
 **/
@AuthorizedFeignClient(name = "oem",path = "/oem/sync", contextId = "oemPurchaseOrderFeignClient", fallbackFactory = OemPurchaseOrderFeignClientFallbackFactory.class)
public interface OemPurchaseOrderFeignClient {

    /**
     * 同步Oem甲供物料收货订单
     */
    @PostMapping("syncOemPurchaseOrder")
    R<String> syncOemPurchaseOrder(@RequestBody List<OemPurchaseOrderVo> oemPurchaseOrderVoList);

}
