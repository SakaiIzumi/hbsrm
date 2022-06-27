package net.bncloud.saas.tenant.domain.vo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@Setter
public class SupplierVO {
    private String code;
    private String name;
    private Long managerId;
    private String managerName;
}
