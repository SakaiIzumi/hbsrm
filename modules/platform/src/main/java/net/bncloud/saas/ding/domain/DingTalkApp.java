package net.bncloud.saas.ding.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import net.bncloud.common.domain.AbstractAuditingEntity;
import net.bncloud.saas.ding.domain.vo.AppType;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.time.Instant;

/**
 * 企业内应用
 */
@Entity
@Table(name = "ss_ding_app")
@Getter
@Setter
public class DingTalkApp extends AbstractAuditingEntity {
    private static final long serialVersionUID = -13705489438619332L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnoreProperties("apps")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "corp_id")
    private DingTalkCorp corp;

    @Enumerated(EnumType.STRING)
    private AppType appType;

    private Long agentId;
    private String appKey;
    private String appSecret;

    private String name;
    private String description;
    private String logo;

    private String h5Url;
    private String pcUrl;
    private String adminUrl;

    private String accessToken;
    private Instant createTokenAt;
    /**
     * 是否有效
     */
    @Transient
    public boolean isValid() {
        return StringUtils.isNoneBlank(accessToken) && (createTokenAt != null && createTokenAt.isAfter(Instant.now().minusSeconds(7000)));
    }

    /**
     * 是否需要刷新token
     *
     * 超过1小时就刷新
     */
    @Transient
    public boolean needRefresh() {
        return !isValid() || createTokenAt.isBefore(Instant.now().minusSeconds(3600));
    }


}
