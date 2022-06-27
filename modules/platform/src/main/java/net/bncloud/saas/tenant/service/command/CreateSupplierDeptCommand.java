package net.bncloud.saas.tenant.service.command;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class CreateSupplierDeptCommand {
    //    private Long orgId;
    private Long supplierId;
    private String code;
    private String name;


    private final List<SupplierManager> managers = new ArrayList<>();

    public static CreateSupplierDeptCommand of(Long supplierId, String code, String name, List<SupplierManager> managers) {
        final CreateSupplierDeptCommand command = new CreateSupplierDeptCommand(supplierId, code, name);
        command.getManagers().addAll(managers);
        return command;
    }

    public static CreateSupplierDeptCommand of(Long supplierId, String code, String name) {
        return new CreateSupplierDeptCommand(supplierId, code, name);
    }
//    public static CreateSupplierDeptCommand of(Long orgId, Long supplierId, String code, String name, List<SupplierManager> managers) {
//        final CreateSupplierDeptCommand command = new CreateSupplierDeptCommand(orgId, supplierId, code, name);
//        command.getManagers().addAll(managers);
//        return command;
//    }
//
//    public static CreateSupplierDeptCommand of(Long orgId, Long supplierId, String code, String name) {
//        return new CreateSupplierDeptCommand(orgId, supplierId, code, name);
//    }
}
