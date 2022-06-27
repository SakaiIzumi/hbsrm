package net.bncloud.delivery.wrapper;


import net.bncloud.delivery.entity.DeliveryCustomsInformation;
import net.bncloud.delivery.vo.DeliveryCustomsInformationVo;
import net.bncloud.common.util.BeanUtil;
import net.bncloud.support.BaseEntityWrapper;


/**
 * <p>
 * 报关资料信息表
 * </p>
 *包装类,返回视图层所需的字段
 * @author huangtao
 * @since 2021-03-17
 */
public class DeliveryCustomsInformationWrapper extends BaseEntityWrapper<DeliveryCustomsInformation,DeliveryCustomsInformationVo>  {

	public static DeliveryCustomsInformationWrapper build() {
		return new DeliveryCustomsInformationWrapper();
	}

	@Override
	public DeliveryCustomsInformationVo entityVO(DeliveryCustomsInformation entity) {
        DeliveryCustomsInformationVo entityVo = BeanUtil.copy(entity, DeliveryCustomsInformationVo.class);
		return entityVo;
	}



}
