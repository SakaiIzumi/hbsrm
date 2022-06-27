package net.bncloud.saas.ding.listener;

import net.bncloud.saas.ding.domain.DingTalkCorp;
import net.bncloud.saas.ding.domain.DingUser;
import net.bncloud.saas.event.DingUserCreatedEvent;
import net.bncloud.saas.tenant.service.DepartmentService;
import net.bncloud.saas.tenant.service.OrgEmployeeService;
import net.bncloud.saas.tenant.service.OrganizationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class DingUserListener {

    private final OrganizationService organizationService;
    private final DepartmentService departmentService;
    private final OrgEmployeeService orgEmployeeService;

    public DingUserListener(OrganizationService organizationService,
                            DepartmentService departmentService,
                            OrgEmployeeService orgEmployeeService) {
        this.organizationService = organizationService;
        this.departmentService = departmentService;
        this.orgEmployeeService = orgEmployeeService;
    }

    @EventListener(DingUserCreatedEvent.class)
    public void dingUserCreatedEventListener(DingUserCreatedEvent event) {
        final DingUser dingUser = event.getDingUser();
        final DingTalkCorp corp = dingUser.getCorp();
        final Long orgId = corp.getOrgId();
        final String deptIdList = dingUser.getDeptIdList();
        if (StringUtils.isNotBlank(deptIdList)) {
            // 同步部门
        } else {
//            orgEmployeeService.addEmployee();
        }

    }
}
