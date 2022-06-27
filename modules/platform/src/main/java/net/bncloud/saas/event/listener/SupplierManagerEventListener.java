package net.bncloud.saas.event.listener;


import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.api.feign.saas.user.SubjectType;
import net.bncloud.common.constants.ManagerType;
import net.bncloud.saas.authorize.domain.Role;
import net.bncloud.saas.authorize.domain.RoleGroup;
import net.bncloud.saas.authorize.service.RoleGroupService;
import net.bncloud.saas.event.CreateSupplierManager;
import net.bncloud.saas.event.SupplierManagerCreateEvent;
import net.bncloud.saas.supplier.domain.Supplier;
import net.bncloud.saas.supplier.domain.SupplierManager;
import net.bncloud.saas.supplier.domain.SupplierStaff;
import net.bncloud.saas.supplier.service.SupplierManagerService;
import net.bncloud.saas.supplier.service.SupplierStaffService;
import net.bncloud.saas.tenant.domain.vo.UserId;
import net.bncloud.saas.user.domain.UserInfo;
import net.bncloud.saas.user.service.UserInfoService;
import net.bncloud.saas.user.service.command.UserCreateCommand;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@AllArgsConstructor
public class SupplierManagerEventListener {

    private final SupplierStaffService supplierStaffService;
    private final SupplierManagerService supplierManagerService;
    private final UserInfoService userInfoService;
    private final RoleGroupService roleGroupService;

    @EventListener(SupplierManagerCreateEvent.class)
    public void listenSupplierManagerCreateEvent(SupplierManagerCreateEvent supplierManagerCreateEvent) {
        CreateSupplierManager createSupplierManager = supplierManagerCreateEvent.getCreateSupplierManager();
        String mobile = createSupplierManager.getMobile();
        String name = createSupplierManager.getName();
        Long supplierId = createSupplierManager.getSupplierId();
        UserInfo user = userInfoService.getUserByMobile(mobile, "");
        if (user == null) {
            user = userInfoService.createActiveUser(UserCreateCommand.of(name, mobile, "86", "swy20210401")); // fixme
        }
        RoleGroup roleGroup = roleGroupService.findOneByNameAndSubjectType("供应商管理员组", SubjectType.supplier);
        List<Role> roles = Lists.newArrayList(roleGroup.getRoles());
        SupplierStaff staff = SupplierStaff.builder()
                .code(user.getCode())
                .name(user.getName())
                .user(UserId.of(user.getId()))
                .mobile(mobile)
                .roles(roles)
                .supplier(Supplier.of(supplierId))
                .enabled(true)
                .build();

        staff = supplierStaffService.save(staff);
        SupplierManager supplierManager = SupplierManager.builder()
                .name(user.getName())
                .mobile(mobile)
                .managerType(ManagerType.MAIN)
                .supplier(Supplier.of(supplierId))
                .supplierStaff(staff)
                .enabled(true)
                .build();
        supplierManagerService.save(supplierManager);
    }

}
