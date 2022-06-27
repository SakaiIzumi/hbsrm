package net.bncloud.saas.tenant.domain;

import lombok.Getter;
import lombok.Setter;
import net.bncloud.common.domain.AbstractAuditingEntity;
import net.bncloud.saas.tenant.domain.vo.ManagerVO;
import net.bncloud.saas.tenant.domain.vo.Status;
import net.bncloud.saas.tenant.domain.vo.UserVO;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * 企业，即租户
 */
@Entity
@Table(name = "ss_tenant_company")
@Getter
@Setter
public class Company extends AbstractAuditingEntity {

    private static final long serialVersionUID = -5527221134104999876L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;
    private String name;
    /**
     * 税号
     */
    private String taxId;
    /**
     * 统一社会信用代码
     */
    private String creditCode;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "register_by_id")),
            @AttributeOverride(name = "name", column = @Column(name = "register_by_name"))
    })
    private UserVO registerBy;

    /**
     * 企业管理员
     */
    @ElementCollection(targetClass = ManagerVO.class)
    @AttributeOverrides({
            @AttributeOverride(name = "userId", column = @Column(name = "manager_user_id")),
            @AttributeOverride(name = "name", column = @Column(name = "manager_user_name"))
    })
    @CollectionTable(
            name = "ss_tenant_company_manager",
            joinColumns = {@JoinColumn(name = "company_id", referencedColumnName = "id")}
    )
    private List<ManagerVO> managers;

    /**
     * 所有组织
     */
    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    private List<Organization> organizations;

    public void addManager(ManagerVO manager) {
        if (this.managers == null) {
            this.managers = new ArrayList<>();
        }
        this.managers.add(manager);
    }
}
