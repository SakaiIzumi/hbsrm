package net.bncloud.delivery.remote;

import net.bncloud.common.api.R;
import net.bncloud.delivery.service.DeliveryNoteService;
import net.bncloud.delivery.service.impl.DeliveryPlanManager;
import net.bncloud.service.api.delivery.dto.DeliveryDetailUpdateDTO;
import net.bncloud.service.api.delivery.dto.DeliveryMaterialNoticeDTO;
import net.bncloud.service.api.delivery.dto.DeliveryPlanDTO;
import net.bncloud.service.api.delivery.dto.SyncOrgIdParams;
import net.bncloud.service.api.delivery.feign.DeliveryPlanFeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *
 * 送货feign接口
 * @author Toby
 */
@RestController
public class DeliveryRemoteController  implements DeliveryPlanFeignClient {

    private final DeliveryPlanManager deliveryPlanManager;

    private final DeliveryNoteService deliveryNoteService;

    public DeliveryRemoteController(DeliveryPlanManager deliveryPlanManager, DeliveryNoteService deliveryNoteService) {
        this.deliveryPlanManager = deliveryPlanManager;
        this.deliveryNoteService = deliveryNoteService;
    }

    /**
     * 接收送货计划列表
     *
     * @param deliveryPlanDTOList 送货计划列表
     * @return 处理结果
     */
    @Override
    public R<String> receiveDeliveryPlans(List<DeliveryPlanDTO> deliveryPlanDTOList) {
        deliveryPlanManager.receiveDeliveryPlans(deliveryPlanDTOList);
        return R.success();
    }

    /**
     * 根据送货单ID，封装收料通知单
     *
     * @param deliveryId 送货单ID
     * @return 返回结果
     */
    @Override
    public R<DeliveryMaterialNoticeDTO> wrapMaterialNotice(Long deliveryId) {
        return R.data(deliveryNoteService.wrapMaterialNotice(deliveryId));
    }

    /**
     * 批量更新收货单信息（收货数量及状态）
     *
     * @param deliveryNoteUpdateDTOList 送货单列表
     * @return 返回结果
     */
    @Override
    public R syncErpDeliveryNoteInfos(List<DeliveryDetailUpdateDTO> deliveryNoteUpdateDTOList) {
        deliveryPlanManager.syncErpDeliveryNoteInfos(deliveryNoteUpdateDTOList);
        return R.success();
    }

    /**
     * 更新送货计划的组织ID
     * @param syncOrgIdParams
     * @return
     */
    @Override
    public R<Object> updateDeliveryPlanOrgId(SyncOrgIdParams syncOrgIdParams) {
        deliveryPlanManager.updateDeliveryPlanOrgId(syncOrgIdParams);
        return R.success();
    }
}
