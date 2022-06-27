package net.bncloud.saas.user.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import net.bncloud.api.feign.saas.user.Mobile;
import net.bncloud.common.security.*;
import net.bncloud.saas.authorize.domain.MenuNav;
import net.bncloud.saas.user.domain.vo.SupplierVO;
import net.bncloud.saas.user.domain.vo.UserOrgVO;
import net.bncloud.service.api.platform.user.dto.AccountStatus;
import net.bncloud.service.api.platform.user.dto.Password;
import net.bncloud.api.feign.saas.user.SubjectType;
import net.bncloud.common.security.data.DataDimensionGrant;
import net.bncloud.saas.authorize.domain.DataGrant;
import net.bncloud.saas.authorize.service.DataGrantService;
import net.bncloud.saas.authorize.service.MenuNavService;
import net.bncloud.saas.ding.service.DingTalkUserAuthService;
import net.bncloud.saas.supplier.domain.SupplierStaff;
import net.bncloud.saas.supplier.repository.SupplierStaffRepository;
import net.bncloud.saas.tenant.domain.Organization;
import net.bncloud.saas.tenant.repository.OrganizationRepository;
import net.bncloud.saas.user.domain.UserCurrent;
import net.bncloud.saas.user.domain.UserInfo;
import net.bncloud.saas.user.domain.UserPassword;
import net.bncloud.saas.user.repository.UserCurrentRepository;
import net.bncloud.saas.user.repository.UserInfoRepository;
import net.bncloud.saas.user.strategy.menunav.MenuNavStrategyContext;
import net.bncloud.saas.user.strategy.switchsub.SwitchCurrentSubjectContext;
import net.bncloud.saas.user.strategy.switchsub.SwitchCurrentUserInfo;
import net.bncloud.service.api.platform.user.dto.UserInfoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class LoginUserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginUserService.class);

    private final UserInfoService userInfoService;
    private final UserInfoRepository userInfoRepository;
    private final DingTalkUserAuthService dingTalkUserAuthService;
    private final StringRedisTemplate stringRedisTemplate;
    private final OrganizationRepository organizationRepository;
    private final SupplierStaffRepository supplierStaffRepository;
    private final UserCurrentRepository userCurrentRepository;
    private final MenuNavService menuNavService;
    private final DataGrantService dataGrantService;
    private final MenuNavStrategyContext menuNavStrategyContext;
    private final SwitchCurrentSubjectContext switchCurrentSubjectContext;


    public UserInfoDTO getUserInfoByMobile(String mobile) {
        Optional<UserInfo> optional = userInfoRepository.findOneByMobile(mobile);
        if (optional.isPresent()) {
            UserInfo userInfo = optional.get();
            UserPassword storedPass = userInfo.getPassword();
            if (storedPass == null) {
                storedPass = new UserPassword();
            }
            Password password = Password.of(storedPass.getPassword(), storedPass.isCredentialsExpired());
            final AccountStatus accountStatus = buildAccountStatus(userInfo);

            UserInfoDTO dto = new UserInfoDTO();
            dto.setId(userInfo.getId());
            dto.setCode(userInfo.getCode());
            dto.setName(userInfo.getName());
            dto.setNickname(userInfo.getNickname());
            dto.setMobile(userInfo.getMobile());
            dto.setEmail(userInfo.getEmail());
            dto.setPassword(password);
            dto.setAccountStatus(accountStatus);
            return dto;
        }
        return null;
    }

    private List<Org> orgs(UserInfo userInfo) {
        List<Organization> organizations = organizationRepository.findAllByUserId(userInfo.getId());
        if (CollectionUtil.isNotEmpty(organizations)) {
            return organizations.stream().map(o -> {
                Org org = new Org();
                org.setId(o.getId());
                org.setName(o.getName());
                return org;
            }).collect(Collectors.toList());

        }
        return Lists.newArrayList();
    }

    private List<Supplier> suppliers(UserInfo userInfo) {
        List<SupplierStaff> staffs = supplierStaffRepository.findAllByUser_UserId(userInfo.getId());
        List<net.bncloud.saas.supplier.domain.Supplier> suppliers = staffs.stream().map(SupplierStaff::getSupplier).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(suppliers)) {
            return suppliers.stream().map(s -> {
                Supplier supplier = new Supplier();
                supplier.setSupplierId(s.getId());
                supplier.setSupplierName(s.getName());
                supplier.setSupplierCode(s.getCode());
                return supplier;
            }).collect(Collectors.toList());
        }
        return Lists.newArrayList();
    }

    @Transactional
    public void cacheLoginInfo(Mobile mobile) {
        Optional<UserInfo> optional = userInfoRepository.findOneByMobile(mobile.getMobile());
        if (optional.isPresent()) {
            UserInfo userInfo = optional.get();
            // TODO 加载公司、组织信息
            Long companyId = null;
            String companyName = null;
            Supplier currentSupplier = null;
            Org org = null;
            UserCurrent currentInfo = userInfo.getCurrentInfo();

            List<Org> orgs = orgs(userInfo);
            List<Supplier> suppliers = suppliers(userInfo);

            String currentMenuNav = "";
            if (currentInfo != null) {
                currentMenuNav = currentInfo.getCurrentMenuNavType();
                SupplierVO supplierVO = currentInfo.getCurrentSupplier();
                if (supplierVO != null) {
                    currentSupplier = Supplier.of(supplierVO.getId(), supplierVO.getName(), supplierVO.getCode());
                }
            } else {
                currentInfo = new UserCurrent();
                currentInfo.setUserInfo(userInfo);
            }
            if (CollectionUtil.isNotEmpty(orgs)) {
                org = orgs.get(0);
                currentInfo.setCurrentOrg(UserOrgVO.of(org.getCompanyId(), org.getCompanyName(), org.getId(), org.getName()));
            }
            if (CollectionUtil.isNotEmpty(suppliers)) {
                Supplier supplier = suppliers.get(0);
                currentSupplier = Supplier.of(supplier.getSupplierId(), supplier.getSupplierName(), supplier.getSupplierCode());
                SupplierVO supplierVO = SupplierVO.of(supplier.getSupplierId(), supplier.getSupplierName(), supplier.getSupplierCode());
                currentInfo.setCurrentSupplier(supplierVO);
            }
            List<MenuNav> meuNavs = menuNavStrategyContext.getMeuNavs(userInfo.getId());


            if (StrUtil.isEmpty(currentMenuNav)) {
                currentMenuNav = meuNavs.get(0).getMenuNavType().name();
                currentInfo.setCurrentMenuNavType(currentMenuNav);
            }
            userInfo.setCurrentInfo(currentInfo);
            userInfoRepository.save(userInfo);
            UserPassword storedPass = userInfo.getPassword();
            if (storedPass == null) {
                storedPass = new UserPassword();
            }
            Password password = Password.of(storedPass.getPassword(), storedPass.isCredentialsExpired());
            List<MenuNavDto> menuNavDTOs = menuNavService.toDto(meuNavs);
            String subjectType = "";
            if (CollectionUtil.isEmpty(orgs) && CollectionUtil.isEmpty(suppliers)) {
                subjectType = SubjectType.platform.name();
            } else {
                subjectType = menuNavService.getSubjectType(currentMenuNav);
            }
            LoginInfo loginInfo = new LoginInfo(userInfo.getId(), userInfo.getMobile(), userInfo.getName(), Platform.PC, // TODO
                    null,
                    org,
                    currentSupplier,
                    null, menuNavDTOs, currentMenuNav, subjectType,
                    orgs,
                    suppliers
            );
            cacheLoginInfo(loginInfo);
        }
    }


    private void cacheLoginInfo(LoginInfo loginInfo) {
        stringRedisTemplate.opsForValue().set(LoginInfoContextPersistenceFilter.CACHE_KEY_PREFIX + loginInfo.getId(), JSON.toJSONString(loginInfo));
    }

    private AccountStatus buildAccountStatus(UserInfo userInfo) {
        if (userInfo.getStatus() == null) {
            return AccountStatus.of(false, false, false);
        }
        return AccountStatus.of(userInfo.getStatus().isDisabled(), userInfo.getStatus().isAccountExpired(),
                userInfo.getStatus().isAccountLocked());
    }

    /**
     * 切换当前组织或当前供应商
     */
    public void switchCurrentSubject(SubjectType subjectType, Long subId) {
        SwitchCurrentUserInfo switchCurrentUserInfo = switchCurrentSubjectContext.switchCurrentSubject(subjectType.name(), subId);
        LoginInfo loginInfo = switchCurrentUserInfo.getLoginInfo();
        UserCurrent userCurrent = switchCurrentUserInfo.getUserCurrent();
        if (userCurrent != null) {
            userCurrentRepository.save(userCurrent);
        }
        if (loginInfo != null) {
            cacheLoginInfo(loginInfo);
        }
    }

    //更改当前用户角色
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


    public DataDimensionGrant build(DataGrant dataGrant) {
        DataDimensionGrant dataDimensionGrant = new DataDimensionGrant();
        dataDimensionGrant.setDimensionCode(dataGrant.getDimensionCode());
        dataDimensionGrant.setSpecial(dataGrant.getIsSpecial());
        return dataDimensionGrant;
    }
}
