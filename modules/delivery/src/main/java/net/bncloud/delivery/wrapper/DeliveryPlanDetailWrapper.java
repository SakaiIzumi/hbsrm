package net.bncloud.delivery.wrapper;


import net.bncloud.delivery.entity.DeliveryPlanDetail;
import net.bncloud.delivery.vo.DeliveryPlanDetailVo;
import net.bncloud.common.util.BeanUtil;
import net.bncloud.support.BaseEntityWrapper;


/**
 * <p>
 * 
 * </p>
 *包装类,返回视图层所需的字段
 * @author Auto-generator
 * @since 2022-01-17
 */
public class DeliveryPlanDetailWrapper extends BaseEntityWrapper<DeliveryPlanDetail,DeliveryPlanDetailVo>  {

	public static DeliveryPlanDetailWrapper build() {
		return new DeliveryPlanDetailWrapper();
	}

	@Override
	public DeliveryPlanDetailVo entityVO(DeliveryPlanDetail entity) {
        DeliveryPlanDetailVo entityVo = BeanUtil.copy(entity, DeliveryPlanDetailVo.class);
		return entityVo;
	}



}
