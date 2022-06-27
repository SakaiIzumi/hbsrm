package net.bncloud.delivery.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import net.bncloud.delivery.entity.DeliveryCustomsInformation;
import net.bncloud.delivery.mapper.DeliveryCustomsInformationMapper;
import net.bncloud.delivery.service.DeliveryCustomsInformationService;
import net.bncloud.support.Condition;
import org.springframework.stereotype.Service;
import net.bncloud.base.BaseServiceImpl;

/**
 * <p>
 * 报关资料信息表 服务实现类
 * </p>
 *
 * @author huangtao
 * @since 2021-03-17
 */
@Service
public class DeliveryCustomsInformationServiceImpl extends BaseServiceImpl<DeliveryCustomsInformationMapper, DeliveryCustomsInformation> implements DeliveryCustomsInformationService {


    /**
     * 更具送货单id查询
     * @author wangyifan
     * @date 2021/5/6
     * @param deliveryId 送货单id
     * @return net.bncloud.delivery.entity.DeliveryCustomsInformation
    */
    @Override
    public DeliveryCustomsInformation getByDeliveryId(Long deliveryId) {
        DeliveryCustomsInformation deliveryCustomsInformationParam = DeliveryCustomsInformation.builder().deliveryId(deliveryId).build();
        QueryWrapper<DeliveryCustomsInformation> deliveryCustomsInformationQueryWrapper = Condition.getQueryWrapper(deliveryCustomsInformationParam);
        DeliveryCustomsInformation deliveryCustomsInformation = this.getOne(deliveryCustomsInformationQueryWrapper);
        return deliveryCustomsInformation;
    }
}
