package net.bncloud.delivery.wrapper;


import net.bncloud.common.util.BeanUtil;
import net.bncloud.delivery.entity.FactoryInfo;
import net.bncloud.delivery.entity.FactoryWorkday;
import net.bncloud.delivery.vo.FactoryInfoVo;
import net.bncloud.delivery.vo.FactoryWorkdayVo;
import net.bncloud.support.BaseEntityWrapper;


/**
 * @author liyh
 * @description 工厂工作日wrapper
 * @since 2022/5/16
 */
public class FactoryWorkdayWrapper extends BaseEntityWrapper<FactoryWorkday, FactoryWorkdayVo>  {

	public static FactoryWorkdayWrapper build() {
		return new FactoryWorkdayWrapper();
	}

	@Override
	public FactoryWorkdayVo entityVO(FactoryWorkday entity) {
		FactoryWorkdayVo entityVo = BeanUtil.copy(entity, FactoryWorkdayVo.class);
		return entityVo;
	}

}
