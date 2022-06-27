package net.bncloud.saas.supplier.service.query;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class SupplierStaffQuery implements Serializable {

    private String name;
    private String code;
    private String mobile;
    private Long orgId;
    private Long supplierId;
    private Boolean enabled;
//    private String code;
    private String supplierCode;
}
