package net.bncloud.saas.event.listener;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.saas.authorize.domain.Role;
import net.bncloud.saas.authorize.service.RoleService;
import net.bncloud.saas.event.CreatedEmployee;
import net.bncloud.saas.event.OrgEmployeeCreatedEvent;
import net.bncloud.saas.tenant.domain.OrgEmployee;
import net.bncloud.saas.tenant.service.OrgEmployeeService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
@Slf4j
public class OrgEmployeeCreatedEventListener {
    private final OrgEmployeeService orgEmployeeService;
    private final RoleService roleService;

    @EventListener(OrgEmployeeCreatedEvent.class)
    public void listenerOrgEmployeeCreatedEvent(OrgEmployeeCreatedEvent event) {
        CreatedEmployee createdEmployee = event.getEmployee();
        Long empId = createdEmployee.getId();
        List<Long> roleIds = createdEmployee.getRoleIds();
        OrgEmployee employee = orgEmployeeService.findById(empId);
        List<Role> roles = roleService.findByIds(roleIds);
        employee.setRoles(roles);
        orgEmployeeService.save(employee);
    }
}
