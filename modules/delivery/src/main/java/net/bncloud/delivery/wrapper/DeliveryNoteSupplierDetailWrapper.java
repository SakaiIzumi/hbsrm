package net.bncloud.delivery.wrapper;

import net.bncloud.common.util.BeanUtil;
import net.bncloud.delivery.entity.DeliveryNoteSupplier;
import net.bncloud.delivery.entity.DeliveryNoteSupplierDetail;
import net.bncloud.delivery.vo.DeliveryNoteSupplierDetailVo;
import net.bncloud.delivery.vo.DeliveryNoteSupplierVo;
import net.bncloud.support.BaseEntityWrapper;

public class DeliveryNoteSupplierDetailWrapper extends BaseEntityWrapper<DeliveryNoteSupplierDetail, DeliveryNoteSupplierDetailVo> {

    public static DeliveryNoteSupplierDetailWrapper build() {
        return new DeliveryNoteSupplierDetailWrapper();
    }

    @Override
    public DeliveryNoteSupplierDetailVo entityVO(DeliveryNoteSupplierDetail entity) {
        DeliveryNoteSupplierDetailVo entityVo = BeanUtil.copy(entity, DeliveryNoteSupplierDetailVo.class);
        return entityVo;
    }
}
