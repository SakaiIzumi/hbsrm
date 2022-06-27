package net.bncloud.saas.tenant.event;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class CompanyDTO implements Serializable {
    private static final long serialVersionUID = 7417244406444639765L;
    private final Long companyId;
    private final String companyName;
    private final Long managerId;
    private final String managerName;

    public static CompanyDTO of(Long companyId, String companyName, Long managerId, String managerName) {
        return new CompanyDTO(companyId, companyName, managerId, managerName);
    }
}
