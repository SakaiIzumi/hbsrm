package net.bncloud.order.entity;

import lombok.Builder;
import lombok.Data;
import net.bncloud.order.constants.ElectricSignatureConfigType;

/**
 * 供应商电子签章配置
 */

@Data
@Builder
public class SupplierElectricSignatureConfig  {

    private Long id;
    private Long companyId;
    private ElectricSignatureConfigType type;
    private boolean enabled;
}
