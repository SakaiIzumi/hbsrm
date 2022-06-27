package net.bncloud.oem.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.bncloud.oem.domain.entity.PurchaseOrderMaterial;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author ddh
 * @description
 * @since 2022/4/24
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PurchaseOrderMaterialVo extends PurchaseOrderMaterial implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 物料收货状态
     * 1待收货：已收货数量=0
     * 2部分收货：0 < 已收货数量 < 订单数量
     * 3已收货：已收货数量 ≥ 订单数量
     */
    @ApiModelProperty(value = "收货状态:1待收货，2部分收货，3已收货")
    private String materialReceiptStatus;

    /**
     * 收货按钮
     */
    private Map<String,Boolean> receiptButton;
    /**
     * 冗余字段：采购单号
     *
     * 适应前端,改个名字
     */
    private String purchaseOrderNum;

    /**
     * 冗余字段：采购单号
     * 销售工作台用
     */
    private String purchaseOrderCode;

    /**
     * 收货集合
     */
    private List<PurchaseOrderReceivingVo> children;

    /**
     * 适应前端的操作按钮,第二层(物料不需要查询备注按钮)
     */
    private Map<String,Boolean> permissionButton;
}
