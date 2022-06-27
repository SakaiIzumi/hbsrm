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
public class DingDeptPK implements Serializable {
    private static final long serialVersionUID = 3663089555359450153L;
    @Column(name = "corp_id")
    private String corpId;
    @Column(name = "dept_id")
    private Long deptId;

    public static DingDeptPK of(String corpId, Long deptId) {
        return new DingDeptPK(corpId, deptId);
    }
}
