package net.bncloud.oem.wrapper;


import net.bncloud.common.util.BeanUtil;
import net.bncloud.oem.domain.entity.PurchaseOrder;
import net.bncloud.oem.domain.entity.ReceivingAddress;
import net.bncloud.oem.domain.vo.ReceivingAddressVo;
import net.bncloud.support.BaseEntityWrapper;


/**
 *
 * 采购订单单扩展信息 转换包装类
 *
 *包装类,返回视图层所需的字段
 * @author liyh
 * @since 2022-04-24
 */
public class ReceivingAddressWrapper extends BaseEntityWrapper<ReceivingAddress, ReceivingAddressVo> {

	public static ReceivingAddressWrapper build() {
		return new ReceivingAddressWrapper();
	}

	@Override
	public ReceivingAddressVo entityVO(ReceivingAddress entity) {
		ReceivingAddressVo entityVo = BeanUtil.copy(entity, ReceivingAddressVo.class);
		return entityVo;
	}



}
