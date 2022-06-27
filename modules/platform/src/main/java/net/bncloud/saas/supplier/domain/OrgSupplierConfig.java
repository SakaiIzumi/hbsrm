package net.bncloud.saas.supplier.domain;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 组织的供应商相关配置
 */
/*@Entity
@Table(name = "t_s_org_config")
@Getter
@Setter*/
public class OrgSupplierConfig {

    @Id
    private Long orgId;
    private String orgName;

    /**
     * 供应商可用角色
     */
    @ElementCollection(targetClass = ConfigedRole.class)
    @CollectionTable(name = "t_s_org_config_role",
            joinColumns = {@JoinColumn(name = "config_id", referencedColumnName = "orgId")})

    private Set<ConfigedRole> configedRoles = new LinkedHashSet<>();
}
