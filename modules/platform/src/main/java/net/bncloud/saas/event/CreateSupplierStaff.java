package net.bncloud.saas.event;

import lombok.Data;

import java.util.List;

@Data
public class CreateSupplierStaff {
    private String name;
    private String mobile;
    private boolean enabled;
    private Long supplierId;
    private List<Long> roleIds;


    public CreateSupplierStaff(String name, String mobile, Long supplierId, List<Long> roleIds, boolean enabled) {
        this.name = name;
        this.mobile = mobile;
        this.supplierId = supplierId;
        this.roleIds = roleIds;
        this.enabled = enabled;
    }

    public static CreateSupplierStaff of(String name, String mobile, Long supplierId, List<Long> roleIds, boolean enabled) {
        return new CreateSupplierStaff(name, mobile, supplierId, roleIds, enabled);
    }
}
