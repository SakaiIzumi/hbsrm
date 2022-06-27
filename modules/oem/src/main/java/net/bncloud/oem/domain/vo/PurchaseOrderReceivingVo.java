package net.bncloud.oem.domain.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.bncloud.oem.domain.entity.FileInfo;
import net.bncloud.oem.domain.entity.PurchaseOrderReceiving;

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
public class PurchaseOrderReceivingVo extends PurchaseOrderReceiving implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 可操作按钮
     */
    private Map<String,Boolean> permissionButton;

    /**
     * 附件列表
     */
    private List<FileInfo> attachmentList;

}
