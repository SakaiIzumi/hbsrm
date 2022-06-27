package net.bncloud.delivery.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.bncloud.base.BaseEntity;

/**
 * desc: 工厂信息表
 * 1、若有关联关系不可删除
 *
 * @author Rao
 * @Date 2022/05/09
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("t_factory_info")
public class FactoryInfo extends BaseEntity {
    private static final long serialVersionUID = -8199703327487742313L;

    /**
     * 编码
     */
    private String number;

    /**
     * 工厂名称
     */
    private String name;

    /**
     * 工厂类型  收货厂/发货厂   现阶段来说， 采购方只有 收货厂类型， 而 供应商方是 发货厂类型 收货厂receipt/发货厂delivery
     */
    private String type;

    /**
     * 所属类型  采购方/供应方
     */
    private String belongType;

    /**
     * 所属方编码： 采购方编码/供应商编码
     */
    private String belongCode;

    /**
     * 所属方名称：采购方名称/供应商名称
     */
    private String belongName;

    /**
     * 所在区域
     */
    private String area;

    /**
     * 详细地址
     */
    private String detailedAddress;

    /**
     * 地址经纬度
     */
    private String addressLongitudeLatitude;
    /**
     * 省
     */
    private String province;
    /**
     * 市
     */
    private String city;
    /**
     * 区
     */
    private String district;
    /**
     * 街道
     */
    private String street;

}
