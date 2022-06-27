package net.bncloud.saas.supplier.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import net.bncloud.api.feign.saas.user.SubjectType;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.base.service.BaseService;
import net.bncloud.common.exception.ApiException;
import net.bncloud.common.exception.BizException;
import net.bncloud.saas.authorize.service.RoleService;
import net.bncloud.saas.event.CreateSupplierStaff;
import net.bncloud.saas.event.SupplierStaffCreateEvent;
import net.bncloud.saas.tenant.service.dto.UserDetailRefDTO;
import net.bncloud.saas.utils.pageable.PageUtils;
import net.bncloud.common.security.LoginInfo;
import net.bncloud.common.security.Org;
import net.bncloud.common.security.SecurityUtils;
import net.bncloud.common.security.Supplier;
import net.bncloud.saas.authorize.domain.Role;
import net.bncloud.saas.result.SaaSResultCode;
import net.bncloud.saas.supplier.domain.QSupplierStaff;
import net.bncloud.saas.supplier.domain.SupplierStaff;
import net.bncloud.saas.supplier.domain.vo.SupplierStaffVO;
import net.bncloud.saas.supplier.repository.SupplierRepository;
import net.bncloud.saas.supplier.repository.SupplierStaffRepository;
import net.bncloud.saas.supplier.service.dto.SupplierStaffDTO;
import net.bncloud.saas.supplier.service.mapper.SupplierStaffMapper;
import net.bncloud.saas.supplier.vo.UserStaffVO;
import net.bncloud.saas.tenant.domain.Organization;
import net.bncloud.saas.tenant.domain.vo.UserId;
import net.bncloud.saas.supplier.service.dto.UserDetailDTO;
import net.bncloud.saas.supplier.service.dto.UserInfDTO;
import net.bncloud.saas.supplier.service.query.SupplierStaffQuery;
import net.bncloud.saas.user.domain.QUserInfo;
import net.bncloud.saas.user.domain.UserInfo;
import net.bncloud.saas.user.service.UserInfoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SupplierStaffService extends BaseService {

    private final UserInfoService userInfoService;
    private final SupplierStaffRepository supplierStaffRepository;
    private final SupplierRepository supplierRepository;
    private final SupplierStaffMapper supplierStaffMapper;
    private final RoleService roleService;
    private final JPAQueryFactory queryFactory;


    public Object supplierStaffTableByOrgId(QueryParam<SupplierStaffQuery> queryParam, Pageable pageable) {
        Optional<Org> currentOrg = SecurityUtils.getCurrentOrg();
        if (currentOrg.isPresent()) {
            QSupplierStaff qSupplierStaff = QSupplierStaff.supplierStaff;
            String searchValue = queryParam.getSearchValue();
            SupplierStaffQuery param = queryParam.getParam();
            Predicate predicate = ExpressionUtils.anyOf();
            if (StrUtil.isNotEmpty(searchValue)) {
                searchValue = "%" + searchValue + "%";

                com.querydsl.core.types.Predicate or = qSupplierStaff.code.like(searchValue );
                or = ExpressionUtils.or(or, qSupplierStaff.name.like( searchValue ));
                or = ExpressionUtils.or(or, qSupplierStaff.mobile.like( searchValue));
                predicate = ExpressionUtils.and(predicate,or);

            } else {
                predicate = StrUtil.isBlank(param.getCode()) ? predicate : ExpressionUtils.and(predicate, qSupplierStaff.code.like("%" + param.getCode() + "%"));
                predicate = StrUtil.isBlank(param.getName()) ? predicate : ExpressionUtils.and(predicate, qSupplierStaff.name.like("%" + param.getName() + "%"));
                predicate = StrUtil.isBlank(param.getMobile()) ? predicate : ExpressionUtils.and(predicate, qSupplierStaff.mobile.like(param.getMobile()));
                predicate = StrUtil.isBlank(param.getSupplierCode()) ? predicate : ExpressionUtils.and(predicate, qSupplierStaff.supplier.code.eq(param.getSupplierCode()));
                predicate = ObjectUtil.isEmpty(param.getSupplierId()) ? predicate : ExpressionUtils.and(predicate, qSupplierStaff.supplier.id.eq(param.getSupplierId()));
            }
            Org org = currentOrg.get();
            Organization organization = new Organization();
            organization.setId(org.getId());
            predicate = ExpressionUtils.and(predicate, qSupplierStaff.supplier.organizations.contains(organization));
            QueryResults<SupplierStaffDTO> queryResults = queryFactory.select(
                    Projections.bean(
                            SupplierStaffDTO.class,
                            qSupplierStaff.id,
                            qSupplierStaff.name,
                            qSupplierStaff.code,
                            qSupplierStaff.mobile,
                            qSupplierStaff.id.as("staffId"),
                            qSupplierStaff.supplier.id.as("supplierId"),
                            qSupplierStaff.supplier.name.as("supplierName")
                    )
            ).distinct().from(qSupplierStaff)
                    .where(predicate)
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetchResults();

            return PageUtils.of(queryResults, pageable);
        }
        return PageUtils.empty();
    }

    public Page queryPage(QueryParam<SupplierStaffQuery> queryParam, Pageable pageable) {
        QUserInfo qUserInfo = QUserInfo.userInfo;
        QSupplierStaff qSupplierStaff = QSupplierStaff.supplierStaff;
        Predicate predicate = ExpressionUtils.anyOf();
        SupplierStaffQuery param = queryParam.getParam();
        predicate = StrUtil.isBlank(param.getSupplierCode()) ? predicate : ExpressionUtils.and(predicate, qSupplierStaff.supplier.code.eq(param.getSupplierCode()));
        QueryResults<SupplierStaffDTO> queryResults = queryFactory.select(Projections.bean(
                SupplierStaffDTO.class,
                qSupplierStaff.id,
                qSupplierStaff.name,
                qSupplierStaff.code,
                qSupplierStaff.mobile,
                qSupplierStaff.supplier.id.as("supplierId"),
                qSupplierStaff.supplier.name.as("supplierName")
        )).distinct().from(qSupplierStaff)
                .innerJoin(qUserInfo)
                .on(qUserInfo.id.eq(qSupplierStaff.user.userId))
                .where(predicate)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
        return PageUtils.of(queryResults, pageable);
    }

    public Page<?> supplierMemberTable(QueryParam<SupplierStaffQuery> queryParam, Pageable pageable) {
        LoginInfo loginInfo = SecurityUtils.getLoginInfoOrThrow();
        Supplier currentSupplier = loginInfo.getCurrentSupplier();
        if (currentSupplier != null) {
            Long supplierId = currentSupplier.getSupplierId();
            QSupplierStaff qSupplierStaff = QSupplierStaff.supplierStaff;
            String searchValue = queryParam.getSearchValue();
            SupplierStaffQuery param = queryParam.getParam();
            Predicate predicate = ExpressionUtils.anyOf();

            if (StrUtil.isNotBlank(searchValue)) {
                searchValue = "%" + searchValue + "%";
                predicate = ExpressionUtils.or(predicate, qSupplierStaff.code.like(searchValue));
                predicate = ExpressionUtils.or(predicate, qSupplierStaff.name.like(searchValue));
                predicate = ExpressionUtils.or(predicate, qSupplierStaff.mobile.like(searchValue));
            } else {
                predicate = StrUtil.isBlank(param.getCode()) ? predicate : ExpressionUtils.and(predicate, qSupplierStaff.code.like("%" + param.getCode() + "%"));
                predicate = StrUtil.isBlank(param.getName()) ? predicate : ExpressionUtils.and(predicate, qSupplierStaff.name.like("%" + param.getName() + "%"));
                predicate = StrUtil.isBlank(param.getMobile()) ? predicate : ExpressionUtils.and(predicate, qSupplierStaff.mobile.like("%" + param.getMobile() + "%"));
            }
            predicate = ExpressionUtils.and(predicate, qSupplierStaff.supplier.id.eq(supplierId));

            QueryResults<SupplierStaffDTO> queryResults = queryFactory.select(Projections.bean(
                    SupplierStaffDTO.class,
                    qSupplierStaff.id,
                    qSupplierStaff.code,
                    qSupplierStaff.name,
                    qSupplierStaff.mobile,
                    qSupplierStaff.user.userId.as("userId"),
                    qSupplierStaff.enabled,
                    qSupplierStaff.supplier.id.as("supplierId"),
                    qSupplierStaff.supplier.name.as("supplierName")
            )).distinct().from(qSupplierStaff)
                    .where(predicate)
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetchResults();
            return PageUtils.of(queryResults, pageable);
        }
        return PageUtils.empty();
    }

    @Transactional(rollbackFor = Exception.class)
    public void createMember(SupplierStaffVO vo) {
        Optional<net.bncloud.saas.supplier.domain.Supplier> supplierOptional = supplierRepository.findOneByCode(vo.getSupplierCode());
        if (!supplierOptional.isPresent()) {
            throw new ApiException(404, "供应商信息不存在");
        }
        Optional<SupplierStaff> supplierStaffOptional = supplierStaffRepository.findByMobileAndSupplier_Code(vo.getMobile(), vo.getSupplierCode());
        if (supplierStaffOptional.isPresent()) {
            throw new ApiException(400, "用户已存在");
        }
        net.bncloud.saas.supplier.domain.Supplier currentSupplier = supplierOptional.get();
        applicationEventPublisher.publishEvent(new SupplierStaffCreateEvent(this, CreateSupplierStaff.of(vo.getName(), vo.getMobile(), currentSupplier.getId(), vo.getRoleIds(), vo.getEnabled())));
    }


    @Transactional(rollbackFor = Exception.class)
    public void createMemberWithRole(SupplierStaffVO vo) {
        LoginInfo loginInfo = SecurityUtils.getLoginInfoOrThrow();
        Supplier currentSupplier = loginInfo.getCurrentSupplier();
        Optional<SupplierStaff> supplierStaffOptional = supplierStaffRepository.findByMobileAndSupplier_Code(vo.getMobile(), currentSupplier.getSupplierCode());
        if (supplierStaffOptional.isPresent()) {
            throw new ApiException(400, "用户已存在");
        }
        applicationEventPublisher.publishEvent(new SupplierStaffCreateEvent(this, CreateSupplierStaff.of(vo.getName(), vo.getMobile(), currentSupplier.getSupplierId(), vo.getRoleIds(), vo.getEnabled())));
    }


    public UserDetailDTO memberDetail(Long supplierStaffId) {
        Optional<SupplierStaff> supplierStaffOptional = supplierStaffRepository.findById(supplierStaffId);
        if (supplierStaffOptional.isPresent()) {
            SupplierStaff supplierStaff = supplierStaffOptional.get();
            UserId user = supplierStaff.getUser();
            UserInfo userInfo = userInfoService.getUserById(user.getUserId());

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
            List<SupplierStaff> supplierStaffs = supplierStaffRepository.findAllByUserUserId(user.getUserId());
            if (CollectionUtil.isNotEmpty(supplierStaffs)) {
                List<UserDetailRefDTO> collect = supplierStaffs.stream().map(staff -> {
                    List<Role> roles = staff.getRoles();
                    List<String> roleNames = Lists.newArrayList();
                    if (CollectionUtil.isNotEmpty(roles)) {
                        roleNames = roles.stream().map(Role::getName).collect(Collectors.toList());
                    }
                    return UserDetailRefDTO.builder()
                            .id(staff.getId())
                            .name(staff.getName())
                            .roleName(roleNames)
                            .position(staff.getPosition())
                            .jobNo(staff.getJobNo())
                            .orgName(staff.getSupplier().getName())
                            .enabled(staff.isEnabled())
                            .subjectId(staff.getSupplier().getId())
                            .subjectType(SubjectType.supplier.name())
                            .build();
                }).collect(Collectors.toList());
                detailRefs.addAll(collect);
            }
            return userDetailDTO;
        }
        return UserDetailDTO.builder().build();
    }

    @Transactional(rollbackFor = Exception.class)
    public SupplierStaff save(SupplierStaff supplierStaff) {
        return supplierStaffRepository.save(supplierStaff);
    }


    public List<net.bncloud.saas.supplier.domain.Supplier> loadRelateSuppliers() {
        LoginInfo loginInfo = SecurityUtils.getLoginInfoOrThrow();
        return supplierRepository.findAllByUserId(loginInfo.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    public SupplierStaffDTO editInfo(Long supplierStaffId) {
        SupplierStaff supplierStaff = supplierStaffRepository.findById(supplierStaffId).orElseThrow(() -> new BizException(SaaSResultCode.EMPLOYEE_NOT_EXIST_ERROR));
        SupplierStaffDTO supplierStaffDTO = supplierStaffMapper.toDto(supplierStaff);
        supplierStaffDTO.setEnabled(supplierStaff.isEnabled());
        List<Long> roleIds = supplierStaff.getRoles().stream().map(Role::getId).distinct().collect(Collectors.toList());
        supplierStaffDTO.setRoleIds(roleIds);
        return supplierStaffDTO;
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateInfo(UserStaffVO vo) {
        SupplierStaff supplierStaff = supplierStaffRepository.findById(vo.getId()).orElseThrow(() -> new BizException(SaaSResultCode.EMPLOYEE_NOT_EXIST_ERROR));
        List<Long> roleIds = vo.getRoleIds();
        List<Role> roles = roleService.findByIds(roleIds);
        supplierStaff.setPosition(vo.getPosition());
        supplierStaff.setJobNo(vo.getJobNo());
        supplierStaff.setEnabled(vo.getEnabled());
        supplierStaff.setRoles(roles);
        supplierStaffRepository.save(supplierStaff);
    }


    @Transactional(rollbackFor = Exception.class)
    public void updateInfo(Long userId, Long subjectId, String position, String jobNo) {
        SupplierStaff supplierStaff = supplierStaffRepository.findAllByUser_UserIdAndSupplier_Id(userId, subjectId).orElseThrow(() -> new ApiException(404, "无法找到相关信息"));
        supplierStaff.setJobNo(jobNo);
        supplierStaff.setPosition(position);
        supplierStaffRepository.save(supplierStaff);
    }

}
