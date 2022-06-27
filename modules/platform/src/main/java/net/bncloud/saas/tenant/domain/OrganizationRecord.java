package net.bncloud.saas.tenant.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import net.bncloud.common.domain.AbstractAuditingEntity;

import javax.persistence.*;

/**
 * 只作记录用,记录协作组织管理员创建的组织
 */
@Entity
@Table(name = "ss_tenant_org_record")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrganizationRecord extends AbstractAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "org_id")
    private Long orgId;

    @Column(name = "org_name")
    private String orgName;

//    @JsonIgnoreProperties("organizationRecords")
    @ManyToOne
    @JoinColumn(name = "org_manager_record_id")
    private OrgManagerRecord orgManagerRecord;

}
