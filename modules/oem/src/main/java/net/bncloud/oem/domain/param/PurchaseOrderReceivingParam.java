package net.bncloud.oem.domain.param;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.bncloud.oem.domain.entity.FileInfo;
import net.bncloud.oem.domain.entity.PurchaseOrderReceiving;

import java.io.Serializable;
import java.util.List;

/**
 * @author ddh
 * @description 批量提交
 * @since 2022/4/24
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PurchaseOrderReceivingParam extends PurchaseOrderReceiving implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 附件列表
     */
    private List<FileInfo> attachmentList;
}
