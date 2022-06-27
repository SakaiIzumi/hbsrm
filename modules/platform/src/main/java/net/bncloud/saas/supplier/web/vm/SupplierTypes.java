package net.bncloud.saas.supplier.web.vm;

import lombok.Getter;
import lombok.Setter;
import net.bncloud.saas.supplier.domain.SupplierType;
//import net.bncloud.supplier.domain.SupplierType;

import java.util.List;

@Getter
@Setter
public class SupplierTypes {
    private Long supplierId;
    private List<String> types;
    private List<SupplierType> supplierTypeList;
}
