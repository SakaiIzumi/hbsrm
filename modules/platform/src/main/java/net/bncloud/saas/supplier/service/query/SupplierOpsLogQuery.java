package net.bncloud.saas.supplier.service.query;

import lombok.Data;

import java.io.Serializable;

@Data
public class SupplierOpsLogQuery implements Serializable {

    private Long supplierId;
}
