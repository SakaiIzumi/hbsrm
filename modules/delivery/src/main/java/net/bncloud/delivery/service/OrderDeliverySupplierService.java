package net.bncloud.delivery.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.base.BaseService;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.delivery.entity.OrderDeliverySupplier;
import net.bncloud.delivery.param.OrderDeliverySupplierParam;
import net.bncloud.delivery.vo.OrderDeliverySupplierVo;
import org.springframework.data.domain.PageImpl;

import java.util.List;

/**
 * @author ddh
 * @description
 * @since 2022/5/23
 */
public interface OrderDeliverySupplierService extends BaseService<OrderDeliverySupplier> {

    void deleteBatch(List<Long> ids);

    void saveSuppliers(OrderDeliverySupplierParam param);

    PageImpl<OrderDeliverySupplierVo> selectPage(IPage<OrderDeliverySupplierVo> page, QueryParam<OrderDeliverySupplierParam> queryParam);

    Boolean isOrderDeliverySupplier();
}
