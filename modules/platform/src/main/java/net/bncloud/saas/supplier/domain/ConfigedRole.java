package net.bncloud.saas.supplier.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@Setter
public class ConfigedRole {
    private Long groupId;
    private String groupName;
    private Long roleId;
    private String roleName;
}
