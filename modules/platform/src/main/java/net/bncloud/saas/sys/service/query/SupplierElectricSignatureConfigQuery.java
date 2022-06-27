package net.bncloud.saas.sys.service.query;

import lombok.Getter;
import lombok.Setter;
import net.bncloud.saas.sys.domain.ElectricSignatureConfigType;

@Getter
@Setter
public class SupplierElectricSignatureConfigQuery {


    private ElectricSignatureConfigType type;
    private String supplierCode;
    private String supplierName;

    /**
     * 供应商编码、名称，模糊搜索内容
     */
    private String qs;
}
