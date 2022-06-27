package net.bncloud.delivery.wrapper;

import net.bncloud.common.util.BeanUtil;
import net.bncloud.delivery.entity.DeliveryNoteSupplier;
import net.bncloud.delivery.vo.DeliveryNoteSupplierVo;
import net.bncloud.support.BaseEntityWrapper;

public class DeliveryNoteSupplierWrapper extends BaseEntityWrapper<DeliveryNoteSupplier, DeliveryNoteSupplierVo> {

    public static DeliveryNoteSupplierWrapper build() {
        return new DeliveryNoteSupplierWrapper();
    }

    @Override
    public DeliveryNoteSupplierVo entityVO(DeliveryNoteSupplier entity) {
        DeliveryNoteSupplierVo entityVo = BeanUtil.copy(entity, DeliveryNoteSupplierVo.class);
        return entityVo;
    }
}
