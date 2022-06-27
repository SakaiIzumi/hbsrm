package net.bncloud.delivery.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.delivery.entity.DeliveryNoteSupplier;
import net.bncloud.delivery.param.DeliveryNoteSupplierParam;

import java.util.List;

public interface DeliveryNoteSupplierMapper extends BaseMapper<DeliveryNoteSupplier>{
    List<DeliveryNoteSupplier> selectListPage(IPage<DeliveryNoteSupplier> page, QueryParam<DeliveryNoteSupplierParam> pageParam);
}
