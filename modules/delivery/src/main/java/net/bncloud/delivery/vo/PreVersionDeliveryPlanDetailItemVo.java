package net.bncloud.delivery.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author ddh
 * @description 上一版本的送货数量
 * @since 2022/6/13
 */
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
@Accessors(chain = true)
public class PreVersionDeliveryPlanDetailItemVo implements Serializable {

    private static final long serialVersionUID = 7597367009653612064L;
    /**
     * 采购方编码
     */
    private String purchaseCode;
    /**
     * 供应商编码是
     */
    private String supplierCode;
    /**
     * 产品编码
     */
    private String productCode;
    /**
     * 预计到货日期
     */
    private LocalDateTime deliveryDate;
    /**
     * 建议发货日期
     */
    private LocalDate suggestedDeliveryDate;
    /**
     * 送货数量
     */
    private BigDecimal deliveryQuantity;

    /**
     * 构建分组key  采购方+供应商+物料编码+预计到货日期+建议发货日期
     * @return
     */
    public String buildGroupKey(){
        return purchaseCode+supplierCode+productCode+deliveryDate.toLocalDate().toString()+suggestedDeliveryDate.toString();
    }

}
