package net.bncloud.delivery.wrapper;


import net.bncloud.delivery.entity.DeliveryPlan;
import net.bncloud.delivery.vo.DeliveryPlanVo;
import net.bncloud.common.util.BeanUtil;
import net.bncloud.support.BaseEntityWrapper;


/**
 * <p>
 * 送货计划基础信息表
 * </p>
 *包装类,返回视图层所需的字段
 * @author Auto-generator
 * @since 2022-01-15
 */
public class DeliveryPlanWrapper extends BaseEntityWrapper<DeliveryPlan,DeliveryPlanVo>  {

	public static DeliveryPlanWrapper build() {
		return new DeliveryPlanWrapper();
	}

	@Override
	public DeliveryPlanVo entityVO(DeliveryPlan entity) {
        DeliveryPlanVo entityVo = BeanUtil.copy(entity, DeliveryPlanVo.class);
		return entityVo;
	}



}
