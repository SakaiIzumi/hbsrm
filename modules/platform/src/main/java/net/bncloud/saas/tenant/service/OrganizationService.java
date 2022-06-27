package net.bncloud.saas.tenant.service;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.constants.ManagerType;
import net.bncloud.common.exception.ApiException;
import net.bncloud.common.security.LoginInfo;
import net.bncloud.common.security.SecurityUtils;
import net.bncloud.common.security.Supplier;
import net.bncloud.common.util.ApplicationContextProvider;
import net.bncloud.enums.SceneType;
import net.bncloud.saas.event.CreateOrganization;
import net.bncloud.saas.event.OrganizationCreateEvent;
import net.bncloud.saas.purchaser.domain.Purchaser;
import net.bncloud.saas.purchaser.service.PurchaserQueryService;
import net.bncloud.saas.purchaser.service.PurchaserService;
import net.bncloud.saas.tenant.domain.*;
import net.bncloud.saas.tenant.domain.vo.*;
import net.bncloud.saas.tenant.repository.*;
import net.bncloud.saas.tenant.service.command.CreateOrgCommand;
import net.bncloud.saas.tenant.service.command.CreateOrganizationCommand;
import net.bncloud.saas.tenant.service.dto.OrganizationDTO;
import net.bncloud.saas.tenant.service.mapstruct.OrganizationMapper;
import net.bncloud.saas.tenant.service.query.OrganizationQuery;
import net.bncloud.saas.user.domain.UserCurrent;
import net.bncloud.saas.user.domain.UserInfo;
import net.bncloud.saas.user.domain.vo.UserOrgVO;
import net.bncloud.saas.user.service.UserInfoService;
import net.bncloud.saas.utils.pageable.PageUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class OrganizationService {


    private final CompanyService companyService;
    private final CompanyRepository companyRepository;

    private final OrganizationRepository organizationRepository;
    private final OrgManagerRepository orgManagerRepository;
    private final OrgDepartmentRepository orgDepartmentRepository;
    private final OrgEmployeeRepository orgEmployeeRepository;
    private final OrgManagerRecordRepository orgManagerRecordRepository;

    private final OrganizationMapper organizationMapper;
    private final UserInfoService userInfoService;
    private final PurchaserService purchaserService;
    private final QOrganization qOrganization = QOrganization.organization;
    private final JPAQueryFactory queryFactory;

    private final PurchaserQueryService purchaserQueryService;

    public List<Organization> allQuery(QueryParam<OrganizationQuery> queryParam) {
        Predicate predicate = ExpressionUtils.anyOf();
        OrganizationQuery query = queryParam.getParam();
        return queryFactory.select(qOrganization).from(qOrganization).fetch();
    }

    public Page pageQuery(QueryParam<OrganizationQuery> queryParam, Pageable pageable) {
        Predicate predicate = ExpressionUtils.anyOf();
        OrganizationQuery query = queryParam.getParam();
        String qs = query.getQs();
        if (StrUtil.isNotEmpty(qs)) {
            predicate = ExpressionUtils.and(predicate, qOrganization.name.like("%" + qs + "%"));
        } else {
            predicate = ObjectUtil.isEmpty(query.getId()) ? predicate : ExpressionUtils.and(predicate, qOrganization.id.eq(query.getId()));
            predicate = StrUtil.isBlank(query.getName()) ? predicate : ExpressionUtils.and(predicate, qOrganization.name.like(query.getName()));
        }
        String scene = query.getScene();
        if (StrUtil.isEmpty(scene)) {
            scene = SceneType.Default.getCode();
        }

        SceneType sceneType = SceneType.code(scene);
        switch (sceneType) {
            case Purchaser:
                String purCode = query.getPurCode();
                Purchaser purchaser = purchaserService.findByCode(purCode);
                Organization organization = purchaser.getOrganization();
                List<Organization> organizations = Lists.newArrayList(organization);
                List<OrganizationDTO> organizationDTOS = organizationMapper.toDto(organizations);
                return PageUtils.of(organizationDTOS, pageable, (long) organizations.size());
//            case Org:
//                Optional<Org> currentOrg = SecurityUtils.getCurrentOrg();
//                if (currentOrg.isPresent()) {
//                    Org org = currentOrg.get();
//
//                }
//                break;
            case Supplier:
                Optional<Supplier> currentSupplier = SecurityUtils.getCurrentSupplier();
                if (currentSupplier.isPresent()) {
                    String supplierCode = currentSupplier.get().getSupplierCode();
                    net.bncloud.saas.supplier.domain.Supplier supplier = net.bncloud.saas.supplier.domain.Supplier.builder().code(supplierCode).build();
                    ExpressionUtils.and(predicate, qOrganization.suppliers.contains(supplier));
                    QueryResults<Organization> organizationQueryResults = queryFactory.select(qOrganization).from(qOrganization)
                            .where(predicate)
                            .offset(pageable.getOffset())
                            .limit(pageable.getPageSize())
                            .fetchResults();
                    List<Organization> results = organizationQueryResults.getResults();
                    List<OrganizationDTO> organizationDTOSResult = organizationMapper.toDto(results);
                    return PageUtils.of(organizationDTOSResult, pageable, organizationQueryResults.getTotal());
                }
                break;
            case All:
            case Default:
                QueryResults<Organization> organizationQueryResults = queryFactory.select(qOrganization).from(qOrganization)
                        .where(predicate)
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetchResults();
                List<OrganizationDTO> organizationDTOSResult = organizationMapper.toDto(organizationQueryResults.getResults());
                return PageUtils.of(organizationDTOSResult, pageable, organizationQueryResults.getTotal());
        }
        return PageUtils.empty();
    }

    public Organization createOrg(CreateOrganizationCommand command) {
        LoginInfo loginInfo = SecurityUtils.getLoginInfoOrThrow();
        UserInfo userInfo = userInfoService.getUserById(loginInfo.getId());
        UserCurrent currentInfo = userInfo.getCurrentInfo();
        if (currentInfo == null) {
            throw new IllegalArgumentException("请先注册或者加入公司");
        }
        UserOrgVO currentOrg = currentInfo.getCurrentOrg();
        if (currentOrg == null || currentOrg.getCompanyId() == null) {
            throw new IllegalArgumentException("请先注册或者加入公司");
        }
        Long companyId = currentOrg.getCompanyId();
        Optional<Company> companyOptional = companyService.findCompanyById(companyId);
        if (!companyOptional.isPresent()) {
            throw new IllegalArgumentException("请先注册或者加入公司");
        }
        Company company = companyOptional.get();
        List<ManagerVO> managers = company.getManagers();
        ManagerVO mVO = managers.stream().filter(managerVO -> managerVO.getUserId().equals(userInfo.getId()))
                .findAny().orElse(null);
        if (mVO == null) {
            throw new IllegalArgumentException("只有管理员才可以创建组织");
        }
        if (ManagerType.MAIN != mVO.getManagerType()) {
            throw new IllegalArgumentException("只有企业的主管理员才可以创建组织");
        }

        if (organizationRepository.findOneByCompanyAndName(company, command.getOrgName()).isPresent()) {
            throw new IllegalArgumentException("同一企业下组织名称不能重复");
        }

        Organization org = command.create();
        org.setCompany(company);
        // 根部门
        OrgDepartment dept = new OrgDepartment();
        dept.setName(org.getName());
        Organization savedOrg = organizationRepository.save(org);


        dept.setOrgId(savedOrg.getId());
        OrgDepartment savedDept = orgDepartmentRepository.save(dept);

        OrgManager orgManager = new OrgManager();
        orgManager.setOrg(org);
//        orgManager.setOrgId(org.getId());
        orgManager.setDepartment(DeptVO.of(savedDept.getId(), savedDept.getName()));
        orgManager.setUser(UserVO.of(loginInfo.getId(), loginInfo.getName()));
        orgManager.setManagerType(ManagerType.MAIN);
        orgManagerRepository.save(orgManager);


        OrgEmployee emp = new OrgEmployee();
        emp.setName(loginInfo.getName());
        emp.setUser(UserId.of(loginInfo.getId()));
        emp.setDepartment(savedDept);
        emp.setMobile(userInfo.getMobile());
        emp.setOrgId(savedDept.getOrgId());

        orgEmployeeRepository.save(emp);

        savedOrg = organizationRepository.save(savedOrg);

        userInfoService.changeCurrentOrg(savedOrg, loginInfo.getId());
        return savedOrg;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean addAsyncOrg(CreateOrgCommand command) {
        LoginInfo loginInfo = SecurityUtils.getLoginInfoOrThrow();
        UserInfo userInfo = userInfoService.getUserById(loginInfo.getId());
        UserCurrent currentInfo = userInfo.getCurrentInfo();
        if (currentInfo == null) {
            throw new IllegalArgumentException("请先注册或者加入公司");
        }
        Long userId = loginInfo.getId();
        Optional<OrgManagerRecord> orgManagerRecordOption = orgManagerRecordRepository.findByUserId(userId);
        if (!orgManagerRecordOption.isPresent()) {
            throw new IllegalArgumentException("请先审查是否拥有协助组织管理员信息");
        }
        //是否首次创建组织
        OrgManagerRecord orgManagerRecord = orgManagerRecordOption.get();
        List<OrganizationRecord> organizationRecords = orgManagerRecord.getOrganizationRecords();
        boolean firstTimeCreateOrg = false;
        if (organizationRecords == null || organizationRecords.isEmpty()) {
            firstTimeCreateOrg = true;
        }

        //TODO 私有化部署,只有一家公司 所有组织都挂靠在一家公司下面
        Optional<Company> companyOptional = companyRepository.findFirstByNameIsNotNull();
        if (!companyOptional.isPresent()) {
            throw new IllegalArgumentException("请先注册或者加入公司");
        }
        Company company = companyOptional.get();
        if (organizationRepository.findOneByCompanyAndName(company, command.getName()).isPresent()) {
            throw new IllegalArgumentException("同一企业下组织名称不能重复");
        }

        String orgName = command.getName();
        Optional<Organization> organizationOptional = organizationRepository.findByName(orgName);
        if (organizationOptional.isPresent()) {
            throw new IllegalArgumentException("组织名字重复");
        }

        Organization org = new Organization();
        org.setName(orgName);
        org.setCompany(company);
        org.setOrgType(OrgType.INTERNAL);
        org = organizationRepository.save(org);
        //按顺序触发事件
        //1.创建组织参数配置
        //2.创建组织管理员 赋予组织管理员角色
        //3.创建组织管理员记录
        ApplicationContextProvider.getApplicationContext().publishEvent(new OrganizationCreateEvent(CreateOrganization.of(userId, org.getId(), orgName, orgManagerRecord.getId(), firstTimeCreateOrg)));
        userInfoService.changeCurrentOrg(org, loginInfo.getId());
//        throw new ApiException(404, "测试流程");
        return firstTimeCreateOrg;
    }


    public Optional<Organization> findById(Long id) {
        return organizationRepository.findById(id);
    }

    public Organization getOne(Long id) {
        return organizationRepository.getOne(id);
    }


    @Transactional(rollbackFor = Exception.class)
    public void save(Organization organization) {
        organizationRepository.save(organization);
    }
}
