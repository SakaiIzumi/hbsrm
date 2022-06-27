package net.bncloud.saas.purchaser.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import net.bncloud.common.domain.AbstractAuditingEntity;
import net.bncloud.saas.supplier.domain.Supplier;
import net.bncloud.saas.tenant.domain.Organization;

import javax.persistence.*;
import java.util.List;

/**
 * 采购方
 */
@Entity
@Table(name = "ss_tenant_purchaser")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Purchaser extends AbstractAuditingEntity {
    private static final long serialVersionUID = -3390712012431397474L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", columnDefinition = "varchar(60) comment '采购方编码'")
    private String code;

    @Column(name = "name", columnDefinition = "varchar(60) comment '采购方名'")
    private String name;

    @Column(name = "artificial_person", columnDefinition = "varchar(100) comment '所属法人'")
    private String artificialPerson;

    @Column(name = "description", columnDefinition = "varchar(1024) comment '描述'")
    private String description;

    @Column(name = "enabled", columnDefinition = "bit(1) comment '状态'")
    private Boolean enabled;

    @Column(name = "source_id", columnDefinition = "varchar(60) comment '采购方来源id'")
    private String sourceId;

    @Column(name = "source_code", columnDefinition = "varchar(60) comment '采购方来源编码'")
    private String sourceCode;

    @Column(name = "company_id", columnDefinition = "varchar(60) comment '公司id'")
    private Long companyId;

    @Column(name = "company_code", columnDefinition = "varchar(60) comment '公司编码'")
    private String companyCode;

    @Column(name = "company_name", columnDefinition = "varchar(60) comment '公司名字'")
    private String companyName;

    @ManyToOne
    @JoinColumn(name = "org_id")
    private Organization organization;


    @JsonIgnore
    @OneToMany(mappedBy = "purchaser")
    private List<PurchaserStaff> purchaserStaffs;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "ss_tenant_purchaser_supplier_ref",
            joinColumns = {@JoinColumn(name = "pur_id")},
            inverseJoinColumns = {@JoinColumn(name = "sup_id")},
            uniqueConstraints = {@UniqueConstraint(name = "pur_sup_uni", columnNames = {"pur_id", "sup_id"})}
    )
    private List<Supplier> suppliers;


    public static Purchaser of(Long id) {
        Purchaser purchaser = new Purchaser();
        purchaser.setId(id);
        return purchaser;
    }
}
