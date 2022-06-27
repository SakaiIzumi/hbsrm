package net.bncloud.saas.tenant.domain.vo;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class SysRoleVO {

    private Long id;
    private String name;

    public static SysRoleVO of(Long id, String name) {
        return new SysRoleVO(id, name);
    }
}
