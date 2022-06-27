package net.bncloud.saas.user.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.rnkrsoft.bopomofo4j.Bopomofo4j;
import com.rnkrsoft.bopomofo4j.ToneType;
import lombok.AllArgsConstructor;
import net.bncloud.api.feign.saas.user.SubjectType;
import net.bncloud.common.api.ResultCode;
import net.bncloud.common.base.service.BaseService;
import net.bncloud.common.constants.MenuNavType;
import net.bncloud.common.exception.ApiException;
import net.bncloud.common.exception.BizException;
import net.bncloud.common.security.*;
import net.bncloud.common.security.LoginInfo;
import net.bncloud.common.security.data.DataDimensionGrant;
import net.bncloud.common.security.data.GrantDataHolder;
import net.bncloud.saas.authorize.domain.*;
import net.bncloud.saas.authorize.domain.Role;
import net.bncloud.saas.authorize.repository.DataGrantRepository;
import net.bncloud.saas.authorize.repository.DataSubjectDimensionRelRepository;
import net.bncloud.saas.authorize.service.MenuNavService;
import net.bncloud.saas.authorize.service.MenuService;
import net.bncloud.saas.authorize.service.RoleService;
import net.bncloud.saas.authorize.service.dto.MenuDTO;
import net.bncloud.saas.authorize.service.dto.RoleSmallDTO;
import net.bncloud.saas.event.UserRelateInfoUpdate;
import net.bncloud.saas.event.UserRelateInfoUpdateEvent;
import net.bncloud.saas.tenant.repository.OrgManagerRecordRepository;
import net.bncloud.saas.event.CreatedUser;
import net.bncloud.saas.event.UserInfoCreatedEvent;
import net.bncloud.saas.result.SaaSResultCode;
import net.bncloud.saas.supplier.domain.SupplierStaff;
import net.bncloud.saas.supplier.repository.SupplierStaffRepository;
import net.bncloud.saas.tenant.domain.Company;
import net.bncloud.saas.tenant.domain.OrgEmployee;
import net.bncloud.saas.tenant.domain.Organization;
import net.bncloud.saas.tenant.repository.OrgEmployeeRepository;
import net.bncloud.saas.tenant.service.command.EditUserInfoCommand;
import net.bncloud.saas.user.constant.UserConstants;
import net.bncloud.saas.user.domain.*;
import net.bncloud.saas.user.domain.vo.UserOrgVO;
import net.bncloud.saas.user.repository.UserCurrentRepository;
import net.bncloud.saas.user.repository.UserInfoRepository;
import net.bncloud.saas.user.service.command.UserCreateCommand;
import net.bncloud.saas.user.service.command.UserRegisterCommand;
import net.bncloud.saas.user.service.command.UserRelateInfoUpdateCommand;
import net.bncloud.saas.user.service.dto.*;
import net.bncloud.saas.user.service.query.UserInfoQuery;
import net.bncloud.saas.user.strategy.userInfo.RoleMenuMass;
import net.bncloud.saas.user.strategy.userInfo.UserInfoStrategyContext;
import net.bncloud.saas.user.strategy.userInfo.RoleMenuMass;
import net.bncloud.saas.user.strategy.userInfo.UserInfoStrategyContext;
import net.bncloud.saas.user.web.payload.ChangePasswordPayload;
import net.bncloud.saas.utils.usercode.UserNameCodeUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class UserInfoService extends BaseService {
    private final UserInfoRepository userInfoRepository;
    private final RoleService roleService;
    private final MenuNavService menuNavService;
    private final MenuService menuService;
    private final OrgEmployeeRepository orgEmployeeRepository;
    private final SupplierStaffRepository supplierStaffRepository;
    private PasswordEncoder passwordEncoder;
    private final StringRedisTemplate stringRedisTemplate;

    protected final Log logger = LogFactory.getLog(getClass());

    public UserInfo register(UserRegisterCommand registerCommand) {
        // TODO check mobile code
        checkRegMobileExist(registerCommand.getMobile(), registerCommand.getStateCode());
        UserInfo user = registerCommand.createUserForRegister();
        UserInfo saved = userInfoRepository.save(user);
        return saved;
    }

    @Transactional
    public UserInfo changePassword(ChangePasswordPayload registerCommand) {

        LoginInfo loginInfo = SecurityUtils.getLoginInfoOrThrow();
        UserInfo userInfo = getUserById(loginInfo.getId());
        //对比新旧密码
        if (registerCommand.getPassword().equals(registerCommand.getPasswordSecond())&&passwordEncoder.matches(registerCommand.getOldPassword(), userInfo.getPassword().getPassword())) {

            UserInfo saved = null;
            userInfo.setPassword(UserPassword.init(registerCommand.getPassword()));
            saved = userInfoRepository.save(userInfo);
            if (saved!=null) {
                stringRedisTemplate.delete(LoginInfoContextPersistenceFilter.CACHE_KEY_PREFIX + loginInfo.getId());
                LoginInfoContextUtils.clear();
            }

            return saved;

        } else {
            throw new BizException(ResultCode.FAILURE_PASSWORD);
        }





    }

    public void sendRegMobileCode(String mobile, String stateCode) {
        checkRegMobileExist(mobile, stateCode);
        // TODO generate code and send
        String code = RandomStringUtils.randomNumeric(4);// TODO 验证码位数改为配置
        // TODO store code
    }

    private void checkRegMobileExist(String mobile, String stateCode) {
        if (StringUtils.isBlank(stateCode)) {
            stateCode = UserConstants.DEFAULT_MOBILE_AREA;
        }
        Optional<UserInfo> userInfoOptional = userInfoRepository.findOneByMobileAndStateCode(mobile, stateCode);
        if (userInfoOptional.isPresent()) {
            throw new BizException(SaaSResultCode.USER_MOBILE_REGISTERED);
        }
    }

    public UserInfo getUserByMobile(String mobile, String stateCode) {
        if (StringUtils.isBlank(stateCode)) {
            stateCode = UserConstants.DEFAULT_MOBILE_AREA;
        }
        Optional<UserInfo> userInfoOptional = userInfoRepository.findOneByMobileAndStateCode(mobile, stateCode);
        return userInfoOptional.orElse(null);
    }

    public UserInfo createActiveUser(UserCreateCommand command) {
        UserInfo user = getUserByMobile(command.getMobile(), command.getStateCode());
        if (user != null) {
            return user;
        }

        user = command.createActiveUser();
        //TODO 暂时随机生成
        String code = UserNameCodeUtil.userNameCode(user.getName());
        user.setCode(code);
        UserInfo saved = userInfoRepository.save(user);
        applicationEventPublisher.publishEvent(new UserInfoCreatedEvent(this, new CreatedUser(saved.getId(), saved.getName(), saved.getMobile())));
        return saved;
    }

    public UserInfo createInactiveUser(UserCreateCommand command) {
        UserInfo user = getUserByMobile(command.getMobile(), command.getStateCode());
        if (user != null) {
            return user;
        }
        user = command.createInactiveUser();

        UserInfo saved = userInfoRepository.save(user);
        applicationEventPublisher.publishEvent(new UserInfoCreatedEvent(this, new CreatedUser(saved.getId(), saved.getName(), saved.getMobile())));
        return saved;
    }


    private final UserInfoStrategyContext userInfoStrategyContext;

    @Transactional(readOnly = true)
    public Object userInfo() {
        LoginInfo loginInfo = SecurityUtils.getLoginInfoOrThrow();
        Optional<UserInfo> userInfoOptional = userInfoRepository.findById(loginInfo.getId());
        if (!userInfoOptional.isPresent()) {
            throw new BizException(ResultCode.NOT_FOUND);
        }
        UserInfo userInfo = userInfoOptional.get();
        List<MenuNavDto> menuNavList = loginInfo.getMenuNavs();
        Org currentOrg = loginInfo.getCurrentOrg();
        Supplier currentSupplier = loginInfo.getCurrentSupplier();
        String currentMenuNav = loginInfo.getCurrentMenuNav();
        String currentSubjectType = loginInfo.getCurrentSubjectType();

        //获取基础信息
        BaseUserInfoDTO baseUserInfo = BaseUserInfoDTO.builder()
                .id(userInfo.getId())
                .name(userInfo.getName())
                .avatar(userInfo.getAvatar())
                .email(userInfo.getEmail())
                .mobile(userInfo.getMobile())
                .subjectType(currentSubjectType)
                .currentMenuNav(currentMenuNav)
                .build();
        if (currentOrg != null && currentOrg.getId() != null) {
            baseUserInfo.setCurrentOrgId(currentOrg.getId());
            baseUserInfo.setCurrentOrg(currentOrg);
        }
        if (currentSupplier != null) {
            baseUserInfo.setCurrentSupId(currentSupplier.getSupplierId());
            baseUserInfo.setCurrentSupplier(currentSupplier);
        }
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        PermissionInfoDTO permissionInfoDTO = PermissionInfoDTO.create();
        if (StrUtil.isEmpty(currentMenuNav) || StrUtil.isEmpty(currentSubjectType)) {
            userInfoDTO.setUserInfo(baseUserInfo);
            userInfoDTO.setPermissionInfo(permissionInfoDTO);
            return userInfoDTO;
        }
        RoleMenuMass roleMenuMass = userInfoStrategyContext.getRoleMenuMass(currentSubjectType);
        // 菜单
        putMenuMap(currentMenuNav, permissionInfoDTO, roleMenuMass.getMenuList());
        // 菜单导航
        putMenuNavMap(menuNavList, permissionInfoDTO);
        // 当前角色
        putRoleMap(permissionInfoDTO, roleMenuMass.getRoleList());
        //当前组织
        putOrgMap(loginInfo, permissionInfoDTO);
        //当前供应商
        putSupplierMap(loginInfo, permissionInfoDTO);
        userInfoDTO.setUserInfo(baseUserInfo);
        userInfoDTO.setPermissionInfo(permissionInfoDTO);
        return userInfoDTO;
    }


    @Transactional(readOnly = true)
    public CurrentUserInfoDTO currentUserInfo(Long id) {
        Optional<UserInfo> userInfoOptional = userInfoRepository.findById(id);
        if (!userInfoOptional.isPresent()) {
            throw new BizException(ResultCode.NOT_FOUND);
        }
        UserInfo u = userInfoOptional.get();
        CurrentUserInfoDTO current = new CurrentUserInfoDTO();
        current.setName(u.getName());
        current.setNickname(u.getNickname());
        current.setMobile(u.getMobile());
        current.setEmail(u.getEmail());
        current.setQqCode(u.getQqCode());
        current.setWeChatCode(u.getWeChatCode());
        current.setGender(u.getGender());
        current.setAvatar(u.getAvatar());
        Optional.ofNullable(u.getLoginInfo()).ifPresent(l -> {
            current.setLastLoginIp(l.getLastLoginIp());
            current.setLastLoginTime(l.getLastLoginTime());
        });
        Optional.ofNullable(u.getCurrentInfo()).ifPresent(userCurrent -> {
            current.setOrg(userCurrent.getCurrentOrg());
            current.setSupplier(userCurrent.getCurrentSupplier());
        });
        current.setOrgList(u.getOrgList());
        current.setSupplierList(u.getSupplierList());

        try {
            List<RoleSmallDTO> roles = roleService.findByUsersId(u.getId());
            current.setRoles(roles);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            List<MenuDTO> menus = menuService.getUserMenus(u.getId());
            current.setMenus(menus);
        } catch (Exception e) {
            e.printStackTrace();
        }

        SecurityUtils.getLoginInfo().get().getCurrentSubjectType();


        return current;
    }

    /**
     * 获取同一主题，同一维度下，数据授权-维度值集合
     *
     * @param dataGrants
     * @return
     */
    Set<String> getDataIds(List<DataGrant> dataGrants) {
        if (dataGrants == null || dataGrants.isEmpty()) {
            return null;
        }
        Set<String> dataIds = new HashSet<>();
        for (DataGrant dataGrant : dataGrants) {
            dataIds.add(dataGrant.getDimensionValue());
        }
        return dataIds;
    }


    private boolean getSpecial(List<DataGrant> dataGrants) {
        boolean special = false;
        if (dataGrants == null || dataGrants.isEmpty()) {
            return false;
        }
        for (DataGrant dataGrant : dataGrants) {
            Boolean isSpecial = dataGrant.getIsSpecial();
            if (isSpecial) {
                special = isSpecial;
                break;
            }
        }
        return special;
    }


    @Transactional(readOnly = true)
    public CurrentUserInfoDTO currentUserInfo() {
        LoginInfo loginInfo = SecurityUtils.getLoginInfo().orElse(null);
        if (loginInfo == null) {
            throw new AccessDeniedException("访问该接口需要认证");
        }
        Long id = loginInfo.getId();
        Optional<UserInfo> userInfoOptional = userInfoRepository.findById(id);
        if (!userInfoOptional.isPresent()) {
            throw new BizException(ResultCode.NOT_FOUND);
        }
        UserInfo u = userInfoOptional.get();
        CurrentUserInfoDTO current = new CurrentUserInfoDTO();
        current.setName(u.getName());
        current.setNickname(u.getNickname());
        current.setMobile(u.getMobile());
        current.setEmail(u.getEmail());
        current.setQqCode(u.getQqCode());
        current.setWeChatCode(u.getWeChatCode());
        current.setGender(u.getGender());
        current.setAvatar(u.getAvatar());
        Optional.ofNullable(u.getLoginInfo()).ifPresent(l -> {
            current.setLastLoginIp(l.getLastLoginIp());
            current.setLastLoginTime(l.getLastLoginTime());
        });
        Optional.ofNullable(u.getCurrentInfo()).ifPresent(userCurrent -> {
            current.setOrg(userCurrent.getCurrentOrg());
            current.setSupplier(userCurrent.getCurrentSupplier());
        });
        current.setOrgList(u.getOrgList());
        current.setSupplierList(u.getSupplierList());

        try {
            List<RoleSmallDTO> roles = roleService.findByUsersId(u.getId());
            current.setRoles(roles);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            List<MenuDTO> menus = menuService.getUserMenus(u.getId());
            current.setMenus(menus);
        } catch (Exception e) {
            e.printStackTrace();
        }


//        List<UserDetailRefDTO> userDetailRefs = Lists.newArrayList();
//        current.setUserDetailRefs(userDetailRefs);
//        String currentSubjectType = loginInfo.getCurrentSubjectType();
//        if (SubjectType.org.name().equals(currentSubjectType)) {
//            List<UserDetailRefDTO> detailRefs = this.tenantOrgInfo(u);
//            userDetailRefs.addAll(detailRefs);
//        }
////        else if (SubjectType.supplier.name().equals(currentSubjectType)) {}
//        List<UserDetailRefDTO> detailRefs = supplierRefOrgInfo(u);
//        userDetailRefs.addAll(detailRefs);

        return current;
    }

    public void changeCurrentCompany(Company company, Long userId) {
        List<Organization> organizations = company.getOrganizations();
        Optional<UserInfo> userInfoOptional = userInfoRepository.findById(userId);
        userInfoOptional.ifPresent(userInfo -> {
            UserCurrent currentInfo = userInfo.getCurrentInfo();
            if (currentInfo == null) {
                currentInfo = new UserCurrent();
            }
            UserOrgVO userOrgVO = UserOrgVO.of(company.getId(), company.getName());
            if (organizations != null && organizations.size() > 0) {
                Organization organization = organizations.get(0);
                userOrgVO = UserOrgVO.of(company.getId(), company.getName(), organization.getId(), organization.getName());
            }
            currentInfo.setCurrentOrg(userOrgVO);
            userInfo.setCurrentInfo(currentInfo);
            currentInfo.setUserInfo(userInfo);
            userInfoRepository.save(userInfo);
        });
    }

    public UserInfo getUserById(Long id) {
        return userInfoRepository.findById(id).orElse(null);
    }

    public void changeCurrentOrg(Organization org, Long userId) {
        Optional<UserInfo> userInfoOptional = userInfoRepository.findById(userId);
        userInfoOptional.ifPresent(userInfo -> {
            UserCurrent currentInfo = userInfo.getCurrentInfo();
            if (currentInfo == null) {
                currentInfo = new UserCurrent();
            }
            currentInfo.setCurrentOrg(UserOrgVO.of(org.getCompany().getId(), org.getCompany().getName(), org.getId(), org.getName()));
            userInfo.setCurrentInfo(currentInfo);
            userInfoRepository.save(userInfo);
        });
    }

    public void changeInitOrg(Organization org, Long userId) {
        Optional<UserInfo> userInfoOptional = userInfoRepository.findById(userId);
        userInfoOptional.ifPresent(userInfo -> {
            UserCurrent currentInfo = userInfo.getCurrentInfo();
            if (currentInfo == null) {
                currentInfo = new UserCurrent();
            }
            currentInfo.setCurrentOrg(UserOrgVO.of(org.getCompany().getId(), org.getCompany().getName(), org.getId(), org.getName()));
            userInfo.setCurrentInfo(currentInfo);
            userInfoRepository.save(userInfo);
        });
    }

    public void editUserInfo(EditUserInfoCommand command) {
        Optional<UserInfo> optional = userInfoRepository.findById(command.getUserId());
        if (optional.isPresent()) {
            UserInfo userInfo = optional.get();
            if (StringUtils.isNotBlank(command.getAvatar())) {
                userInfo.setAvatar(command.getAvatar());
            }

            userInfoRepository.save(userInfo);
        }

    }


    public Page<UserInfo> pageQuery(UserInfoQuery query, Pageable pageable) {
        return userInfoRepository.findAll(buildSpecification(query), pageable);
    }


    private Specification<UserInfo> buildSpecification(UserInfoQuery roleQuery) {
        return new Specification<UserInfo>() {
            private static final long serialVersionUID = -7797421010838361430L;

            @Override
            public Predicate toPredicate(Root<UserInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if (roleQuery == null) {
                    return predicate;
                }
                if (StringUtils.isNotBlank(roleQuery.getName())) {
                    predicate.getExpressions().add(cb.like(root.get("name"), "%" + roleQuery.getName() + "%"));
                }
                if (StringUtils.isNotBlank(roleQuery.getMobile())) {
                    predicate.getExpressions().add(cb.like(root.get("mobile"), "%" + roleQuery.getMobile() + "%"));
                }

//                if (StringUtils.isNotBlank(roleQuery.getDeptId())) {
//                    predicate.getExpressions().add(cb.equal(root.get("currentInfo").get("currentOrg").get("orgId"), roleQuery.getDeptId()));
//                }


                return predicate;
            }
        };
    }


    @Transactional
    public void updateContact(EditContactDTO dto) {
        SecurityUtils.getLoginInfo().flatMap(loginInfo -> userInfoRepository.findById(loginInfo.getId()))
                .ifPresent(userInfo -> {
                    userInfo.setEmail(dto.getEmail());
                    userInfo.setQqCode(dto.getQqCode());
                    userInfo.setWeChatCode(dto.getWeChatCode());
                    userInfoRepository.save(userInfo);
                });
    }

    public UserInfo saveUser(UserInfo user) {
        return userInfoRepository.save(user);
    }

    public List<UserRelateDetailDTO> relateInfo() {
        LoginInfo loginInfo = SecurityUtils.getLoginInfoOrThrow();
        String subjectType = loginInfo.getCurrentSubjectType();
        if (StrUtil.isBlank(subjectType)) {
            return Lists.newArrayList();
        } else if (SubjectType.org.equals(SubjectType.valueOf(subjectType))) {
            return tenantOrgInfo(loginInfo.getId());
        } else if (SubjectType.supplier.equals(SubjectType.valueOf(subjectType))) {
            return supplierRefOrgInfo(loginInfo.getId());
        } else {
            return Lists.newArrayList();
        }
    }

    public List<UserRelateDetailDTO> tenantOrgInfo(Long userId) {
        List<OrgEmployee> employees = orgEmployeeRepository.findAllByUserUserId(userId);
        if (CollectionUtil.isNotEmpty(employees)) {
            return employees.stream().map(employee -> UserRelateDetailDTO.builder()
                    .userId(employee.getUser().getUserId())
                    .userName(employee.getName())
                    .mobile(employee.getMobile())
                    .subjectId(employee.getOrg().getId())
                    .subjectType(SubjectType.org.name())
                    .orgName(employee.getOrg().getName())
                    .position(employee.getPosition())
                    .jobNo(employee.getJobNo())
                    .enabled(employee.isEnabled())
                    .build()).collect(Collectors.toList());
        }
        return Lists.newArrayList();
    }

    public List<UserRelateDetailDTO> supplierRefOrgInfo(Long userId) {
        List<SupplierStaff> supplierStaffs = supplierStaffRepository.findAllByUserUserId(userId);
        if (CollectionUtil.isNotEmpty(supplierStaffs)) {
            return supplierStaffs.stream().map(staff -> UserRelateDetailDTO.builder()
                    .userId(staff.getUser().getUserId())
                    .userName(staff.getName())
                    .mobile(staff.getMobile())
                    .position(staff.getPosition())
                    .jobNo(staff.getJobNo())
                    .orgName(staff.getSupplier().getName())
                    .subjectId(staff.getSupplier().getId())
                    .subjectType(SubjectType.supplier.name())
                    .enabled(staff.isEnabled())
                    .build()).collect(Collectors.toList());
        }
        return Lists.newArrayList();
    }


    public UserRelateDetailDTO relateInfoEdit(Long userId, String subjectType, Long subjectId) {
        UserRelateDetailDTO.UserRelateDetailDTOBuilder builder = UserRelateDetailDTO.builder();
        if (SubjectType.org.equals(SubjectType.valueOf(subjectType))) {
            Optional<OrgEmployee> orgEmployeeOptional = orgEmployeeRepository.findByUser_UserIdAndOrg_Id(userId, subjectId);
            orgEmployeeOptional.ifPresent(employee -> {
                builder.userId(employee.getUser().getUserId())
                        .userName(employee.getName())
                        .subjectId(employee.getOrg().getId())
                        .orgName(employee.getOrg().getName())
                        .mobile(employee.getMobile())
                        .subjectType(SubjectType.org.name())
                        .jobNo(employee.getJobNo())
                        .position(employee.getPosition())
                        .enabled(employee.isEnabled());
            });
        } else if (SubjectType.supplier.equals(SubjectType.valueOf(subjectType))) {
            Optional<SupplierStaff> supplierStaffOptional = supplierStaffRepository.findAllByUser_UserIdAndSupplier_Id(userId, subjectId);
            supplierStaffOptional.ifPresent(staff -> {
                builder.userId(staff.getUser().getUserId())
                        .userName(staff.getName())
                        .position(staff.getPosition())
                        .orgName(staff.getSupplier().getName())
                        .mobile(staff.getMobile())
                        .jobNo(staff.getJobNo())
                        .subjectId(staff.getSupplier().getId())
                        .subjectType(SubjectType.supplier.name())
                        .enabled(staff.isEnabled());
            });
        }
        return builder.build();
    }

    public void relateInfoUpdate(UserRelateInfoUpdateCommand command) {
        UserRelateInfoUpdate userRelateInfoUpdate = new UserRelateInfoUpdate(
                command.getUserId(),
                command.getSubjectType(),
                command.getSubjectId(),
                command.getPosition(),
                command.getJobNo());
        applicationEventPublisher.publishEvent(new UserRelateInfoUpdateEvent(this, userRelateInfoUpdate));
    }


    private void putSupplierMap(LoginInfo loginInfo, PermissionInfoDTO permissionInfoDTO) {
        List<Supplier> supplierList = loginInfo.getSuppliers();
        if (CollectionUtil.isNotEmpty(supplierList)) {
            List<Map<String, Object>> suppliers = supplierList.stream().map(supplier -> MapUtil.<String, Object>builder()
                    .put("id", supplier.getSupplierId())
                    .put("name", supplier.getSupplierName())
                    .build()).collect(Collectors.toList());
            permissionInfoDTO.setSuppliers(suppliers);
        }
    }

    private void putOrgMap(LoginInfo loginInfo, PermissionInfoDTO permissionInfoDTO) {
        List<Org> orgList = loginInfo.getOrgs();
        if (CollectionUtil.isNotEmpty(orgList)) {
            List<Map<String, Object>> orgs = orgList.stream().map(org -> MapUtil.<String, Object>builder()
                    .put("id", org.getId())
                    .put("name", org.getName())
                    .build()).collect(Collectors.toList());
            permissionInfoDTO.setOrgs(orgs);
        }
    }

    private void putRoleMap(PermissionInfoDTO permissionInfoDTO, List<Role> roleList) {
        if (CollectionUtil.isNotEmpty(roleList)) {
            List<Map<String, Object>> roles = roleList.stream().map(role -> MapUtil.<String, Object>builder()
                    .put("id", role.getId())
                    .put("name", role.getName())
                    .build()).collect(Collectors.toList());
            permissionInfoDTO.setRoles(roles);
        }
    }

    private void putMenuNavMap(List<MenuNavDto> menuNavList, PermissionInfoDTO permissionInfoDTO) {
        if (CollectionUtil.isNotEmpty(menuNavList)) {
            List<Map<String, Object>> menuNavs = menuNavList.stream().map(menuNav -> MapUtil.<String, Object>builder()
                    .put("id", menuNav.getId())
                    .put("title", menuNav.getTitle())
                    .put("menuNavType", menuNav.getMenuNavType())
                    .build()).collect(Collectors.toList());
            permissionInfoDTO.setMenuNavs(menuNavs);
        }
    }

    private void putMenuMap(String currentMenuNav, PermissionInfoDTO permissionInfoDTO, List<Menu> menuList) {
        //菜单
        if (CollectionUtil.isNotEmpty(menuList)) {
            menuList = menuList.stream().filter(menu -> menu.getMenuNavType().equals(MenuNavType.valueOf(currentMenuNav))).collect(Collectors.toList());
            List<Map<String, Object>> menus = menuList.stream().map(m -> MapUtil.<String, Object>builder()
                    .put("id", m.getId())
                    .put("name", m.getName())
                    .put("route", m.getRoute())
                    .put("title", m.getTitle())
                    .build()).collect(Collectors.toList());
            permissionInfoDTO.setMenus(menus);
        }
    }


}
