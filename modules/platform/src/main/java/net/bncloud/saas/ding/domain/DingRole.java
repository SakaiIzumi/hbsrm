package net.bncloud.saas.ding.domain;

import lombok.Getter;
import lombok.Setter;
import net.bncloud.common.domain.AbstractAuditingEntity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "ss_ding_role")
@Getter
@Setter
public class DingRole extends AbstractAuditingEntity {

    private static final long serialVersionUID = -4091280317763907641L;
    @Id
    private Long id;
    private String name;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private DingRoleGroup group;

    @ManyToOne
    @JoinColumn(name = "corp_id", referencedColumnName = "corp_id", nullable = false, updatable = false)
    private DingTalkCorp corp;
}
