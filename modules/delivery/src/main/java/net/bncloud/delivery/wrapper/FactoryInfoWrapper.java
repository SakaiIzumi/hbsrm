package net.bncloud.delivery.wrapper;


import net.bncloud.common.util.BeanUtil;
import net.bncloud.delivery.entity.FactoryInfo;
import net.bncloud.delivery.vo.FactoryInfoVo;
import net.bncloud.support.BaseEntityWrapper;


/**
 * @author liyh
 * @description 工厂主数据wrapper
 * @since 2022/5/16
 */
public class FactoryInfoWrapper extends BaseEntityWrapper<FactoryInfo, FactoryInfoVo>  {

	public static FactoryInfoWrapper build() {
		return new FactoryInfoWrapper();
	}

	@Override
	public FactoryInfoVo entityVO(FactoryInfo entity) {
		FactoryInfoVo entityVo = BeanUtil.copy(entity, FactoryInfoVo.class);
		return entityVo;
	}

}
