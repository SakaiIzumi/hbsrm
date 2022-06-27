package net.bncloud.saas.event.listener;

import lombok.AllArgsConstructor;
import net.bncloud.saas.authorize.domain.Role;
import net.bncloud.saas.authorize.service.RoleService;
import net.bncloud.saas.event.CreateSupplierStaff;
import net.bncloud.saas.event.SupplierStaffCreateEvent;
import net.bncloud.saas.supplier.domain.SupplierStaff;
import net.bncloud.saas.supplier.service.SupplierStaffService;
import net.bncloud.saas.tenant.domain.vo.UserId;
import net.bncloud.saas.user.domain.UserInfo;
import net.bncloud.saas.user.service.UserInfoService;
import net.bncloud.saas.user.service.command.UserCreateCommand;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class SupplierStaffCreateEventListener {

    private final SupplierStaffService supplierStaffService;
    private final UserInfoService userInfoService;
    private final RoleService roleService;

    @EventListener(SupplierStaffCreateEvent.class)
    public void SupplierStaffCreateEvent(SupplierStaffCreateEvent supplierStaffCreateEvent) {
        CreateSupplierStaff createSupplierStaff = supplierStaffCreateEvent.getCreateSupplierStaff();
        List<Long> roleIds = createSupplierStaff.getRoleIds();
        String mobile = createSupplierStaff.getMobile();
        String name = createSupplierStaff.getName();
        boolean enabled = createSupplierStaff.isEnabled();
        Long supplierId = createSupplierStaff.getSupplierId();
        UserInfo userInfo = userInfoService.getUserByMobile(mobile, null);
        if (userInfo == null) {
            userInfo = userInfoService.createActiveUser(UserCreateCommand.of(name, mobile, "86", "swy20210401"));
        }
        SupplierStaff supplierStaff = createSupplierStaff(userInfo.getCode(), name, mobile, userInfo.getId(), supplierId, enabled);
        if (roleIds != null) {
            List<Role> roles = roleService.findByIds(roleIds);
            supplierStaff.setRoles(roles);
        }
        supplierStaffService.save(supplierStaff);
    }

    private SupplierStaff createSupplierStaff(String code, String name, String mobile, Long userId, Long supplierId, boolean enabled) {
        net.bncloud.saas.supplier.domain.Supplier supplier = new net.bncloud.saas.supplier.domain.Supplier();
        supplier.setId(supplierId);
        return SupplierStaff.builder()
                .code(code)
                .name(name)
                .user(UserId.of(userId))
                .mobile(mobile)
                .enabled(enabled)
                .supplier(supplier)
                .build();
    }
}
