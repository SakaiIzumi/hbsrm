package net.bncloud.saas.tenant.domain;

import lombok.Getter;
import lombok.Setter;
import net.bncloud.common.domain.AbstractTreeEntity;
import net.bncloud.saas.tenant.domain.vo.ManagerVO;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ss_tenant_org_department")
@Getter
@Setter
public class OrgDepartment extends AbstractTreeEntity<OrgDepartment> {
    private static final long serialVersionUID = -3263053495158168392L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;

    /**
     * 所属组织ID
     */
    private Long orgId;

    private Long companyId;

    private Long supplierId;
    private String supplierCode;
    private String supplierName;

//    @JsonIgnoreProperties("department")
//    @OneToMany(mappedBy = "department", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @Transient
    private List<OrgEmployee> employees = new ArrayList<>();

    /**
     * 部门主管
     */
//    @JsonIgnoreProperties("managerType")
//    @ElementCollection(targetClass = ManagerVO.class)
//    @AttributeOverrides({
//            @AttributeOverride(name = "userId", column = @Column(name = "manager_user_id")),
//            @AttributeOverride(name = "name", column = @Column(name = "manager_user_name"))
//    })
//    @CollectionTable(
//            name = "ss_tenant_org_department_manager",
//            joinColumns = {@JoinColumn(name = "dept_id", referencedColumnName = "id")}
//    )
    @Transient
    private List<ManagerVO> managers = new ArrayList<>();
}