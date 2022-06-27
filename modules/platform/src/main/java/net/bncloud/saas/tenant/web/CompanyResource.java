package net.bncloud.saas.tenant.web;

import net.bncloud.common.api.R;
import net.bncloud.saas.tenant.domain.Company;
import net.bncloud.saas.tenant.service.CompanyService;
import net.bncloud.saas.tenant.service.dto.TenantCompanyDTO;
import net.bncloud.saas.tenant.service.query.CompanyQuery;
import net.bncloud.saas.tenant.web.payload.RegisterCompanyPayload;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tenant/company")
public class CompanyResource {

    private final CompanyService companyService;

    public CompanyResource(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("/register")
    public R<Company> registerCompany(@RequestBody RegisterCompanyPayload payload) {
        Company company = companyService.createCompany(payload.toCommand());
        return R.data(company);
    }

    @PutMapping("/active/{id}")
    public R<Void> active(@PathVariable Long id) {
        companyService.active(id);
        return R.success();
    }

    @PutMapping("/disable/{id}")
    public R<Void> disable(@PathVariable Long id) {
        companyService.disable(id);
        return R.success();
    }

    @PostMapping("/pageQuery")
    public R<Page<TenantCompanyDTO>> pageQuery(Pageable pageable, @RequestBody CompanyQuery query) {
        return R.data(companyService.pageQuery(query, pageable));
    }
}
