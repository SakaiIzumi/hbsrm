package net.bncloud.delivery.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.bncloud.base.BaseEntity;

/**
 * desc: 供应商送货配置
 *   虽然很怪，先这样用，具体呢，静哥设计；存储每个供应商的配置。
 *   现在有送货协同的  自动导入法定节假日安排配置
 *
 * @author Rao
 * @Date 2022/06/20
 **/
@Accessors( chain = true)
@TableName("t_supplier_delivery_config")
@EqualsAndHashCode(callSuper = true)
@Data
public class SupplierDeliveryConfig extends BaseEntity {
    private static final long serialVersionUID = -3097642123134960471L;

    /**
     * 供应商编码
     */
    private String supplierCode;

    /**
     * 供应商配置key
     */
    private String code;

    /**
     * 供应商配置值
     */
    private String value;

    /**
     * 描述
     */
    private String remark;

}
