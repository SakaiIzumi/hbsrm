package net.bncloud.common.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Supplier {
    private Long supplierId;
    private String supplierName;
    private String supplierCode;


    public static Supplier of(Long supplierId, String supplierName, String supplierCode) {
        return new Supplier(supplierId, supplierName, supplierCode);
    }

}
