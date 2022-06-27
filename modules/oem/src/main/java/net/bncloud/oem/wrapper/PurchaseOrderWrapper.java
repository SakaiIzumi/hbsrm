package net.bncloud.oem.wrapper;


import net.bncloud.common.util.BeanUtil;
import net.bncloud.oem.domain.entity.PurchaseOrder;
import net.bncloud.oem.domain.vo.PurchaseOrderVo;
import net.bncloud.support.BaseEntityWrapper;


/**
 *
 * 采购订单单扩展信息 转换包装类
 *
 *包装类,返回视图层所需的字段
 * @author liyh
 * @since 2022-04-24
 */
public class PurchaseOrderWrapper extends BaseEntityWrapper<PurchaseOrder, PurchaseOrderVo> {

	public static PurchaseOrderWrapper build() {
		return new PurchaseOrderWrapper();
	}

	@Override
	public PurchaseOrderVo entityVO(PurchaseOrder entity) {
		PurchaseOrderVo entityVo = BeanUtil.copy(entity, PurchaseOrderVo.class);
		return entityVo;
	}



}
