package net.bncloud.saas.event;

import lombok.Data;


@Data
public class CreateSupplierManager {
    private String name;
    private String mobile;
    private Long supplierId;
    private boolean enabled;


    public CreateSupplierManager(String name, String mobile, Long supplierId, boolean enabled) {
        this.name = name;
        this.mobile = mobile;
        this.supplierId = supplierId;
        this.enabled = enabled;
    }

    public static CreateSupplierManager of(String name, String mobile, Long supplierId, boolean enabled) {
        return new CreateSupplierManager(name, mobile, supplierId, enabled);
    }
}
