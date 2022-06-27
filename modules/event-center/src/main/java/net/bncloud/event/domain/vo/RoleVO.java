package net.bncloud.event.domain.vo;

import lombok.Getter;
import lombok.Setter;
import net.bncloud.common.domain.AbstractAuditingEntity;

import javax.persistence.Embeddable;
import java.time.Instant;

@Embeddable
@Getter
@Setter
public class RoleVO  extends AbstractAuditingEntity {
    private Long roleId;
    private String roleName;
    private Instant relateAt;
    private boolean disabled;

}
