package net.bncloud.saas.authorize.domain.vo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
public class GrantedUser {
    /**
     * 被授权的用户ID
     */
    private Long id;
    /**
     * 被授权的用户姓名
     */
    private String name;
    private String deptIds;
    private String deptNames;
    private Long grantedById;
    private String grantedByName;

    public GrantedUser() {
    }

    public GrantedUser(Long id) {
        this.id = id;
    }

    public GrantedUser(Long id, String name, String deptIds, String deptNames, Long grantedById, String grantedByName) {
        this.id = id;
        this.name = name;
        this.deptIds = deptIds;
        this.deptNames = deptNames;
        this.grantedById = grantedById;
        this.grantedByName = grantedByName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GrantedUser that = (GrantedUser) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
