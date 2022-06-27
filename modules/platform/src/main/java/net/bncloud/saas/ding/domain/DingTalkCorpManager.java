package net.bncloud.saas.ding.domain;

import lombok.Getter;
import lombok.Setter;
import net.bncloud.common.constants.ManagerType;
import net.bncloud.common.domain.AbstractAuditingEntity;
import net.bncloud.saas.ding.domain.pk.DingTalkCorpManagerPK;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

/**
 * 钉钉组织
 */
@Entity
@Table(name = "ss_ding_corp_manager")
@Getter
@Setter
public class DingTalkCorpManager extends AbstractAuditingEntity {

    private static final long serialVersionUID = 3909882724461007528L;

    @EmbeddedId
    private DingTalkCorpManagerPK id;

    @MapsId("corpId")
    @ManyToOne
    @JoinColumn(name = "corp_id", referencedColumnName = "corp_id", nullable = false, updatable = false)
    private DingTalkCorp corp;

    private String name;

    @Enumerated(EnumType.STRING)
    private ManagerType managerType;
}
