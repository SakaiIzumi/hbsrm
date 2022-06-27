package net.bncloud.delivery.wrapper;


import net.bncloud.common.util.BeanUtil;
import net.bncloud.delivery.entity.FactoryInfo;
import net.bncloud.delivery.entity.FactoryTransportationDuration;
import net.bncloud.delivery.vo.FactoryInfoVo;
import net.bncloud.delivery.vo.FactoryTransportationDurationVo;
import net.bncloud.support.BaseEntityWrapper;


/**
 * @author liyh
 * @description 工厂运输时长wrapper
 * @since 2022/5/16
 */
public class FactoryTransportationDurationWrapper extends BaseEntityWrapper<FactoryTransportationDuration, FactoryTransportationDurationVo>  {

	public static FactoryTransportationDurationWrapper build() {
		return new FactoryTransportationDurationWrapper();
	}

	@Override
	public FactoryTransportationDurationVo entityVO(FactoryTransportationDuration entity) {
		FactoryTransportationDurationVo entityVo = BeanUtil.copy(entity, FactoryTransportationDurationVo.class);
		return entityVo;
	}

}
