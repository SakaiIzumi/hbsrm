package net.bncloud.saas.tenant.service;

import net.bncloud.common.constants.LoginTarget;
import net.bncloud.common.exception.ApiException;
import net.bncloud.common.security.LoginInfo;
import net.bncloud.common.security.SecurityUtils;
import net.bncloud.common.security.Supplier;
import net.bncloud.saas.event.CreatedOrgDept;
import net.bncloud.saas.event.OrgSupplierDeptCreatedEvent;
import net.bncloud.saas.tenant.domain.Company;
import net.bncloud.saas.tenant.domain.OrgDepartment;
import net.bncloud.saas.tenant.domain.OrgEmployee;
import net.bncloud.saas.tenant.domain.Organization;
import net.bncloud.saas.tenant.domain.vo.UserId;
import net.bncloud.saas.tenant.repository.CompanyRepository;
import net.bncloud.saas.tenant.repository.OrgDepartmentRepository;
import net.bncloud.saas.tenant.repository.OrgEmployeeRepository;
import net.bncloud.saas.tenant.repository.OrganizationRepository;
import net.bncloud.saas.tenant.service.command.BulkUpdateParentCommand;
import net.bncloud.saas.tenant.service.command.CreateDeptCommand;
import net.bncloud.saas.tenant.service.command.CreateSupplierDeptCommand;
import net.bncloud.saas.tenant.service.dto.CreateSupplierDeptResultDTO;
import net.bncloud.saas.tenant.service.dto.OrgDepartmentDTO;
import net.bncloud.saas.tenant.service.mapstruct.OrgDepartmentMapper;
import net.bncloud.saas.user.domain.UserInfo;
import net.bncloud.saas.user.service.UserInfoService;
import net.bncloud.saas.user.service.command.UserCreateCommand;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static net.bncloud.saas.result.SaaSResultCode.DEPARTMENT_DELETE_FORBIDDEN;

@Service
@Transactional(rollbackFor = Exception.class)
public class DepartmentService implements ApplicationEventPublisherAware {

    private final OrgDepartmentRepository orgDepartmentRepository;
    private final UserInfoService userInfoService;
    private final OrgEmployeeRepository orgEmployeeRepository;
    private final OrganizationRepository organizationRepository;
    private final CompanyRepository companyRepository;
    private final OrgDepartmentMapper orgDepartmentMapper;

    public DepartmentService(OrgDepartmentRepository orgDepartmentRepository,
                             UserInfoService userInfoService,
                             OrgEmployeeRepository orgEmployeeRepository,
                             OrganizationRepository organizationRepository,
                             CompanyRepository companyRepository,
                             OrgDepartmentMapper orgDepartmentMapper) {
        this.orgDepartmentRepository = orgDepartmentRepository;
        this.userInfoService = userInfoService;
        this.orgEmployeeRepository = orgEmployeeRepository;
        this.organizationRepository = organizationRepository;
        this.companyRepository = companyRepository;
        this.orgDepartmentMapper = orgDepartmentMapper;
    }

    public OrgDepartment createDept(CreateDeptCommand command) {
        if (command.getParentId() != null) {
            if (!orgDepartmentRepository.findById(command.getParentId()).isPresent()) {
                throw new IllegalArgumentException("所选上级部门不存在");
            }
        }
        if (orgDepartmentRepository.findOneByParentIdAndName(command.getParentId(), command.getName()).isPresent()) {
            throw new IllegalArgumentException("已存在部门 [" + command.getName() + "]");
        }
        final OrgDepartment department = command.create();
        SecurityUtils.getCurrentCompany().ifPresent(company -> {
            department.setCompanyId(company.getId());
        });
        SecurityUtils.getCurrentOrg().ifPresent(org -> {
            department.setOrgId(org.getId());
        });
        SecurityUtils.getCurrentSupplier().ifPresent(supplier -> {
            department.setSupplierId(supplier.getSupplierId());
            department.setSupplierCode(supplier.getSupplierCode());
            department.setSupplierName(supplier.getSupplierName());
        });
        return orgDepartmentRepository.save(department);
    }

//    public CreateSupplierDeptResultDTO createDeptBySupplier(CreateSupplierDeptCommand command) {
//        Optional<OrgDepartment> deptOptional = orgDepartmentRepository.findOneByOrgIdAndSupplierCode(command.getOrgId(), command.getCode());
//        if (deptOptional.isPresent()) {
//            OrgDepartment dept = deptOptional.get();
//            return buildResult(dept);
//        }
//        Optional<Organization> org = organizationRepository.findById(command.getOrgId());
//        Organization organization = org.get();
//        OrgDepartment rootDept = organization.getRootDept();
//        OrgDepartment dept = new OrgDepartment();
////        dept.setParent(rootDept);
//        dept.setParentId(rootDept.getId());
//        dept.setOrgId(command.getOrgId());
//        dept.setName(command.getName());
//        dept.setDescription(command.getName());
//        dept.setSupplierId(command.getSupplierId());
//        dept.setSupplierCode(command.getCode());
//        dept.setSupplierName(command.getName());
//
//        OrgDepartment savedDept = orgDepartmentRepository.save(dept);
//
//       applicationEventPublisher.publishEvent(new OrgSupplierDeptCreatedEvent(this, new CreatedOrgDept(organization.getId(), organization.getName(), savedDept.getId(), savedDept.getName())));
//        CreateSupplierDeptResultDTO createSupplierDeptResultDTO = buildResult(savedDept);
//
//        command.getManagers().forEach(supplierManager -> {
//            UserInfo user = userInfoService.createActiveUser(UserCreateCommand.of(supplierManager.getName(), supplierManager.getMobile(), "86", "Aa12345678")); // fixme 密码初始化逻辑
//            OrgEmployee employee = new OrgEmployee();
//            employee.setUser(UserId.of(user.getId()));
//            employee.setName(user.getName());
//            employee.setDepartment(savedDept);
//            employee.setMobile(supplierManager.getMobile());
//            employee.setOrgId(savedDept.getOrgId());
//            orgEmployeeRepository.save(employee);
//        });
//
//        return createSupplierDeptResultDTO;
//    }

    private CreateSupplierDeptResultDTO buildResult(OrgDepartment dept) {
        CreateSupplierDeptResultDTO result = new CreateSupplierDeptResultDTO();
        result.setOrgId(dept.getOrgId());
        result.setDeptId(dept.getId());
        result.setDeptName(dept.getName());

        return result;
    }

    public void update(OrgDepartment resources) {
        SecurityUtils.getCurrentCompany().ifPresent(company -> {
            resources.setCompanyId(company.getId());
        });
        SecurityUtils.getCurrentOrg().ifPresent(org -> {
            resources.setOrgId(org.getId());
        });
        orgDepartmentRepository.save(resources);
    }

    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    /**
     * 根据采购方编码查询部门列表
     *
     * @param companyCode 采购方编码
     * @return 部门列表
     */
    public List<OrgDepartmentDTO> getListByCompanyCode(String companyCode) {
        Optional<Company> companyOptional = companyRepository.findCompanyByCode(companyCode);
        List<OrgDepartment> orgDepartments = companyOptional.map(company -> {

            List<Long> ids = company.getOrganizations().stream().map(Organization::getId).collect(Collectors.toList());
            return orgDepartmentRepository.findAllByOrgIdIn(ids);
        }).orElse(Collections.emptyList());
        return orgDepartmentMapper.toDto(orgDepartments);

    }

    public List<OrgDepartmentDTO> getListByParentId(Long parentId) {

        if (parentId == null) {
            final LoginInfo loginInfo = SecurityUtils.getLoginInfoOrThrow();
//            if (LoginTarget.ZY == SecurityUtils.getLoginTarget() && loginInfo.isSupplier()) {
//                final Supplier currentSupplier = loginInfo.getCurrentSupplier();
//                final List<OrgDepartment> departments = orgDepartmentRepository.findAllBySupplierId(currentSupplier.getSupplierId());
//                return orgDepartmentMapper.toDto(departments);
//            }
            List<OrgDepartment> departmentList = SecurityUtils.getCurrentCompany().map(company ->
                    orgDepartmentRepository.findAllByCompanyIdAndParentIsNullAndDeleted(company.getId(), false))
                    .orElse(Collections.emptyList());
            return orgDepartmentMapper.toDto(departmentList);
        } else {
            OrgDepartment parent = new OrgDepartment();
            parent.setId(parentId);
            List<OrgDepartment> departmentList = SecurityUtils.getCurrentCompany().map(company ->
                    orgDepartmentRepository.findAllByParentAndDeleted(parent, false))
                    .orElse(Collections.emptyList());
            return orgDepartmentMapper.toDto(departmentList);
        }
    }

    @Transactional
    public void deleteById(Long id) {
        /*final LoginInfo loginInfo = SecurityUtils.getLoginInfoOrThrow();
        organizationRepository.findById(loginInfo.getCurrentOrg().getId());*/
        // TODO 权限校验
        final List<OrgDepartment> children = orgDepartmentRepository.findAllByParentId(id);
        if (children != null && children.size() > 0) {
            throw new ApiException(DEPARTMENT_DELETE_FORBIDDEN.getCode(),DEPARTMENT_DELETE_FORBIDDEN.getMessage());
        }
        orgDepartmentRepository.findById(id).ifPresent(orgDepartmentRepository::delete);
    }

    @Transactional
    public void bulkDelete(List<Long> ids) {
        if (ids == null) {
            return;
        }
        ids.forEach(this::deleteById);
    }

    @Transactional
    public void bulkUpdateParent(BulkUpdateParentCommand command) {
        if (command.getDeptIds() == null || command.getDeptIds().isEmpty()) {
            return;
        }
        if (command.getParentId() == null) {
            throw new IllegalArgumentException("批量修改父级部门时所选父级部门不能为空");
        }
        if (!orgDepartmentRepository.findById(command.getParentId()).isPresent()) {
            throw new IllegalArgumentException("所选父级部门不存在");
        }
        command.getDeptIds().forEach(id -> {
            orgDepartmentRepository.findById(id).ifPresent(orgDepartment -> {
                orgDepartment.setParentId(command.getParentId());
                orgDepartmentRepository.save(orgDepartment);
            });
        });

    }
}
