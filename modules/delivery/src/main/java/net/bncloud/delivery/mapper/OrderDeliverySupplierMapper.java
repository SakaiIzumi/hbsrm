package net.bncloud.delivery.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.delivery.entity.OrderDeliverySupplier;
import net.bncloud.delivery.param.OrderDeliverySupplierParam;
import net.bncloud.delivery.vo.OrderDeliverySupplierVo;
import net.bncloud.delivery.vo.SupplierItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author ddh
 * @since 2022/5/23
 * @description
 */
public interface OrderDeliverySupplierMapper extends BaseMapper<OrderDeliverySupplier> {

    /**
     * 分页使用
     * */
    List<OrderDeliverySupplierVo> selectPageList(IPage<OrderDeliverySupplierVo> page,@Param("queryParam") QueryParam<OrderDeliverySupplierParam> queryParam);
    /**
     * 非分页使用
     * */
    List<OrderDeliverySupplierVo> selectNoPageList(@Param("queryParam") QueryParam<OrderDeliverySupplierParam> queryParam);

    List<SupplierItem> selectTagItemListBySupplierId(@Param("id") Long id);

    List<SupplierItem> selectTypeItemListBySupplierId(@Param("id") Long id);
}