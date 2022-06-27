package net.bncloud.delivery.wrapper;


import net.bncloud.delivery.entity.DeliveryHarbor;
import net.bncloud.delivery.vo.DeliveryHarborVo;
import net.bncloud.common.util.BeanUtil;
import net.bncloud.support.BaseEntityWrapper;


/**
 * <p>
 * 港口信息表
 * </p>
 *包装类,返回视图层所需的字段
 * @author huangtao
 * @since 2021-03-17
 */
public class DeliveryHarborWrapper extends BaseEntityWrapper<DeliveryHarbor,DeliveryHarborVo>  {

	public static DeliveryHarborWrapper build() {
		return new DeliveryHarborWrapper();
	}

	@Override
	public DeliveryHarborVo entityVO(DeliveryHarbor entity) {
        DeliveryHarborVo entityVo = BeanUtil.copy(entity, DeliveryHarborVo.class);
		return entityVo;
	}



}
