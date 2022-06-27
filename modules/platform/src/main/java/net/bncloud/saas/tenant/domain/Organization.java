
package net.bncloud.saas.tenant.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.bncloud.common.domain.AbstractAuditingEntity;
import net.bncloud.saas.purchaser.domain.Purchaser;
import net.bncloud.saas.supplier.domain.Supplier;
import net.bncloud.saas.tenant.domain.vo.OrgType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ss_tenant_organization")
@Getter
@Setter
@NoArgsConstructor
public class Organization extends AbstractAuditingEntity {

    private static final long serialVersionUID = -1782878644447307510L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    private String name;


    private String description;

    /**
     * 组织类型
     */
    @Enumerated(EnumType.STRING)
    private OrgType orgType;

    /**
     * 所属企业
     */
    @JsonIgnoreProperties("organizations")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false, updatable = false)
    private Company company;

    @OneToMany(fetch = FetchType.LAZY)
    private List<OrgManager> managers = new ArrayList<>();

    @OneToMany(mappedBy = "org", fetch = FetchType.LAZY)
    private List<OrgEmployee> employees;

    @ManyToMany(mappedBy = "organizations")
    private List<Supplier> suppliers;

    //    @ManyToMany(mappedBy = "organizations")
    @OneToMany(mappedBy = "organization")
    private List<Purchaser> purchasers;


    public Organization(Long id) {
        this.id = id;
    }

    public static Organization of(Long id) {
        return new Organization(id);
    }
}
