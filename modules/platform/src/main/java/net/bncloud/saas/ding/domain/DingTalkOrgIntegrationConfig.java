package net.bncloud.saas.ding.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import net.bncloud.common.domain.AbstractAuditingEntity;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * 组织集成要用到的APP配置，
 */
@Entity
@Table(name = "ss_ding_integration_config")
@Getter
@Setter
public class DingTalkOrgIntegrationConfig extends AbstractAuditingEntity {
    private static final long serialVersionUID = -13705489438619332L;
    @Id
    private String corpId;

    @JsonIgnoreProperties({"apps", "users"})
    @MapsId("corpId")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "corp_id")
    private DingTalkCorp corp;

    private Long agentId;
    private String appKey;
    private String appSecret;

    private Long orgId;//对应的组织ID
    private Integer isOrg=1;//是否组织架构应用 1-是 0-否

    public void checkNotBlank() {
        if (StringUtils.isBlank(appKey)) {
            throw new IllegalStateException("appKey 不能为空");
        }
        if (StringUtils.isBlank(appSecret)) {
            throw new IllegalStateException("appSecret 不能为空");
        }
    }
}