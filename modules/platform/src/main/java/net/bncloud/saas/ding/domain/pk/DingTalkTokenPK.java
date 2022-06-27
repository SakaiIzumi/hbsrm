package net.bncloud.saas.ding.domain.pk;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DingTalkTokenPK implements Serializable {
    private static final long serialVersionUID = 7632461427305788241L;
    private String corpId;
    private Long agentId;

    public static DingTalkTokenPK of(String corpId, Long agentId) {
        return new DingTalkTokenPK(corpId, agentId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DingTalkTokenPK that = (DingTalkTokenPK) o;
        return Objects.equals(corpId, that.corpId) && Objects.equals(agentId, that.agentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(corpId, agentId);
    }
}
