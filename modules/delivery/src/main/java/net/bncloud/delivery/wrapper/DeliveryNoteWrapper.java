package net.bncloud.delivery.wrapper;


import net.bncloud.delivery.entity.DeliveryNote;
import net.bncloud.delivery.vo.DeliveryNoteVo;
import net.bncloud.common.util.BeanUtil;
import net.bncloud.support.BaseEntityWrapper;


/**
 * <p>
 * 送货单信息表
 * </p>
 *包装类,返回视图层所需的字段
 * @author huangtao
 * @since 2021-03-17
 */
public class DeliveryNoteWrapper extends BaseEntityWrapper<DeliveryNote,DeliveryNoteVo>  {

	public static DeliveryNoteWrapper build() {
		return new DeliveryNoteWrapper();
	}

	@Override
	public DeliveryNoteVo entityVO(DeliveryNote entity) {
        DeliveryNoteVo entityVo = BeanUtil.copy(entity, DeliveryNoteVo.class);
		return entityVo;
	}



}
