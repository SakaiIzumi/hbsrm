package net.bncloud.saas.user.strategy.userInfo;

import lombok.AllArgsConstructor;
import net.bncloud.common.security.LoginInfo;
import net.bncloud.common.security.SecurityUtils;
import net.bncloud.common.security.Supplier;
import net.bncloud.saas.authorize.domain.Menu;
import net.bncloud.saas.authorize.domain.Role;
import net.bncloud.saas.supplier.domain.SupplierStaff;
import net.bncloud.saas.supplier.repository.SupplierStaffRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component("supplierUserInfoStrategy")
@AllArgsConstructor
public class SupplierUserInfoStrategy implements IUserInfoStrategy {
    private final SupplierStaffRepository supplierStaffRepository;

    @Override
    public RoleMenuMass getRoleMenuMass() {
        RoleMenuMass roleMenuMass = RoleMenuMass.create();
        LoginInfo loginInfo = SecurityUtils.getLoginInfoOrThrow();
        //加载 组织成员菜单信息
        Supplier currentSupplier = loginInfo.getCurrentSupplier();
        if (currentSupplier != null) {
            Optional<SupplierStaff> supplierStaffOptional = supplierStaffRepository.findAllByUser_UserIdAndSupplier_Id(loginInfo.getId(), currentSupplier.getSupplierId());
            if (supplierStaffOptional.isPresent()) {
                SupplierStaff supplierStaff = supplierStaffOptional.get();
                List<Role> roles = supplierStaff.getRoles();
                roleMenuMass.getRoleList().addAll(roles);
                supplierStaff.getRoles().forEach(r -> {
                    roleMenuMass.getMenuList().addAll(r.getMenus());
                });
            }
        }
        List<Menu> menuList = roleMenuMass.getMenuList().stream().filter(menu -> !menu.isHidden()).distinct().collect(Collectors.toList());
        roleMenuMass.setMenuList(menuList);
        return roleMenuMass;
    }
}
