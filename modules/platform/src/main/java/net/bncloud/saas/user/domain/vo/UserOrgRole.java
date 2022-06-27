package net.bncloud.saas.user.domain.vo;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserOrgRole {

    private Long companyId;
    private String companyName;
    private Long orgId;
    private String orgName;
}
