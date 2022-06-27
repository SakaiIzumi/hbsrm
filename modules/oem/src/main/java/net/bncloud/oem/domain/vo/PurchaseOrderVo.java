package net.bncloud.oem.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.bncloud.oem.domain.entity.PurchaseOrder;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


/**
 * @author liyh
 * @since 2022/4/24
 * @description
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PurchaseOrderVo extends PurchaseOrder implements Serializable {
    private static final long serialVersionUID = 5415540040122744985L;

    /**
     * 采购订单id（冗余字段）
     */
    private Long purchaseOrderId;

    /**
     * 物料集合
     */
    private List<PurchaseOrderMaterialVo> children;

    /**
     * 送货地址
     */
    @ApiModelProperty(value="送货地址")
    private String address;

    /**
     * 收货地址集合(第三层) 创建erp物料采购订单的时候需要使用
     */
    private List<PurchaseOrderReceivingVo> receivingChildren;

    /**
     * 适应前端的操作按钮,第一层(采购订单不需要查询备注按钮)
     */
    private Map<String,Boolean> permissionButton;

    /**
     * 标记第一层的字段
     */
    private String purchaseOneLayer;
}