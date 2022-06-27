package net.bncloud.delivery.wrapper;


import net.bncloud.delivery.entity.DeliveryOperationLog;
import net.bncloud.delivery.vo.DeliveryOperationLogVo;
import net.bncloud.common.util.BeanUtil;
import net.bncloud.support.BaseEntityWrapper;


/**
 * <p>
 * 送货通知操作记录信息表
 * </p>
 *包装类,返回视图层所需的字段
 * @author huangtao
 * @since 2021-03-17
 */
public class DeliveryOperationLogWrapper extends BaseEntityWrapper<DeliveryOperationLog,DeliveryOperationLogVo>  {

	public static DeliveryOperationLogWrapper build() {
		return new DeliveryOperationLogWrapper();
	}

	@Override
	public DeliveryOperationLogVo entityVO(DeliveryOperationLog entity) {
        DeliveryOperationLogVo entityVo = BeanUtil.copy(entity, DeliveryOperationLogVo.class);
		return entityVo;
	}



}
