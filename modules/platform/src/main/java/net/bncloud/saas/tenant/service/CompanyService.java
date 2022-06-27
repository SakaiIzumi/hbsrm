package net.bncloud.saas.tenant.service;

import net.bncloud.common.constants.ManagerType;
import net.bncloud.common.exception.BizException;
import net.bncloud.common.security.LoginInfo;
import net.bncloud.common.security.SecurityUtils;
import net.bncloud.common.util.ApplicationContextProvider;
import net.bncloud.saas.result.SaaSResultCode;
import net.bncloud.saas.tenant.domain.Company;
import net.bncloud.saas.tenant.domain.vo.ManagerVO;
import net.bncloud.saas.tenant.domain.vo.Status;
import net.bncloud.saas.tenant.domain.vo.UserVO;
import net.bncloud.saas.tenant.event.CompanyDTO;
import net.bncloud.saas.tenant.event.CompanyRegisteredEvent;
import net.bncloud.saas.tenant.repository.CompanyRepository;
import net.bncloud.saas.tenant.service.command.CreateCompanyCommand;
import net.bncloud.saas.tenant.service.dto.TenantCompanyDTO;
import net.bncloud.saas.tenant.service.mapstruct.CompanyMapper;
import net.bncloud.saas.tenant.service.query.CompanyQuery;
import net.bncloud.saas.user.service.UserInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.Optional;

@Service
@Transactional
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final UserInfoService userInfoService; // TODO 注意依赖关系
    private final CompanyMapper companyMapper;

    public CompanyService(CompanyRepository companyRepository, UserInfoService userInfoService, CompanyMapper companyMapper) {
        this.companyRepository = companyRepository;
        this.userInfoService = userInfoService;
        this.companyMapper = companyMapper;
    }

    public Company createCompany(CreateCompanyCommand command) {
        // TODO 调第三方接口校验统一社会代码的合法性
        // 检查是否已注册
        Optional<Company> existOptional = companyRepository.findOneByCreditCode(command.getCreditCode());
        if (existOptional.isPresent()) {
            throw new BizException(SaaSResultCode.COMPANY_CREDIT_CODE_EXIST);
        }
        Company company = command.create();
        LoginInfo loginInfo = SecurityUtils.getLoginInfoOrThrow();
        Long id = loginInfo.getId();
        String name = loginInfo.getName();
        company.setRegisterBy(UserVO.of(id, name));
        company.addManager(ManagerVO.of(id, name, ManagerType.MAIN));
        Company saved = companyRepository.save(company);

        // 发布企业注册事件（内部事件）
        ApplicationContextProvider.getApplicationContext()
                .publishEvent(CompanyRegisteredEvent.of(
                        CompanyDTO.of(saved.getId(), saved.getName(),
                                saved.getRegisterBy().getId(),
                                saved.getRegisterBy().getName())));

        userInfoService.changeCurrentCompany(saved, loginInfo.getId());
        return saved;
    }

    public void active(Long id) {
        companyRepository.findById(id).ifPresent(company -> {
            company.setStatus(Status.ACTIVE);
            companyRepository.save(company);
        });
    }

    public void disable(Long id) {
        companyRepository.findById(id).ifPresent(company -> {
            company.setStatus(Status.DISABLED);
            companyRepository.save(company);
        });
    }

    public Optional<Company> findCompanyById(Long id) {


        return companyRepository.findById(id);
    }

    public Page<TenantCompanyDTO> pageQuery(CompanyQuery param, Pageable pageable) {
        return companyRepository.findAll((Specification<Company>) (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            if (param == null) {
                return predicate;
            }
            if (StringUtils.isNotBlank(param.getCode())) {
                predicate.getExpressions().add(criteriaBuilder.like(root.get("code"), "%" + param.getCode() + "%"));
            }
            if (StringUtils.isNotBlank(param.getName())) {
                predicate.getExpressions().add(criteriaBuilder.like(root.get("name"), "%" + param.getName() + "%"));
            }
            if (StringUtils.isBlank(param.getCode()) && StringUtils.isBlank(param.getName()) && StringUtils.isNotBlank(param.getQs())) {
                Predicate or = criteriaBuilder.or(criteriaBuilder.like(root.get("code"), "%" + param.getQs() + "%"),
                        criteriaBuilder.like(root.get("name"), "%" + param.getQs() + "%"));
                return or;
            }
            return predicate;
        }, pageable).map(company -> companyMapper.toDto(company));

    }
}
