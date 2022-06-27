package net.bncloud.saas.tenant.domain;

import lombok.*;
import net.bncloud.common.constants.ManagerType;
import net.bncloud.common.domain.AbstractAuditingEntity;
import net.bncloud.saas.tenant.domain.vo.DeptVO;
import net.bncloud.saas.tenant.domain.vo.UserVO;
import net.bncloud.saas.user.domain.UserInfo;
import net.bncloud.saas.user.domain.vo.SupplierVO;
import net.bncloud.saas.user.domain.vo.UserInfoVO;

import javax.persistence.*;

@Entity
@Table(name = "ss_tenant_org_manager")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrgManager extends AbstractAuditingEntity {

    private static final long serialVersionUID = -657799685508695491L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "user_id"))
    })
    private UserVO user;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "dept_id")),
            @AttributeOverride(name = "name", column = @Column(name = "dept_name"))
    })
    private DeptVO department;

    @Enumerated(EnumType.STRING)
    private ManagerType managerType;

//    @Column(name = "org_id")
//    private Long orgId;

    /**
     * 是否启用
     */
    private boolean enabled;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, optional = false)
    @JoinColumn(name = "org_id")
    private Organization org;

    @Transient
    public boolean isMain() {
        return managerType == ManagerType.MAIN;
    }

//    @OneToOne
//    @JoinColumn(name = "employee_id", referencedColumnName = "id")
//    private OrgEmployee employee;

    private Long employeeId;


}
