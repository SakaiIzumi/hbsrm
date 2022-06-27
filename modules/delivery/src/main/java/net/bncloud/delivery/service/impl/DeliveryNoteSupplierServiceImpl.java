package net.bncloud.delivery.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.base.BaseServiceImpl;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.delivery.entity.DeliveryNoteSupplier;
import net.bncloud.delivery.mapper.DeliveryNoteSupplierMapper;
import net.bncloud.delivery.param.DeliveryNoteSupplierParam;
import net.bncloud.delivery.service.DeliveryNoteSupplierService;
import org.springframework.stereotype.Service;


@Service
public class DeliveryNoteSupplierServiceImpl extends BaseServiceImpl<DeliveryNoteSupplierMapper,DeliveryNoteSupplier> implements DeliveryNoteSupplierService {

    @Override
    public IPage<DeliveryNoteSupplier> selectPage(IPage<DeliveryNoteSupplier> page, QueryParam<DeliveryNoteSupplierParam> pageParam) {
        return page.setRecords(baseMapper.selectListPage(page, pageParam));
    }
}
