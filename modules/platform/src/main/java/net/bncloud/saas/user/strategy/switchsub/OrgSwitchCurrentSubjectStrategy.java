package net.bncloud.saas.user.strategy.switchsub;

import cn.hutool.core.collection.CollectionUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import net.bncloud.api.feign.saas.user.SubjectType;
import net.bncloud.common.security.BncUserDetails;
import net.bncloud.common.security.LoginInfo;
import net.bncloud.common.security.Org;
import net.bncloud.common.security.SecurityUtils;
import net.bncloud.common.util.BeanUtilTwo;
import net.bncloud.saas.authorize.domain.Role;
import net.bncloud.saas.tenant.domain.OrgEmployee;
import net.bncloud.saas.tenant.domain.Organization;
import net.bncloud.saas.tenant.repository.OrgEmployeeRepository;
import net.bncloud.saas.tenant.repository.OrganizationRepository;
import net.bncloud.saas.user.domain.UserCurrent;
import net.bncloud.saas.user.domain.vo.UserOrgVO;
import net.bncloud.saas.user.repository.UserCurrentRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component("orgSwitchCurrentSubjectStrategy")
@AllArgsConstructor
public class OrgSwitchCurrentSubjectStrategy implements ISwitchCurrentSubjectStrategy {
    private final OrganizationRepository organizationRepository;
    private final OrgEmployeeRepository orgEmployeeRepository;
    private final UserCurrentRepository userCurrentRepository;

    @Override
    public SwitchCurrentUserInfo switchCurrentSubject(Long subId) {
        SwitchCurrentUserInfo switchCurrentUserInfo = new SwitchCurrentUserInfo();
        LoginInfo loginInfo = SecurityUtils.getLoginInfoOrThrow();
        Optional<BncUserDetails> bncUserDetailsOptional = SecurityUtils.getCurrentUser();
        loginInfo.setCurrentSubjectType(SubjectType.org.name());
        Optional<Organization> organizationOptional = organizationRepository.findById(subId);
        if (!organizationOptional.isPresent()) {
            return switchCurrentUserInfo;
        }
        Organization organization = organizationOptional.get();
        Org currentOrg = loginInfo.getCurrentOrg();
        currentOrg.setId(organization.getId());
        currentOrg.setName(organization.getName());
        currentOrg.setCompanyId(organization.getCompany().getId());
        currentOrg.setCompanyName(organization.getCompany().getName());
        loginInfo.setCurrentOrg(currentOrg);
        Optional<OrgEmployee> orgEmployeeOptional = orgEmployeeRepository.findByUser_UserIdAndOrg_Id(loginInfo.getId(), organization.getId());
        if (orgEmployeeOptional.isPresent()) {
            OrgEmployee orgEmployee = orgEmployeeOptional.get();
            List<Role> roles = orgEmployee.getRoles();
            updateLoginInfoRoles(loginInfo, roles);
        } else {
            updateLoginInfoRoles(loginInfo, Lists.newArrayList());
        }
        switchCurrentUserInfo.setLoginInfo(loginInfo);

        if (bncUserDetailsOptional.isPresent()) {
            BncUserDetails bncUserDetails = bncUserDetailsOptional.get();
            UserCurrent userCurrent = userCurrentRepository.findById(bncUserDetails.getId()).get();
            if (BeanUtilTwo.isNotEmpty(userCurrent)) {
                userCurrent.setCurrentOrg(UserOrgVO.of(organization.getId(), organization.getName()));
                userCurrentRepository.save(userCurrent);
                switchCurrentUserInfo.setUserCurrent(userCurrent);
            }
        }

        return switchCurrentUserInfo;
    }

    private void updateLoginInfoRoles(LoginInfo loginInfo, List<net.bncloud.saas.authorize.domain.Role> roles) {
        Set<net.bncloud.common.security.Role> rolesSet = loginInfo.getRoles();
        if (CollectionUtil.isNotEmpty(roles)) {
            if (CollectionUtil.isEmpty(rolesSet)) {
                rolesSet = Sets.newHashSet();
            }
            List<net.bncloud.common.security.Role> collect = roles.stream().map(r -> {
                net.bncloud.common.security.Role role = new net.bncloud.common.security.Role();
                role.setId(r.getId());
                role.setName(r.getName());
                role.setSysDefault(r.isSysDefault());
                return role;
            }).collect(Collectors.toList());
            rolesSet.addAll(collect);
            loginInfo.setRoles(rolesSet);
        }
    }
}
