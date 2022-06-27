package net.bncloud.service.api.delivery.feign;


import net.bncloud.common.api.R;
import net.bncloud.security.client.AuthorizedFeignClient;
import net.bncloud.service.api.delivery.dto.DeliveryDetailUpdateDTO;
import net.bncloud.service.api.delivery.dto.DeliveryMaterialNoticeDTO;
import net.bncloud.service.api.delivery.dto.DeliveryPlanDTO;
import net.bncloud.service.api.delivery.dto.SyncOrgIdParams;
import net.bncloud.service.api.delivery.feign.fallback.DeliveryPlanFeignClientFallbackFactory;
import net.bncloud.service.api.delivery.feign.fallback.OrderDeliverySupplierFeignClientFallbackFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author Toby
 */
@AuthorizedFeignClient(name = "delivery", path = "/delivery", contextId = "orderDeliveryPlanClient", fallbackFactory = OrderDeliverySupplierFeignClientFallbackFactory.class)
public interface OrderDeliverySupplierFeignClient {

    /**
     * 根据当前排程供应商查询拥有的物料编码
     */
    @PostMapping("/zc/orderDelivery/selectCode")
    R<List<String>> selectOrderDeliverySupplierProductCode(@RequestBody Long supplierId);



}
