package net.bncloud.saas.ding.domain;

import lombok.Getter;
import lombok.Setter;
import net.bncloud.common.domain.AbstractAuditingEntity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "ss_ding_role_group")
@Getter
@Setter
public class DingRoleGroup extends AbstractAuditingEntity {
    private static final long serialVersionUID = 4288193474525319131L;
    @Id
    private Long id;
    private String name;

    @OneToMany(mappedBy = "group",cascade={CascadeType.PERSIST,CascadeType.REMOVE})
    private List<DingRole> roles;

    @ManyToOne
    @JoinColumn(name = "corp_id", referencedColumnName = "corp_id", nullable = false, updatable = false)
    private DingTalkCorp corp;
}
