package net.bncloud.delivery.wrapper;


import net.bncloud.common.util.BeanUtil;
import net.bncloud.delivery.entity.FactoryInfo;
import net.bncloud.delivery.entity.FactoryVacation;
import net.bncloud.delivery.vo.FactoryInfoVo;
import net.bncloud.delivery.vo.FactoryVacationVo;
import net.bncloud.support.BaseEntityWrapper;


/**
 * @author liyh
 * @description 工厂假期wrapper
 * @since 2022/5/16
 */
public class FactoryVacationWrapper extends BaseEntityWrapper<FactoryVacation, FactoryVacationVo>  {

	public static FactoryVacationWrapper build() {
		return new FactoryVacationWrapper();
	}

	@Override
	public FactoryVacationVo entityVO(FactoryVacation entity) {
		FactoryVacationVo entityVo = BeanUtil.copy(entity, FactoryVacationVo.class);
		return entityVo;
	}

}
