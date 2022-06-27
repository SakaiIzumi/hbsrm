package net.bncloud.service.api.delivery.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * desc:
 *
 * @author Rao
 * @Date 2022/06/20
 **/
@Data
public class SupplierDeliveryConfigVo implements Serializable {
    private static final long serialVersionUID = 5443051088722625885L;

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
