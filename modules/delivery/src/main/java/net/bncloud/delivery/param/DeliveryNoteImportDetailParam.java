package net.bncloud.delivery.param;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * desc: 送货单导入明细param
 *
 * @author Rao
 * @Date 2022/03/09
 **/
@Data
public class DeliveryNoteImportDetailParam implements Serializable {
    private static final long serialVersionUID = 2715176033441061350L;

    /**
     * 送货单ID
     */
    @NotNull(message = "请先保存送货单")
    private Long deliveryNoteId;

}
