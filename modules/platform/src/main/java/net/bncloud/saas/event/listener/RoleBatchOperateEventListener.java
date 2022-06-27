package net.bncloud.saas.event.listener;

import cn.hutool.core.collection.CollectionUtil;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import net.bncloud.api.feign.saas.user.SubjectType;
import net.bncloud.common.security.Org;
import net.bncloud.common.security.SecurityUtils;
import net.bncloud.common.security.Supplier;
import net.bncloud.saas.authorize.domain.Role;
import net.bncloud.saas.authorize.repository.RoleRepository;
import net.bncloud.saas.event.RoleBatch;
import net.bncloud.saas.event.RoleBatchGrantEvent;
import net.bncloud.saas.event.RoleBatchRemoveEvent;
import net.bncloud.saas.supplier.domain.SupplierStaff;
import net.bncloud.saas.supplier.repository.SupplierStaffRepository;
import net.bncloud.saas.tenant.domain.OrgEmployee;
import net.bncloud.saas.tenant.repository.OrgEmployeeRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class RoleBatchOperateEventListener {
    private final OrgEmployeeRepository orgEmployeeRepository;
    private final SupplierStaffRepository supplierStaffRepository;
    private final RoleRepository roleRepository;


    @EventListener(RoleBatchGrantEvent.class)
    public void RoleBatchGrantEvent(RoleBatchGrantEvent roleBatchGrantEvent) {
        RoleBatch roleBatch = roleBatchGrantEvent.getRoleBatch();
        List<Long> roleIds = roleBatch.getRoleIds();
        List<Long> userIds = roleBatch.getUserIds();
        String subjectType = roleBatch.getSubjectType();

        if (SubjectType.org.name().equals(subjectType)) {
            Optional<Org> currentOrg = SecurityUtils.getCurrentOrg();
            if (currentOrg.isPresent()) {
                List<OrgEmployee> orgEmployees = orgEmployeeRepository.findAllByOrg_IdAndIdIn(currentOrg.get().getId(), userIds);
                if (CollectionUtil.isNotEmpty(orgEmployees)) {
                    List<Role> roles = roleRepository.findAllById(roleIds);
                    orgEmployees.forEach(orgEmployee -> {
                        List<Role> rolesHas = orgEmployee.getRoles();
                        if (CollectionUtil.isNotEmpty(rolesHas)) {
                            rolesHas.addAll(roles);
                        } else {
                            rolesHas = roles;
                        }
                        orgEmployee.setRoles(rolesHas);
                    });
                    orgEmployeeRepository.saveAll(orgEmployees);
                }
            }
        } else if (SubjectType.supplier.name().equals(subjectType)) {
            Optional<Supplier> currentSupplier = SecurityUtils.getCurrentSupplier();
            if (currentSupplier.isPresent()) {
                List<SupplierStaff> supplierStaffs = supplierStaffRepository.findAllBySupplier_IdAndIdIn(currentSupplier.get().getSupplierId(), userIds);
                if (CollectionUtil.isNotEmpty(supplierStaffs)) {
                    List<Role> roles = roleRepository.findAllById(roleIds);
                    supplierStaffs.forEach(supplierStaff -> {
                        List<Role> rolesHas = supplierStaff.getRoles();
                        if (CollectionUtil.isNotEmpty(rolesHas)) {
                            rolesHas.addAll(roles);
                        } else {
                            rolesHas = roles;
                        }
                        supplierStaff.setRoles(rolesHas);
                    });
                    supplierStaffRepository.saveAll(supplierStaffs);
                }
            }
        }

    }

    @EventListener(RoleBatchRemoveEvent.class)
    public void RoleBatchRemoveEvent(RoleBatchRemoveEvent roleBatchRemoveEvent) {
        RoleBatch roleBatch = roleBatchRemoveEvent.getRoleBatch();
        List<Long> roleIds = roleBatch.getRoleIds();
        List<Long> userIds = roleBatch.getUserIds();
        String subjectType = roleBatch.getSubjectType();
        List<Role> roles = roleRepository.findAllById(roleIds);
        if (SubjectType.org.name().equals(subjectType)) {
            Optional<Org> currentOrg = SecurityUtils.getCurrentOrg();
            if (currentOrg.isPresent()) {
                Org org = currentOrg.get();
                List<OrgEmployee> orgEmployees = orgEmployeeRepository.findAllById(userIds);
                if (CollectionUtil.isNotEmpty(orgEmployees)) {
                    orgEmployees.forEach(orgEmployee -> {
                        List<Role> rolesBefore = orgEmployee.getRoles();
                        List<Role> rolesAfter = Lists.newArrayList();
                        rolesBefore.forEach(role -> {
                            if (!roles.contains(role)) {
                                rolesAfter.add(role);
                            }
                        });
                        orgEmployee.setRoles(rolesAfter);

                    });
                    orgEmployeeRepository.saveAll(orgEmployees);
                }
            }
        } else if (SubjectType.supplier.name().equals(subjectType)) {
            Optional<Supplier> currentSupplier = SecurityUtils.getCurrentSupplier();
            if (currentSupplier.isPresent()) {
                List<SupplierStaff> supplierStaffs = supplierStaffRepository.findAllById(userIds);
                if (CollectionUtil.isNotEmpty(supplierStaffs)) {
                    supplierStaffs.forEach(supplierStaff -> {
                        List<Role> rolesBefore = supplierStaff.getRoles();
                        List<Role> rolesAfter = Lists.newArrayList();
                        rolesBefore.forEach(role -> {
                            if (!roles.contains(role)) {
                                rolesAfter.add(role);
                            }
                        });
                        supplierStaff.setRoles(rolesAfter);
                    });
                    supplierStaffRepository.saveAll(supplierStaffs);
                }
            }
        }

    }
}
