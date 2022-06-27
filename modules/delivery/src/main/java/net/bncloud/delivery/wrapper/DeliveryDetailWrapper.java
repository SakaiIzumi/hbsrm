package net.bncloud.delivery.wrapper;


import net.bncloud.delivery.entity.DeliveryDetail;
import net.bncloud.delivery.vo.DeliveryDetailVo;
import net.bncloud.common.util.BeanUtil;
import net.bncloud.support.BaseEntityWrapper;


/**
 * <p>
 * 送货明细表
 * </p>
 *包装类,返回视图层所需的字段
 * @author huangtao
 * @since 2021-03-17
 */
public class DeliveryDetailWrapper extends BaseEntityWrapper<DeliveryDetail,DeliveryDetailVo>  {

	public static DeliveryDetailWrapper build() {
		return new DeliveryDetailWrapper();
	}

	@Override
	public DeliveryDetailVo entityVO(DeliveryDetail entity) {
        DeliveryDetailVo entityVo = BeanUtil.copy(entity, DeliveryDetailVo.class);
		return entityVo;
	}



}
