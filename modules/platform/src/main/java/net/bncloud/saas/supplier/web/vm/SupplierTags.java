package net.bncloud.saas.supplier.web.vm;

import lombok.Getter;
import lombok.Setter;
import net.bncloud.saas.supplier.domain.SupplierTag;
//import net.bncloud.supplier.domain.SupplierTag;

import java.util.List;

@Getter
@Setter
public class SupplierTags {
    private Long supplierId;
    private List<String> tags;
    private List<SupplierTag> supplierTagList;


}
