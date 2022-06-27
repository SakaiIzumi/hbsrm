package net.bncloud.quotation.param;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 供应商需求附件清单请求参数
 */
@Data
public class SupplierAttRequireParam implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 供应商id
     */
    private String supplierId;
    /**
     * 供应商名称
     */
    private String supplierName;

    /**
     * 询价单id
     */
    @NotNull(message = "询价单id不能为空")
    private String quotationBaseId;

    /**
     * 文件类型
     */
    private String fileType;
}
