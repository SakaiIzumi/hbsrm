package net.bncloud.quotation.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.bncloud.api.feign.file.FileInfo;
import net.bncloud.quotation.entity.QuotationAttRequire;

import java.io.Serializable;
import java.util.List;

/**
 * @author ddh
 * @desc 供应商附件需求清单
 * @since 2022/4/7
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupplierAttachmentRequireVo extends QuotationAttRequire implements Serializable {
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
     * 需求清单的文件列表
     */
    private List<FileInfo> attRequireAttachments;
}
