package net.bncloud.saas.tenant.domain.vo;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.bncloud.common.constants.ManagerType;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ManagerVO {

    private Long userId;
    private String name;
    @Enumerated(EnumType.STRING)
    private ManagerType managerType;

    public static ManagerVO of(Long userId, String name, ManagerType managerType) {
        return new ManagerVO(userId, name, managerType);
    }
}
