package net.bncloud.delivery.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.bncloud.base.BaseEntity;

import javax.validation.constraints.NotNull;
import java.util.List;


/**
 * @author ddh
 * @description
 * @since 2022/5/23
 */
@TableName("t_order_delivery_supplier")
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDeliverySupplier extends BaseEntity {


    /**
     * 供应商信息表
     */
    @ApiModelProperty(value = "供应商信息表")
    private Long supplierId;

    /**
     * 供应商名称
     */
    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    /**
     * 启动状态 0false-未启用 1true-启用 (弃用)
     */
    @ApiModelProperty(value = "启动状态 0-未启用 1-启用")
    private Boolean status;


    /**
     *供应商编码
     */
    private String supplierCode;

    /**
     *供应商关系(有多个,以json字符串存放)
     */
    private List<SupplierType> supplierType;

    /**
     *关联的物料分组的id
     */
    private Long materialId;

    /**
     *关联的物料分组的erpid
     */
    private Long materialErpId;

    /**
     *关联的物料分组的erpparentid
     */
    private Long materialErpParentId;

    /**
     *关联的物料分组的编码
     */
    private String materialErpNumber;

    /**
     *分组名字
     */
    private String materialErpName;

    /**
     *关联的物料组的父组的id
     */
    private Long parentId;

    /**
     *关联的物料组的父组的erpid
     */
    private Long parentErpId;

    /**
     *关联的物料组的父组的物料分组编码
     */
    private String parentErpNumber;

    /**
     *关联的物料组的父组的物料分组名字
     */
    private String parentName;

    @Data
    @NoArgsConstructor
    public static class SupplierType{
        /**
         * supplierType
         * */
        private Long id;
        /**
         * 供应商类型
         * */
        private String item;

    }


}