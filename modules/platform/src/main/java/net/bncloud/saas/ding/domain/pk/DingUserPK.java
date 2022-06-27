package net.bncloud.saas.ding.domain.pk;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DingUserPK implements Serializable {

    private static final long serialVersionUID = -7144555827113633196L;

    @Column(name = "corp_id")
    private String corpId;
    @Column(name = "user_id")
    private String userId;

    public static DingUserPK of(String corpId, String userId) {
        return new DingUserPK(corpId, userId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DingUserPK that = (DingUserPK) o;
        return userId.equals(that.userId) && corpId.equals(that.corpId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, corpId);
    }
}
