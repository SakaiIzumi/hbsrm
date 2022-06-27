package net.bncloud.delivery.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.base.BaseService;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.delivery.entity.DeliveryNoteSupplier;
import net.bncloud.delivery.param.DeliveryNoteSupplierParam;

public interface DeliveryNoteSupplierService extends BaseService<DeliveryNoteSupplier> {

    IPage<DeliveryNoteSupplier> selectPage(IPage<DeliveryNoteSupplier> page, QueryParam<DeliveryNoteSupplierParam> pageParam);

}
