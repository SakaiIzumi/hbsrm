package net.bncloud.saas.event.listener;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import net.bncloud.saas.authorize.domain.Role;
import net.bncloud.saas.authorize.domain.RoleGroup;
import net.bncloud.saas.authorize.repository.RoleGroupRepository;
import net.bncloud.saas.event.SupplierCreatedEvent;
import net.bncloud.saas.supplier.domain.Supplier;
import net.bncloud.saas.supplier.domain.SupplierStaff;
import net.bncloud.saas.supplier.repository.SupplierExtRepository;
import net.bncloud.saas.supplier.repository.SupplierManagerRepository;
import net.bncloud.saas.supplier.repository.SupplierStaffRepository;
import net.bncloud.saas.supplier.service.SupplierManagerService;
import net.bncloud.saas.supplier.service.SupplierStaffService;
import net.bncloud.saas.tenant.domain.vo.UserId;
import net.bncloud.saas.tenant.service.DepartmentService;
import net.bncloud.saas.tenant.service.command.CreateSupplierDeptCommand;
import net.bncloud.saas.user.domain.UserInfo;
import net.bncloud.saas.user.service.UserInfoService;
import net.bncloud.saas.user.service.command.UserCreateCommand;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class SupplierEventListener {
    private final UserInfoService userInfoService;
    private final SupplierStaffService supplierStaffService;
    private final SupplierManagerService supplierManagerService;
    private final RoleGroupRepository roleGroupRepository;

    @EventListener(SupplierCreatedEvent.class)
    public void supplierCreateEventListener(SupplierCreatedEvent event) {
        Supplier supplier = event.getSupplier();
        final CreateSupplierDeptCommand createSupplierDeptCommand = CreateSupplierDeptCommand.of(supplier.getId(), supplier.getCode(), supplier.getName());
        createSupplierDeptCommand.getManagers().add(new net.bncloud.saas.tenant.service.command.SupplierManager(null, supplier.getManagerMobile(), supplier.getManagerName()));
        UserInfo userInfo = userInfoService.getUserByMobile(supplier.getManagerMobile(), "");
        if (userInfo == null) {
            userInfo = userInfoService.createActiveUser(UserCreateCommand.of(supplier.getManagerName(), supplier.getManagerMobile(), "86", "swy20210401"));
        }
        RoleGroup roleGroup = roleGroupRepository.findOneByName("供应商管理员组");
        List<Role> roles = roleGroup.getRoles();
        SupplierStaff supplierStaff = SupplierStaff.builder()
                .name(supplier.getManagerName())
                .user(UserId.of(userInfo.getId()))
                .roles(Lists.newArrayList(roles))
                .supplier(supplier).build();
        supplierStaffService.save(supplierStaff);
        net.bncloud.saas.supplier.domain.SupplierManager supplierManager = new net.bncloud.saas.supplier.domain.SupplierManager();
        supplierManager.setName(supplier.getManagerName());
        supplierManager.setMobile(supplier.getManagerName());
        supplierManager.setSupplierStaff(supplierStaff);
        supplierManager.setSupplier(supplier);
        supplierManagerService.save(supplierManager);
    }
}
