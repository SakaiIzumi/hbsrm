package net.bncloud.saas.ding.domain;

import lombok.Getter;
import lombok.Setter;
import net.bncloud.common.domain.AbstractAuditingEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

/**
 * 钉钉组织
 */
@Entity
@Table(name = "ss_ding_corp")
@Getter
@Setter
public class DingTalkCorp extends AbstractAuditingEntity {

    private static final long serialVersionUID = 3909882724461007528L;
    @Id
    @Column(name = "corp_id")
    private String corpId;
    private Long orgId;
    private String orgName;

    @OneToMany(mappedBy = "corp", fetch = FetchType.LAZY)
    private List<DingTalkApp> apps;

    @OneToMany(mappedBy = "corp", fetch = FetchType.LAZY)
    private List<DingUser> users;
}
