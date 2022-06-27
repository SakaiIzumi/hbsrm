package net.bncloud.saas.tenant.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import net.bncloud.common.domain.AbstractAuditingEntity;
import net.bncloud.saas.authorize.domain.Role;
import net.bncloud.saas.tenant.domain.vo.UserId;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 员工
 */
@Entity
@Table(name = "ss_tenant_org_employee", uniqueConstraints = {
        @UniqueConstraint(name = "uni_mobile_org_id", columnNames = {"mobile", "org_id"})
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrgEmployee extends AbstractAuditingEntity {
    private static final long serialVersionUID = 6863202617575871071L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    private String name;

    @Embedded
    private UserId user;

    private String jobNo;

    //    @JsonIgnoreProperties("employees")
//    @ManyToOne
//    @JoinColumn(name = "dept_id", referencedColumnName = "id")
    @Transient
    private OrgDepartment department;

    private String mobile;

    /**
     * 职位名称
     */
    private String position;

    /**
     * 创建人名称
     */
    private String createdByName;

    /**
     * 是否启用
     */
    private boolean enabled;


    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "ss_tenant_org_employee_role",
            joinColumns = {@JoinColumn(name = "employee_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")},
            uniqueConstraints = {@UniqueConstraint(name = "employee_role_uni", columnNames = {"employee_id", "role_id"})}
    )
    private List<Role> roles;

    @Transient
    @Column(name = "org_id", insertable = false, updatable = false)
    private Long orgId;//必须有

    @ManyToOne
    @JoinColumn(name = "org_id")
    private Organization org;

//    @OneToOne(mappedBy = "employee")
//    private OrgManager orgManager;

    /*@OneToOne
    @JoinColumn(name="id",referencedColumnName = "employee_id")
    private OrgEmployeeMangeScope orgEmployeeMangeScope;*/
}