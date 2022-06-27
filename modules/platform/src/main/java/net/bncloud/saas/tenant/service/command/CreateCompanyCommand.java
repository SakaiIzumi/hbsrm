package net.bncloud.saas.tenant.service.command;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.bncloud.saas.tenant.domain.Company;
import net.bncloud.saas.tenant.domain.vo.Status;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class CreateCompanyCommand {
    private String companyName;
    private String creditCode;

    public static CreateCompanyCommand of(String companyName, String creditCode) {
        return new CreateCompanyCommand(companyName, creditCode);
    }

    public Company create() {
        Company company = new Company();
        company.setName(companyName);
        company.setCreditCode(creditCode);
        company.setStatus(Status.AUDIT);
        return company;
    }
}
