package net.bncloud.delivery.param;

import lombok.Data;
import net.bncloud.delivery.entity.DeliveryNoteSupplier;
import net.bncloud.delivery.entity.DeliveryPlanDetailItem;

import java.io.Serializable;

/**
 * <p>
 * 送货单信息列表查询条件
 * </p>
 *
 * @author liyh
 * @since 2021-1-19
 */
@Data
public class DeliveryNoteSupplierParam extends DeliveryNoteSupplier implements Serializable {

    private static final long serialVersionUID = 1L;

}
