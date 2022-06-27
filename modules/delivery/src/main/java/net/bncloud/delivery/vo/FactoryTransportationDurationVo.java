package net.bncloud.delivery.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @author liyh
 * @description 工厂运输时长vo
 * @since 2022/5/16
 */
@Data
@Accessors(chain = true)
public class FactoryTransportationDurationVo  implements Serializable {

    /**
     * 运输时长id
     */
    private Long id;

    /**
     * 送货工厂id
     */
    private Long deliveryFactoryId;

    /**
     * 收货工厂id
     */
    private Long receiptFactoryId;
    /**
     * 采购方编码
     */
    private String purchaseCode;
    /**
     * 采购方名称
     */
    private String purchaseName;
    /**
     * 采购方工厂编码
     */
    private String receiptFactoryNumber;

    /**
     * 采购方工厂名称
     */
    private String receiptFactoryName;

    /**
     * 收货工厂详细地址
     */
    private String receiptFactoryDetailAddress;

    /**
     * 供应商编码
     */
    private String supplierCode;
    /**
     * 供应商名称
     */
    private String supplierName;
    /**
     * 送货工厂编码
     */
    private String deliveryFactoryNumber;
    /**
     * 送货工厂名称
     */
    private String deliveryFactoryName;

    /**
     * 所在区域
     */
    private String deliveryFactoryArea;
    /**
     * 送货工厂-省
     */
    private String deliveryFactoryProvince;
    /**
     * 送货工厂-市
     */
    private String deliveryFactoryCity;
    /**
     * 送货工厂-区
     */
    private String deliveryFactoryDistrict;
    /**
     * 送货工厂-街道
     */
    private String deliveryFactoryStreet;
    /**
     * 送货工厂详细地址
     */
    private String deliveryFactoryDetailAddress;

    /**
     * 运输方式
     */
    private String transportWay;
    /**
     * 运输时长
     */
    private String transportDuration;
    /**
     * 备注
     */
    private String remark;
    /**
     * areas 按前端要求 加这个字段
     */
    private List<String> areas;


}
