package net.bncloud.delivery.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.bncloud.delivery.entity.SupplierDeliveryConfig;

/**
 * desc:
 *
 * @author Rao
 * @Date 2022/06/20
 **/
@Accessors( chain = true)
@EqualsAndHashCode(callSuper = true)
@Data
public class SupplierDeliveryConfigVo extends SupplierDeliveryConfig {
    private static final long serialVersionUID = -5894477454099147000L;
}
