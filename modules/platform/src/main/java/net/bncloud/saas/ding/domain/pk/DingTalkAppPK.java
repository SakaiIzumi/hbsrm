package net.bncloud.saas.ding.domain.pk;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DingTalkAppPK implements Serializable {

    private static final long serialVersionUID = 981932277630922781L;
    @Column(name = "corp_id")
    private String corpId;
    @Column(name = "agent_id")
    private Long agentId;

    public static DingTalkAppPK of(String corpId, Long agentId) {
        return new DingTalkAppPK(corpId, agentId);
    }
}
