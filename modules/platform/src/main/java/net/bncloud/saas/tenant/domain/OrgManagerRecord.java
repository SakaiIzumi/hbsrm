package net.bncloud.saas.tenant.domain;

import lombok.*;
import net.bncloud.common.domain.AbstractAuditingEntity;
import net.bncloud.saas.tenant.domain.vo.UserVO;

import javax.persistence.*;
import java.util.List;

/**
 * 只作记录用,组织管理员表,显示平台创建的组织管理员信息,解决协作组织管理员初期不存在组织的问题
 */
@Entity
@Table(name = "ss_tenant_org_manager_record")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrgManagerRecord extends AbstractAuditingEntity {

    private static final long serialVersionUID = -657799685508695491L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "user_id"))
    })
    private UserVO user;

    private String managerType;

    private String mobile;

    private Boolean enabled;

    @OneToMany(mappedBy = "orgManagerRecord")
    private List<OrganizationRecord> organizationRecords;
}
