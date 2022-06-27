package net.bncloud.saas.tenant.domain;

import lombok.*;
import net.bncloud.common.domain.AbstractAuditingEntity;
import net.bncloud.saas.tenant.domain.vo.UserId;

import javax.persistence.*;

/**
 * 员工关联询价单的权限
 */
@Entity
@Table(name = "ss_tenant_org_employee_manage_scope")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrgEmployeeMangeScope extends AbstractAuditingEntity {
    private static final long serialVersionUID = 6863202617575871742L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @Column(name="employee_id",insertable = false, updatable = false)
    private Long employeeId;

//    @Column(name="scope",insertable = false, updatable = false)
    private String scope;

}
