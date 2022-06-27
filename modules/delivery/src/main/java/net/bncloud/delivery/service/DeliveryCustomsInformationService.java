package net.bncloud.delivery.service;

import net.bncloud.delivery.entity.DeliveryCustomsInformation;
import net.bncloud.base.BaseService;


/**
 * <p>
 * 报关资料信息表 服务类
 * </p>
 *
 * @author huangtao
 * @since 2021-03-17
 */
public interface DeliveryCustomsInformationService extends BaseService<DeliveryCustomsInformation> {


    DeliveryCustomsInformation getByDeliveryId(Long deliveryId);
}
