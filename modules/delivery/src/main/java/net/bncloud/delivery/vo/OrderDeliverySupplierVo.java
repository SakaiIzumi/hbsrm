package net.bncloud.delivery.vo;

import lombok.Data;
import net.bncloud.delivery.entity.OrderDeliverySupplier;

import java.io.Serializable;
import java.util.List;

/**
 * @author ddh
 * @description
 * @since 2022/5/24
 */
@Data
public class OrderDeliverySupplierVo extends OrderDeliverySupplier implements Serializable {

//    private Long orderDeliverySupplierId;

    /**
     * 供应商id
     */
//    private Long supplierId;
    /**
     * 供应商名称
     */
//    private String supplierName;
    /**
     * 启动状态 1-启用 0-未启用
     */
//    private Boolean status;

    /**
     * 物料类型：0非成品类，1成品类
     */
//    private int materialType;

    /**
     * 供应商标签
     */
    List<SupplierItem> tagItems;
    /**
     * 供应商类型
     */
    List<SupplierItem> typeItems;


}
