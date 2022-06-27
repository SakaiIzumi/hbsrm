package net.bncloud.service.api.delivery.feign;


import net.bncloud.common.api.R;
import net.bncloud.security.client.AuthorizedFeignClient;
import net.bncloud.service.api.delivery.dto.DeliveryNoteDTO;
import net.bncloud.service.api.delivery.feign.fallback.SupplierDeliveryNoteFeignClientFallbackFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@AuthorizedFeignClient(name = "delivery", contextId = "SupplierDeliveryNoteFeignClient", fallbackFactory = SupplierDeliveryNoteFeignClientFallbackFactory.class, decode404 = true)
public interface SupplierDeliveryNoteFeignClient {

    /**
     * 根据对账模块的收料通知号查询送货服务送货单号
     * @return 返回单号
     */
    @PostMapping("/delivery/zy/delivery-note/vs/getDeliveryNoteNo")
    R<DeliveryNoteDTO> getDeliveryNoteNo(@RequestBody String fNumber);
}
