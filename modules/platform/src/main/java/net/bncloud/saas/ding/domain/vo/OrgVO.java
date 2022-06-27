package net.bncloud.saas.ding.domain.vo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@Setter
public class OrgVO {
    private Long companyId;
    private String companyName;
    private Long orgId;
    private String orgName;

}
