package net.bncloud.oem.service.api.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * desc:
 *
 * @author Rao
 * @Date 2022/04/24
 **/
@Data
public class OemReceivingAddressVo implements Serializable {
    private static final long serialVersionUID = 2537638308329978492L;

    /**
     * 地址编码
     */
    private String code;

    /**
     * 地址
     */
    private String address;

    /**
     * oem供应商编码(如果有的话)
     */
    private String supplierCode;

    /**
     * oem供应商名字(如果有的话)
     */
    private String supplierName;

    /**
     * 数据来源：1用户维护，2用户导入，3ERP同步(如果有的话)
     */
    private String sourceType;

}
