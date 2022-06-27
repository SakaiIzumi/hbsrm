package net.bncloud.delivery.param;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.bncloud.delivery.entity.OrderDeliverySupplier;

import java.io.Serializable;
import java.util.List;

/**
 * @author ddh
 * @description
 * @since 2022/5/23
 */
@Data
public class OrderDeliverySupplierParam extends OrderDeliverySupplier implements Serializable {

    /**
     * 用户选择的供应商列表
     * */
    List<OrderDeliverySupplier> supplierList;
    /**
     * 用户选择的物料列表
     * */
    List<OrderDeliverySupplier> materialList;
    /**
     * 高级查询使用  供应商类型字段
     * */
    private String types;


}
