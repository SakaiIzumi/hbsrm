package net.bncloud.saas.user.domain.vo;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class UserOrgVO {

    private Long companyId;
    private String companyName;
    private Long orgId;
    private String orgName;


    public static UserOrgVO of(Long companyId, String companyName, Long orgId, String orgName) {
        return new UserOrgVO(companyId, companyName, orgId, orgName);
    }

    public static UserOrgVO of(Long companyId, String companyName) {
        return new UserOrgVO(companyId, companyName, null, null);
    }
}
