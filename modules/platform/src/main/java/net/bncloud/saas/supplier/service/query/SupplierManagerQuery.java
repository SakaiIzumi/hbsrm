package net.bncloud.saas.supplier.service.query;

import lombok.Data;

import java.io.Serializable;

@Data
public class SupplierManagerQuery implements Serializable {

    private String userCode;

    private String mobile;

    private String userName;

    private Long supplierId;

    private Long orgId;
}
