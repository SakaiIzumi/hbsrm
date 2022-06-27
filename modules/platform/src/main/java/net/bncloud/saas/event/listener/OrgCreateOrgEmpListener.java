package net.bncloud.saas.event.listener;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.api.feign.saas.user.SubjectType;
import net.bncloud.common.constants.ManagerType;
import net.bncloud.saas.authorize.domain.Role;
import net.bncloud.saas.authorize.domain.RoleGroup;
import net.bncloud.saas.authorize.domain.User;
import net.bncloud.saas.authorize.service.RoleGroupService;
import net.bncloud.saas.authorize.service.UserService;
import net.bncloud.saas.event.CreateOrganization;
import net.bncloud.saas.event.OrganizationCreateEvent;
import net.bncloud.saas.tenant.domain.OrgEmployee;
import net.bncloud.saas.tenant.domain.OrgManager;
import net.bncloud.saas.tenant.domain.Organization;
import net.bncloud.saas.tenant.domain.vo.UserId;
import net.bncloud.saas.tenant.domain.vo.UserVO;
import net.bncloud.saas.tenant.service.OrgEmployeeService;
import net.bncloud.saas.tenant.service.OrgManagerService;
import net.bncloud.saas.tenant.service.OrganizationService;
import net.bncloud.saas.user.domain.UserInfo;
import net.bncloud.saas.user.service.UserInfoService;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.SmartApplicationListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
@Slf4j
public class OrgCreateOrgEmpListener implements SmartApplicationListener {
    private final UserInfoService userInfoService;
    private final UserService userService;
    private final OrgEmployeeService orgEmployeeService;
    private final RoleGroupService roleGroupService;
    private final OrganizationService organizationService;
    private final OrgManagerService orgManagerService;

    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        OrganizationCreateEvent organizationCreateEvent = (OrganizationCreateEvent) applicationEvent;
        CreateOrganization createOrganization = organizationCreateEvent.getCreateOrganization();
        Long userId = createOrganization.getUserId();
        Long orgId = createOrganization.getOrgId();
        UserInfo userInfo = userInfoService.getUserById(userId);

        OrgEmployee emp = new OrgEmployee();
        emp.setCode(userInfo.getCode());
        emp.setName(userInfo.getName());
        emp.setUser(UserId.of(userInfo.getId()));
        emp.setEnabled(Boolean.TRUE);
        emp.setMobile(userInfo.getMobile());
        Organization organization = organizationService.getOne(orgId);
        organization.setId(orgId);
        emp.setOrg(organization);

        RoleGroup roleGroup = roleGroupService.findOneByNameAndSubjectType("协作组织管理员组", SubjectType.org);
        List<Role> roles = Lists.newArrayList(roleGroup.getRoles());
        emp.setRoles(roles);
        orgEmployeeService.save(emp);
//        organization.setEmployees(Lists.newArrayList(emp));
//        organizationService.save(organization);

        OrgManager orgManager = new OrgManager();
        orgManager.setUser(UserVO.of(userInfo.getId(), userInfo.getName()));
        orgManager.setManagerType(ManagerType.MAIN);
        orgManager.setEmployeeId(emp.getId());
        orgManager.setEnabled(Boolean.TRUE);

        orgManager.setOrg(organization);
        orgManagerService.save(orgManager);
//        //移除临时组织管理员角色

        Optional<User> userOptional = userService.findById(userId);
        userOptional.ifPresent(user -> {
            RoleGroup roleGroupTemp = roleGroupService.findOneByNameAndSubjectType("平台协作组织管理员组", SubjectType.platform);
            if (roleGroupTemp != null) {
                List<Role> rolesTemp = roleGroupTemp.getRoles();
                user.getRoles().removeAll(rolesTemp);
                userService.save(user);
            }
        });
    }

    @Override
    public boolean supportsEventType(Class<? extends ApplicationEvent> aClass) {
        return OrganizationCreateEvent.class.isAssignableFrom(aClass);
    }

    @Override
    public boolean supportsSourceType(Class<?> sourceType) {
        return true;
    }

    @Override
    public int getOrder() {
        return 2; //需要保证顺序执行
    }


}
