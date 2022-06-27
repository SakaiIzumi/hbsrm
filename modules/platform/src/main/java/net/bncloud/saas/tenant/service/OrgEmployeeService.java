package net.bncloud.saas.tenant.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import net.bncloud.api.feign.saas.user.SubjectType;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.base.service.BaseService;
import net.bncloud.common.exception.ApiException;
import net.bncloud.common.exception.BizException;
import net.bncloud.enums.DimensionType;
import net.bncloud.saas.authorize.domain.QDataGrant;
import net.bncloud.saas.authorize.domain.QRole;
import net.bncloud.saas.authorize.domain.Role;
import net.bncloud.common.security.LoginInfo;
import net.bncloud.common.security.Org;
import net.bncloud.common.security.SecurityUtils;
import net.bncloud.saas.authorize.repository.RoleRepository;
import net.bncloud.saas.event.CreatedEmployee;
import net.bncloud.saas.event.OrgEmployeeCreatedEvent;
import net.bncloud.saas.result.SaaSResultCode;
import net.bncloud.saas.supplier.repository.SupplierStaffRepository;
import net.bncloud.saas.tenant.domain.*;
import net.bncloud.saas.tenant.domain.vo.*;
import net.bncloud.saas.tenant.repository.OrgDepartmentRepository;
import net.bncloud.saas.tenant.repository.OrgEmployeeRepository;
import net.bncloud.saas.tenant.repository.OrgManagerRepository;
import net.bncloud.saas.tenant.repository.OrganizationRepository;
import net.bncloud.saas.tenant.service.command.CreateOrgEmployeeCommand;
import net.bncloud.saas.tenant.service.command.EditBatchOrgEmployeeCommand;
import net.bncloud.saas.tenant.service.command.EditOrgEmployeeCommand;
import net.bncloud.saas.tenant.service.command.EditUserInfoCommand;
import net.bncloud.saas.tenant.service.dto.*;
import net.bncloud.saas.tenant.service.mapstruct.OrgEmployeeMapper;
import net.bncloud.saas.tenant.service.query.OrgEmployeeQuery;
import net.bncloud.saas.tenant.service.query.OrgMemberQuery;
import net.bncloud.saas.user.domain.QUserInfo;
import net.bncloud.saas.user.domain.UserCurrent;
import net.bncloud.saas.user.domain.UserInfo;
import net.bncloud.saas.user.domain.vo.SupplierVO;
import net.bncloud.saas.user.domain.vo.UserOrgVO;
import net.bncloud.saas.user.service.UserInfoService;
import net.bncloud.saas.user.service.command.UserCreateCommand;
import net.bncloud.saas.utils.pageable.PageUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName OrgEmployeeService
 * @Description: 部门员工service
 * @Author Administrator
 * @Date 2021/4/22
 * @Version V1.0
 **/
@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class OrgEmployeeService extends BaseService {
    private final JPAQueryFactory queryFactory;
    private final OrganizationRepository organizationRepository;
    private final OrgEmployeeRepository orgEmployeeRepository;
    private final OrgManagerRepository orgManagerRepository;
    private final OrgEmployeeMapper orgEmployeeMapper;
    private final OrgDepartmentRepository orgDepartmentRepository;
    private final RoleRepository roleRepository;

    private final UserInfoService userInfoService;
    private final QOrgEmployee qOrgEmployee = QOrgEmployee.orgEmployee;
    private final QRole qRole = QRole.role;

    public Page<OrgEmployee> pageQuery(OrgEmployeeQuery param, Pageable pageable) {
        Page<OrgEmployee> all = orgEmployeeRepository.findAll((Specification<OrgEmployee>) (root, criteriaQuery, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            if (param == null) {
                return predicate;
            }
            if (StringUtils.isNotBlank(param.getDeptId())) {
                predicate.getExpressions().add(criteriaBuilder.equal(root.get("department").get("id"), param.getDeptId()));
            }
            if (StringUtils.isNotBlank(param.getName())) {
                predicate.getExpressions().add(criteriaBuilder.like(root.get("name"), "%" + param.getName() + "%"));
            }
            if (StringUtils.isNotBlank(param.getJobNo())) {
                predicate.getExpressions().add(criteriaBuilder.like(root.get("jobNo"), "%" + param.getJobNo() + "%"));
            }
            if (StringUtils.isBlank(param.getJobNo()) && StringUtils.isBlank(param.getName()) && StringUtils.isNotBlank(param.getQs())) {
                Predicate or = criteriaBuilder.or(criteriaBuilder.like(root.get("name"), "%" + param.getQs() + "%"),
                        criteriaBuilder.like(root.get("jobNo"), "%" + param.getQs() + "%"),
                        criteriaBuilder.like(root.get("mobile"), "%" + param.getQs() + "%")
                );

                return or;
            }

            return predicate;
        }, pageable);
        return all;
    }

    public Page<OrgEmployeeDTO> managePageQuery(QueryParam<OrgEmployeeQuery> query, Pageable pageable) {
        OrgEmployeeQuery param = query.getParam();
        String searchValue = query.getSearchValue();
        Page<OrgEmployeeDTO> all = orgEmployeeRepository.findAll((Specification<OrgEmployee>) (root, criteriaQuery, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            if (param == null) {
                return predicate;
            }
            if (StringUtils.isNotBlank(param.getName())) {
                predicate.getExpressions().add(criteriaBuilder.like(root.get("name"), "%" + param.getName() + "%"));
            }

            if (StringUtils.isNotBlank(param.getMobile())) {
                predicate.getExpressions().add(criteriaBuilder.like(root.get("mobile"), "%" + param.getMobile() + "%"));
            }
            if (StringUtils.isNotBlank(param.getDeptId())) {
                predicate.getExpressions().add(criteriaBuilder.equal(root.get("department").get("id"), param.getDeptId()));
            }

            if (StringUtils.isNotBlank(param.getJobNo())) {
                predicate.getExpressions().add(criteriaBuilder.like(root.get("jobNo"), "%" + param.getJobNo() + "%"));
            }


            if (param.getEnabled() != null) {
                predicate.getExpressions().add(criteriaBuilder.equal(root.get("enabled"), param.getEnabled()));
            }

            if (StringUtils.isNotBlank(searchValue)) {
                Predicate or = criteriaBuilder.or(criteriaBuilder.like(root.get("name"), "%" + searchValue + "%"),
                        criteriaBuilder.like(root.get("jobNo"), "%" + searchValue + "%"),
                        criteriaBuilder.like(root.get("mobile"), "%" + searchValue + "%")
                );

                predicate.getExpressions().add(or);
            }

            return predicate;
        }, pageable).map(orgEmployeeMapper::toDto);
        return all;
    }

    /**
     * 获取员工详情
     *
     * @param employeeId 员工ID
     * @return 员工详情
     */
    public OrgEmployeeDTO get(Long employeeId) {
        OrgEmployee orgEmployee = orgEmployeeRepository.findById(employeeId).orElseThrow(() -> new BizException(SaaSResultCode.EMPLOYEE_NOT_EXIST_ERROR));
        OrgEmployeeDTO employeeDTO = orgEmployeeMapper.toDto(orgEmployee);
        if (orgEmployee.getUser() != null) {
            employeeDTO.setUserInfo(userInfoService.getUserById(orgEmployee.getUser().getUserId()));
        }
        return employeeDTO;
    }

    public OrgEmployee findById(Long id) {
        return orgEmployeeRepository.findById(id).orElseThrow(() -> new BizException(SaaSResultCode.EMPLOYEE_NOT_EXIST_ERROR));
    }

    /**
     * 获取员工详情
     *
     * @param userId 员工ID
     * @return 员工详情
     */
    public OrgEmployeeDTO getPopoverInfo(Long userId) {
        OrgEmployee orgEmployee = orgEmployeeRepository.findByUserId(userId).orElseThrow(() -> new BizException(SaaSResultCode.EMPLOYEE_NOT_EXIST_ERROR));
        OrgEmployeeDTO employeeDTO = orgEmployeeMapper.toDto(orgEmployee);
        if (orgEmployee.getUser() != null) {
            employeeDTO.setUserInfo(userInfoService.getUserById(orgEmployee.getUser().getUserId()));
        }
        return employeeDTO;
    }

    /**
     * 新增员工
     */
    @Transactional(rollbackFor = Exception.class)
    public void addEmployee(CreateOrgEmployeeCommand command) {
        Org org = SecurityUtils.getCurrentOrg().orElseThrow(() -> new ApiException(404, "组织不存在"));
        Optional<OrgEmployee> employeeOptional = orgEmployeeRepository.findByMobileAndOrg_Id(command.getMobile(), org.getId());
        if (employeeOptional.isPresent()) {
            throw new ApiException(400, "该成员已存在");
        }

        UserInfo user = userInfoService.getUserByMobile(command.getMobile(), "");
        if (user == null) {
            user = userInfoService.createActiveUser(UserCreateCommand.of(command.getName(), command.getMobile(), "86", "swy20210401")); // fixme
        }
        UserId userId = UserId.of(user.getId());
        Optional<LoginInfo> optional = SecurityUtils.getLoginInfo();
        String createdByName = null;
        if (optional.isPresent()) {
            LoginInfo loginInfo = optional.get();
            createdByName = loginInfo.getName();
        }
        OrgEmployee orgEmployee = command.create(userId, createdByName);
        List<Long> roleIds = command.getRoleIds();
        orgEmployee.setCode(user.getCode());
        orgEmployee.setOrg(new Organization(org.getId()));
        orgEmployee = orgEmployeeRepository.save(orgEmployee);
        if (user.getCurrentInfo() == null) {
            user.setCurrentInfo(new UserCurrent());
            user.getCurrentInfo().setUserInfo(user);
            user.getCurrentInfo().setUserId(user.getId());
        }
        final UserCurrent currentInfo = user.getCurrentInfo();
        currentInfo.setCurrentOrg(UserOrgVO.of(org.getCompanyId(), org.getCompanyName(), org.getId(), org.getName()));
        userInfoService.saveUser(user);
        CreatedEmployee createdEmployee = new CreatedEmployee();
        createdEmployee.setId(orgEmployee.getId());
        createdEmployee.setRoleIds(roleIds);
        applicationEventPublisher.publishEvent(new OrgEmployeeCreatedEvent(this, createdEmployee));

    }

    /**
     * 批量编辑员工
     */
    @Transactional(rollbackFor = Exception.class)
    public void editBatchEmployee(EditBatchOrgEmployeeCommand command) {
        List<Long> employeeIds = command.getEmployeeIds();
        for (Long employeeId : employeeIds) {
            Optional<OrgEmployee> optional = orgEmployeeRepository.findById(employeeId);
            if (optional.isPresent()) {
                OrgEmployee orgEmployee = optional.get();
                orgEmployee.setDepartment(command.createDepartment());
                if (StringUtils.isNotBlank(command.getPosition())) {
                    orgEmployee.setPosition(command.getPosition());
                }
                orgEmployeeRepository.save(orgEmployee);
            }
        }
    }

    /**
     * 编辑员工
     *
     * @param command 请求参数
     */
    public void edit(EditOrgEmployeeCommand command) {
        Optional<OrgEmployee> optional = orgEmployeeRepository.findById(command.getEmployeeId());
        if (optional.isPresent()) {
            OrgEmployee orgEmployee = optional.get();
            if (command.getEnabled() != null) {
                orgEmployee.setEnabled(command.getEnabled());
            }

            orgEmployeeRepository.save(orgEmployee);
        }
    }

    public void editEmpUserInfo(EditUserInfoCommand command) {
        userInfoService.editUserInfo(command);
    }


    public List<RelateOrgTreeDTO> relateOrgTree() {
        Long id = SecurityUtils.getLoginInfoOrThrow().getId();
        List<Organization> orgs = organizationRepository.findAllByUserId(id);
        return orgs.stream().map(r -> {
            RelateOrgTreeDTO singleTree = RelateOrgTreeDTO.builder().id(r.getId()).name(r.getName()).children(new ArrayList<>()).build();
            RelateOrgTreeDTO purchaser = RelateOrgTreeDTO.builder().id(RandomUtil.randomLong()).parentId(r.getId()).name("采购组织").type(SubjectType.org.name()).build();
            RelateOrgTreeDTO supplier = RelateOrgTreeDTO.builder().id(RandomUtil.randomLong()).parentId(r.getId()).name("供应商").type(SubjectType.supplier.name()).build();
            singleTree.getChildren().add(purchaser);
            singleTree.getChildren().add(supplier);
            return singleTree;
        }).collect(Collectors.toList());
    }

    public Page orgMemberTable(QueryParam<OrgMemberQuery> queryParam, Pageable pageable) {
        Optional<Org> currentOrg = SecurityUtils.getCurrentOrg();
        OrgMemberQuery param = queryParam.getParam();
        String searchValue = queryParam.getSearchValue();
        if (currentOrg.isPresent()) {
            Org org = currentOrg.get();
            QOrgEmployee orgEmployee = QOrgEmployee.orgEmployee;
            Long id = org.getId();
            if (id != null) {
                com.querydsl.core.types.Predicate predicate = ExpressionUtils.anyOf();
                if (StrUtil.isNotBlank(searchValue)) {

                    com.querydsl.core.types.Predicate or = orgEmployee.code.like("%" + searchValue + "%");
                    or = ExpressionUtils.or(or, orgEmployee.name.like("%" + searchValue + "%"));
                    or = ExpressionUtils.or(or, orgEmployee.mobile.like("%" + searchValue + "%"));
                    predicate = ExpressionUtils.and(predicate,or);

                } else {
                    predicate = StrUtil.isBlank(param.getName()) ? predicate : ExpressionUtils.or(predicate, orgEmployee.name.like("%" + param.getName() + "%"));
                    predicate = StrUtil.isBlank(param.getMobile()) ? predicate : ExpressionUtils.or(predicate, orgEmployee.mobile.like("%" + param.getMobile() + "%"));
                    predicate = StrUtil.isBlank(param.getCode()) ? predicate : ExpressionUtils.or(predicate, orgEmployee.code.like("%" + param.getCode() + "%"));
                    predicate = ObjectUtil.isEmpty(param.getEnabled()) ? predicate : ExpressionUtils.or(predicate, orgEmployee.enabled.eq(param.getEnabled()));
                }
                predicate = ExpressionUtils.and(predicate, orgEmployee.org.id.eq(id));
                QueryResults<OrgEmpDTO> queryResults = queryFactory.select(
                        Projections.bean(OrgEmpDTO.class,
                                orgEmployee.id,
                                orgEmployee.name,
                                orgEmployee.user.userId.as("userId"),
                                orgEmployee.code,
                                orgEmployee.mobile,
                                orgEmployee.org.id.as("orgId"),
                                orgEmployee.org.name.as("orgName")
                        )
                ).from(orgEmployee)
                        .where(predicate)
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetchResults();
                return PageUtils.of(queryResults, pageable);
            }

        }
        return PageUtils.empty();
    }

    public void batchRemovePurchaserMember(RemoveOrgMemberVO vo) {
        Optional<Org> currentOrg = SecurityUtils.getCurrentOrg();
        if (currentOrg.isPresent()) {
            Org org = currentOrg.get();
            List<OrgEmployee> employees = orgEmployeeRepository.findAllByOrg_IdAndUser_UserIdIn(org.getId(), vo.getUserIds());
            List<OrgManager> managers = orgManagerRepository.findAllByOrg_IdAndUser_IdIn(org.getId(), vo.getUserIds());
            orgEmployeeRepository.deleteAll(employees);
            orgManagerRepository.deleteAll(managers);
        }
    }

    public UserDetailDTO memberDetail(Long id) {
        Optional<OrgEmployee> employeeOptional = orgEmployeeRepository.findById(id);
        if (employeeOptional.isPresent()) {
            OrgEmployee orgEmployee = employeeOptional.get();
            UserInfo userInfo = userInfoService.getUserById(orgEmployee.getUser().getUserId());
            UserDetailDTO userDetailDTO = UserDetailDTO.builder().build();
            UserInfDTO userInfDTO = UserInfDTO.builder()
                    .userId(userInfo.getId())
                    .avatar(userInfo.getAvatar())
                    .userName(userInfo.getName())
                    .userCode(userInfo.getCode())
                    .mobile(userInfo.getMobile())
                    .email(userInfo.getEmail())
                    .wechatCode(userInfo.getWeChatCode())
                    .dingTalkCode(userInfo.getDingTalkCode())
                    .qqCode(userInfo.getQqCode())
                    .build();
            userDetailDTO.setUserInfo(userInfDTO);
            List<UserDetailRefDTO> detailRefs = Lists.newArrayList();
            userDetailDTO.setOrgInfo(detailRefs);
            List<OrgEmployee> employees = orgEmployeeRepository.findAllByUserUserId(userInfo.getId());
            if (CollectionUtil.isNotEmpty(employees)) {
                List<UserDetailRefDTO> collect = employees.stream().map(employee -> {
                    List<Role> roles = employee.getRoles();
                    List<String> roleNames = Lists.newArrayList();
                    if (CollectionUtil.isNotEmpty(roles)) {
                        roleNames = roles.stream().map(Role::getName).distinct().collect(Collectors.toList());
                    }
                    Organization org = employee.getOrg();
                    return UserDetailRefDTO.builder()
                            .id(employee.getId())
                            .name(employee.getName())
                            .subjectId(org.getId())
                            .subjectType(SubjectType.org.name())
                            .orgName(org.getName())
                            .roleName(roleNames)
                            .position(employee.getPosition())
                            .jobNo(employee.getJobNo())
                            .enabled(employee.isEnabled())
                            .build();
                }).collect(Collectors.toList());
                detailRefs.addAll(collect);
            }
            return userDetailDTO;
        }
        return UserDetailDTO.builder().build();
    }

    public void createMember(BatchZltPurchaserStaffVO vo) {
        Optional<Org> currentOrg = SecurityUtils.getCurrentOrg();
        if (currentOrg.isPresent()) {
            Org org = currentOrg.get();
            Optional<Organization> organizationOptional = organizationRepository.findById(org.getId());
            Organization organization = organizationOptional.get();
            List<ZltPurchaserStaffVO> purchaserUsers = vo.getPurchaserUsers();
            List<Long> roleIds = vo.getRoleIds();
            List<Role> roles = roleRepository.findAllById(roleIds);
            for (ZltPurchaserStaffVO purchaserUser : purchaserUsers) {
                UserInfo user = userInfoService.getUserByMobile(purchaserUser.getMobile(), "");
                if (user == null) {
                    user = userInfoService.createActiveUser(UserCreateCommand.of(purchaserUser.getName(), purchaserUser.getMobile(), "86", "swy20210401")); // fixme
                }
                UserId userId = UserId.of(user.getId());
                Optional<LoginInfo> optional = SecurityUtils.getLoginInfo();
                String createdByName = null;
                if (optional.isPresent()) {
                    LoginInfo loginInfo = optional.get();
                    createdByName = loginInfo.getName();
                }
                OrgEmployee orgEmployee = createOrgEmployee(userId, purchaserUser.getName(), purchaserUser.getMobile(), createdByName);
                orgEmployee.setOrg(organization);
                orgEmployee.setRoles(roles);
                orgEmployee.setCode(user.getCode());
                orgEmployeeRepository.save(orgEmployee);
            }
        }
    }

    public List<Organization> loadRelateOrgs() {
        LoginInfo loginInfo = SecurityUtils.getLoginInfoOrThrow();
        return organizationRepository.findAllByUserId(loginInfo.getId());
    }

    private OrgEmployee createOrgEmployee(UserId user, String name, String mobile, String createdByName) {
        OrgEmployee orgEmployee = new OrgEmployee();
        orgEmployee.setName(name);
        orgEmployee.setMobile(mobile);
        orgEmployee.setCreatedByName(createdByName);
        orgEmployee.setUser(user);
        orgEmployee.setEnabled(true);
        return orgEmployee;
    }


    public Object editInfo(Long employeeId) {
        OrgEmployee orgEmployee = orgEmployeeRepository.findById(employeeId).orElseThrow(() -> new BizException(SaaSResultCode.EMPLOYEE_NOT_EXIST_ERROR));
        OrgEmployeeDTO orgEmployeeDTO = orgEmployeeMapper.toDto(orgEmployee);
        orgEmployeeDTO.setOrgName(orgEmployee.getOrg().getName());
        List<Long> roleIds = orgEmployee.getRoles().stream().map(Role::getId).distinct().collect(Collectors.toList());
        orgEmployeeDTO.setRoleIds(roleIds);
        return orgEmployeeDTO;
    }

    public void updateInfo(UserMemberVO vo) {
        OrgEmployee orgEmployee = orgEmployeeRepository.findById(vo.getId()).orElseThrow(() -> new BizException(SaaSResultCode.EMPLOYEE_NOT_EXIST_ERROR));
        orgEmployee.setEnabled(vo.getEnabled());
        List<Long> roleIds = vo.getRoleIds();
        List<Role> roles = roleRepository.findAllById(roleIds);
        orgEmployee.setPosition(vo.getPosition());
        orgEmployee.setJobNo(vo.getJobNo());
        orgEmployee.setRoles(roles);
        orgEmployee.setEnabled(vo.getEnabled());
        orgEmployeeRepository.save(orgEmployee);
    }

    public void updateInfo(Long userId, Long subjectId, String position, String jobNo) {
        OrgEmployee employee = orgEmployeeRepository.findByUser_UserIdAndOrg_Id(userId, subjectId).orElseThrow(() -> new ApiException(404, "无法找到相关信息"));
        employee.setPosition(position);
        employee.setJobNo(jobNo);
        orgEmployeeRepository.save(employee);

    }

    //有此数据权限的成员信息
    public Page queryPageBydataGrant(OrgEmployeeQuery query, Pageable pageable) {
        String purchaserCode = query.getPurchaserCode();
        Long orgId = query.getOrgId();
        QDataGrant qDataGrant = QDataGrant.dataGrant;
        com.querydsl.core.types.Predicate predicate = ExpressionUtils.anyOf();
        predicate = ExpressionUtils.and(predicate, qDataGrant.dimensionType.eq(DimensionType.purchaser_code.getCode()));
        predicate = ExpressionUtils.and(predicate, qDataGrant.dimensionValue.eq(purchaserCode));
        List<Long> roleIds = queryFactory.select(qRole.id).from(qDataGrant).innerJoin(qDataGrant.roles, qRole).distinct().where(predicate).fetch();
        if (CollectionUtil.isNotEmpty(roleIds)) {
            com.querydsl.core.types.Predicate empPredicate = ExpressionUtils.anyOf();
            empPredicate = ExpressionUtils.and(empPredicate, qOrgEmployee.org.id.eq(orgId));
            empPredicate = ExpressionUtils.and(empPredicate, qRole.id.in(roleIds));
            QueryResults<OrgEmployee> orgEmployeeDTOQueryResults = queryFactory.select(qOrgEmployee).from(qOrgEmployee).innerJoin(qOrgEmployee.roles, qRole)
                    .where(empPredicate)
                    .fetchResults();
            List<OrgEmployeeDTO> orgEmployeeDTOS = orgEmployeeDTOQueryResults.getResults().stream().map(orgEmployeeMapper::toDto).collect(Collectors.toList());
            return PageUtils.of(orgEmployeeDTOS, pageable, orgEmployeeDTOQueryResults.getTotal());
        }
        return PageUtils.empty();
    }

    @Transactional(rollbackFor = Exception.class)
    public void save(OrgEmployee orgEmployee) {
        orgEmployeeRepository.save(orgEmployee);
    }
}

