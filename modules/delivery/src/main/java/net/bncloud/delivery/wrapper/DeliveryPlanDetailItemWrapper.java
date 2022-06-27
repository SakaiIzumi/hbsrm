package net.bncloud.delivery.wrapper;


import net.bncloud.delivery.entity.DeliveryPlanDetailItem;
import net.bncloud.delivery.vo.DeliveryPlanDetailItemVo;
import net.bncloud.common.util.BeanUtil;
import net.bncloud.support.BaseEntityWrapper;


/**
 * <p>
 * 送货计划明细批次表
 * </p>
 *包装类,返回视图层所需的字段
 * @author Auto-generator
 * @since 2022-01-17
 */
public class DeliveryPlanDetailItemWrapper extends BaseEntityWrapper<DeliveryPlanDetailItem,DeliveryPlanDetailItemVo>  {

	public static DeliveryPlanDetailItemWrapper build() {
		return new DeliveryPlanDetailItemWrapper();
	}

	@Override
	public DeliveryPlanDetailItemVo entityVO(DeliveryPlanDetailItem entity) {
        DeliveryPlanDetailItemVo entityVo = BeanUtil.copy(entity, DeliveryPlanDetailItemVo.class);
		return entityVo;
	}



}
