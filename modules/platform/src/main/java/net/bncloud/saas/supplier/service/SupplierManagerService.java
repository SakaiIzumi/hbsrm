package net.bncloud.saas.supplier.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.constants.ManagerType;
import net.bncloud.common.exception.ApiException;
import net.bncloud.saas.authorize.domain.Role;
import net.bncloud.saas.authorize.domain.RoleGroup;
import net.bncloud.saas.authorize.repository.RoleGroupRepository;
import net.bncloud.saas.user.domain.UserInfo;
import net.bncloud.saas.user.repository.UserInfoRepository;
import net.bncloud.saas.utils.pageable.PageUtils;
import net.bncloud.common.security.LoginInfo;
import net.bncloud.common.security.Org;
import net.bncloud.common.security.SecurityUtils;
import net.bncloud.common.security.Supplier;
import net.bncloud.saas.supplier.domain.QSupplierManager;
import net.bncloud.saas.supplier.domain.SupplierManager;
import net.bncloud.saas.supplier.domain.SupplierStaff;
import net.bncloud.saas.supplier.domain.vo.BatchSupplierManagerVO;
import net.bncloud.saas.supplier.repository.SupplierManagerRepository;
import net.bncloud.saas.supplier.repository.SupplierRepository;
import net.bncloud.saas.supplier.repository.SupplierStaffRepository;
import net.bncloud.saas.supplier.service.dto.SupplierManagerDTO;
import net.bncloud.saas.tenant.domain.Organization;
import net.bncloud.saas.tenant.repository.OrganizationRepository;
import net.bncloud.saas.supplier.service.query.SupplierManagerQuery;
import net.bncloud.saas.user.domain.QUserInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class SupplierManagerService {

    private final OrganizationRepository organizationRepository;
    private final SupplierRepository supplierRepository;
    private final SupplierStaffRepository supplierStaffRepository;
    private final SupplierManagerRepository supplierManagerRepository;
    private final UserInfoRepository userInfoRepository;
    private final RoleGroupRepository roleGroupRepository;
    private final JPAQueryFactory queryFactory;

    public Object supplierManagerTableByOrgId(QueryParam<SupplierManagerQuery> queryParam, Pageable pageable) {
        Optional<Org> currentOrg = SecurityUtils.getCurrentOrg();
        if (currentOrg.isPresent()) {
            Org org = currentOrg.get();
            QUserInfo qUserInfo = QUserInfo.userInfo;
            QSupplierManager supplierManager = QSupplierManager.supplierManager;
            Optional<Organization> organizationOptional = organizationRepository.findById(org.getId());
            Organization organization = organizationOptional.get();
            Predicate predicate = supplierManager.supplier.organizations.contains(organization);

            String searchValue = queryParam.getSearchValue();
            SupplierManagerQuery param = queryParam.getParam();
            if (StrUtil.isNotBlank(searchValue)) {
                searchValue = "%" + searchValue + "%";

                com.querydsl.core.types.Predicate or = qUserInfo.code.like(searchValue );
                or = ExpressionUtils.or(or, qUserInfo.name.like( searchValue ));
                or = ExpressionUtils.or(or, qUserInfo.mobile.like( searchValue));
                predicate = ExpressionUtils.and(predicate,or);

            } else {
                predicate = StrUtil.isBlank(param.getUserCode()) ? predicate : ExpressionUtils.and(predicate, qUserInfo.code.eq(param.getUserCode()));
                predicate = StrUtil.isBlank(param.getUserName()) ? predicate : ExpressionUtils.and(predicate, qUserInfo.name.eq(param.getUserName()));
                predicate = StrUtil.isBlank(param.getMobile()) ? predicate : ExpressionUtils.and(predicate, qUserInfo.mobile.eq(param.getMobile()));
            }

            QueryResults<SupplierManagerDTO> queryResults = queryFactory.select(
                    Projections.bean(
                            SupplierManagerDTO.class,
                            qUserInfo.id,
                            qUserInfo.name,
                            qUserInfo.code,
                            qUserInfo.mobile,
                            supplierManager.enabled,
                            supplierManager.id.as("managerId"),
                            supplierManager.supplier.id.as("supplierId"),
                            supplierManager.supplier.name.as("supplierName")
                    )
            ).from(supplierManager)
                    .innerJoin(qUserInfo)
                    .on(supplierManager.supplierStaff.user.userId.eq(qUserInfo.id))
                    .where(predicate)
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetchResults();

            return PageUtils.of(queryResults, pageable);
        }
        return PageUtils.empty();
    }


    public Page supplierManagerTable(QueryParam<SupplierManagerQuery> query, Pageable pageable) {
        SupplierManagerQuery param = query.getParam();
        LoginInfo loginInfo = SecurityUtils.getLoginInfoOrThrow();
        Supplier currentSupplier = loginInfo.getCurrentSupplier();
        if (currentSupplier != null) {
            QUserInfo qUserInfo = QUserInfo.userInfo;
            QSupplierManager supplierManager = QSupplierManager.supplierManager;
            BooleanExpression condition = supplierManager.supplier.id.eq(currentSupplier.getSupplierId());
            QueryResults<SupplierManagerDTO> queryResults = queryFactory.select(
                    Projections.bean(
                            SupplierManagerDTO.class,
                            qUserInfo.id,
                            qUserInfo.code,
                            qUserInfo.name,
                            qUserInfo.mobile,
                            supplierManager.enabled,
                            supplierManager.id.as("managerId"),
                            supplierManager.id.as("supplierId"),
                            supplierManager.name.as("supplierName")
                    )
            ).from(qUserInfo, supplierManager)
                    .where(qUserInfo.id.eq(supplierManager.supplierStaff.user.userId)
                            .and(condition))
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetchResults();
            return PageUtils.of(queryResults, pageable);
        }
        return PageUtils.empty();
    }

    public SupplierManager save(SupplierManager supplierManager) {
        return supplierManagerRepository.save(supplierManager);
    }

    public void batchAddSupplierManager(BatchSupplierManagerVO vo) {
        Long supplierId = vo.getSupplierId().get(0);
        List<Long> supplierMemberIds = vo.getSupplierMemberIds();
        Optional<net.bncloud.saas.supplier.domain.Supplier> supplierOptional = supplierRepository.findById(supplierId);
        if (!supplierOptional.isPresent()) {
            throw new ApiException(400, "供应商信息不存在");
        }
        List<SupplierStaff> supplierStaffs = supplierStaffRepository.findAllById(supplierMemberIds);
        List<SupplierManager> managerList = supplierManagerRepository.findAllBySupplierStaffIn(supplierStaffs);
        if (CollectionUtil.isNotEmpty(managerList)) {
            List<Long> managerIdsExist = managerList.stream().flatMap(r -> Stream.of(r.getSupplierStaff())).map(SupplierStaff::getId).collect(Collectors.toList());
            supplierStaffs = supplierStaffs.stream().filter(supplierStaff -> !managerIdsExist.contains(supplierStaff.getId())).collect(Collectors.toList());
        }
        if (CollectionUtil.isNotEmpty(supplierStaffs)) {
            List<SupplierManager> managers = supplierStaffs.stream().map(staff -> SupplierManager.builder()
                    .managerType(ManagerType.SUB)
                    .supplierStaff(staff)
                    .supplier(supplierOptional.get())
                    .mobile(staff.getMobile())
                    .name(staff.getName())
                    .enabled(Boolean.TRUE)
                    .build()).collect(Collectors.toList());
            supplierManagerRepository.saveAll(managers);

            List<Long> userIds = supplierStaffs.stream().map(supplierStaff -> supplierStaff.getUser().getUserId()).collect(Collectors.toList());
            List<UserInfo> userInfos = userInfoRepository.findAllByIdIn(userIds);
            userInfos.forEach(userInfo -> {
                userInfo.getStatus().setDisabled(Boolean.FALSE);
            });
            userInfoRepository.saveAll(userInfos);
        }
        addManagerRole(supplierStaffs);
    }

    private void addManagerRole(List<SupplierStaff> supplierStaffs) {
        //TODO 添加供应商管理员角色
        RoleGroup roleGroup = roleGroupRepository.findOneByName("供应商管理员组");
        List<Role> roles = Lists.newArrayList();
        if (roleGroup != null) {
            roles.addAll(Lists.newArrayList(roleGroup.getRoles()));
        }
        supplierStaffs.forEach(supplierStaff -> {
            List<Role> staffRoles = supplierStaff.getRoles();
            if (CollectionUtil.isEmpty(staffRoles)) {
                supplierStaff.setRoles(roles);
            } else {
                staffRoles.addAll(roles);
            }
        });
        supplierStaffRepository.saveAll(supplierStaffs);
    }

    public Object checkTransfer(Long supplierManagerId) {

        return null;
    }
}
