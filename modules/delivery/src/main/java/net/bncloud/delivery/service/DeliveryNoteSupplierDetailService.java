package net.bncloud.delivery.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.base.BaseService;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.delivery.entity.DeliveryNoteSupplier;
import net.bncloud.delivery.entity.DeliveryNoteSupplierDetail;
import net.bncloud.delivery.param.DeliveryNoteSupplierDetailParam;
import net.bncloud.delivery.param.DeliveryNoteSupplierParam;

public interface DeliveryNoteSupplierDetailService extends BaseService<DeliveryNoteSupplierDetail> {

    IPage<DeliveryNoteSupplierDetail> selectPage(IPage<DeliveryNoteSupplierDetail> page, QueryParam<DeliveryNoteSupplierDetailParam> pageParam);

}
