package net.bncloud.delivery.param;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * @author ddh
 * @version 1.0.0
 * @description 供需平衡报表  请求参数
 * @since 2022/2/24
 */
@Data
public class SupplyDemandBalanceParam implements Serializable {


    /**
     *产品编码/产品名称
     */
    @Length(min = 0,max = 255)
    private String product;

    /**
     * 商家编码
     */
    @Length(min = 0,max = 255)
    private String merchantCode;

    /**
     * 供应商编码/供应商名称
     */
    @Length(min = 0,max = 255)
    private String supplier;

    /**
     * 缺料风险：
     * 0否（供需结余为非负数）
     * 1是（供需结余为负数）
     */
    private String risk;


}
