package net.bncloud.service.api.delivery.feign;


import net.bncloud.common.api.R;
import net.bncloud.security.client.AuthorizedFeignClient;
import net.bncloud.service.api.delivery.dto.DeliveryDetailUpdateDTO;
import net.bncloud.service.api.delivery.dto.DeliveryMaterialNoticeDTO;
import net.bncloud.service.api.delivery.dto.DeliveryPlanDTO;
import net.bncloud.service.api.delivery.dto.SyncOrgIdParams;
import net.bncloud.service.api.delivery.feign.fallback.DeliveryPlanFeignClientFallbackFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author Toby
 */
@AuthorizedFeignClient(name = "delivery", path = "/delivery", contextId = "deliveryPlanClient", fallbackFactory = DeliveryPlanFeignClientFallbackFactory.class)
public interface DeliveryPlanFeignClient {

    /**
     * 接收送货计划列表
     *
     * @param deliveryPlanDTOList 送货计划列表
     * @return 处理结果
     */
    @PostMapping("/zc/delivery-plan/sync/receiveDeliveryPlans")
    R<String> receiveDeliveryPlans(@RequestBody List<DeliveryPlanDTO> deliveryPlanDTOList);


    /**
     * 根据送货单ID，封装收料通知单
     *
     * @param deliveryId 送货单ID
     * @return 返回结果
     */
    @PostMapping("/delivery-note/wrapMaterialNotice")
    R<DeliveryMaterialNoticeDTO> wrapMaterialNotice(@RequestBody Long deliveryId);

    /**
     * 批量更新收货单信息（收货数量及状态）
     *
     * @param deliveryNoteUpdateDTOList 送货单列表
     * @return 返回结果
     */
    @PostMapping("/delivery/delivery-note/syncErpDeliveryNoteInfos")
    R syncErpDeliveryNoteInfos(@RequestBody List<DeliveryDetailUpdateDTO> deliveryNoteUpdateDTOList);

    /**
     * 更新送货计划数据的组织ID
     * @return
     */
    @PostMapping("delivery-note/updateDeliveryPlanOrgId")
    R<Object> updateDeliveryPlanOrgId(@RequestBody SyncOrgIdParams syncOrgIdParams);

}
