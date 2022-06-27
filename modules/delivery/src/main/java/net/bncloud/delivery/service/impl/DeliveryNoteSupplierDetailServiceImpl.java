package net.bncloud.delivery.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.base.BaseServiceImpl;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.delivery.entity.DeliveryNoteSupplier;
import net.bncloud.delivery.entity.DeliveryNoteSupplierDetail;
import net.bncloud.delivery.mapper.DeliveryNoteSupplierDetailMapper;
import net.bncloud.delivery.mapper.DeliveryNoteSupplierMapper;
import net.bncloud.delivery.param.DeliveryNoteSupplierDetailParam;
import net.bncloud.delivery.param.DeliveryNoteSupplierParam;
import net.bncloud.delivery.service.DeliveryNoteSupplierDetailService;
import net.bncloud.delivery.service.DeliveryNoteSupplierService;
import org.springframework.stereotype.Service;


@Service
public class DeliveryNoteSupplierDetailServiceImpl extends BaseServiceImpl<DeliveryNoteSupplierDetailMapper,DeliveryNoteSupplierDetail> implements DeliveryNoteSupplierDetailService{

    @Override
    public IPage<DeliveryNoteSupplierDetail> selectPage(IPage<DeliveryNoteSupplierDetail> page, QueryParam<DeliveryNoteSupplierDetailParam> pageParam) {
        return page.setRecords(baseMapper.selectListPage(page, pageParam));
    }
}
