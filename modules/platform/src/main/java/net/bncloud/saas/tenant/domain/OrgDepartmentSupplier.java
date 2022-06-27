package net.bncloud.saas.tenant.domain;

import lombok.Getter;
import lombok.Setter;
import net.bncloud.common.domain.AbstractAuditingEntity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "ss_tenant_org_department_supplier")
@Getter
@Setter
public class OrgDepartmentSupplier extends AbstractAuditingEntity {

    private static final long serialVersionUID = -3992475607807303531L;
    @Id
    private Long deptId;

    @MapsId("deptId")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dept_id", referencedColumnName = "id", nullable = false)
    private OrgDepartment department;

    private String supplierCode;
    private String supplierName;
    private Long managerId;
    private String managerMobile;
    private String managerName;

}
