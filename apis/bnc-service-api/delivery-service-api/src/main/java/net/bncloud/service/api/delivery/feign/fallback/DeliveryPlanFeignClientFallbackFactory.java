package net.bncloud.service.api.delivery.feign.fallback;


import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.common.api.R;
import net.bncloud.service.api.delivery.dto.*;
import net.bncloud.service.api.delivery.feign.DeliveryPlanFeignClient;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Toby
 */
@Component
@Slf4j
public class DeliveryPlanFeignClientFallbackFactory implements FallbackFactory<DeliveryPlanFeignClient> {
    @Override
    public DeliveryPlanFeignClient create(Throwable throwable) {
        return new DeliveryPlanFeignClient() {
            /**
             * 接收送货计划列表
             *
             * @param deliveryPlanDTOList 送货计划列表
             * @return 处理结果
             */
            @Override
            public R receiveDeliveryPlans(List<DeliveryPlanDTO> deliveryPlanDTOList) {
                log.error("接收送货计划列表异常",throwable);
                return R.fail("收发货服务暂时不可用，接收送货计划列表失败，请稍后再试");
            }

            /**
             * 根据送货单ID，封装收料通知单
             *
             * @param deliveryId 送货单ID
             * @return 返回结果
             */
            @Override
            public R<DeliveryMaterialNoticeDTO> wrapMaterialNotice(Long deliveryId) {
                log.error("根据送货单ID，封装收料通知单",throwable);
                return R.fail("收发货服务暂时不可用，根据送货单ID，封装收料通知单失败，请稍后再试");
            }

            /**
             * 批量更新收货单信息（收货数量及状态）
             *
             * @param deliveryNoteUpdateDTOList 收货单列表
             * @return 返回结果
             */
            @Override
            public R syncErpDeliveryNoteInfos(List<DeliveryDetailUpdateDTO> deliveryNoteUpdateDTOList)  {
                log.error("批量更新收货单信息",throwable);
                return R.fail("收发货服务暂时不可用，批量更新收货单信息失败，请稍后再试!");
            }

            @Override
            public R<Object> updateDeliveryPlanOrgId(SyncOrgIdParams syncOrgIdParams) {
                log.error("更新送货计划数据的组织ID",throwable);
                return R.fail("收发货服务暂时不可用，更新送货计划数据的组织ID失败，请稍后再试!");
            }


        };
    }
}
