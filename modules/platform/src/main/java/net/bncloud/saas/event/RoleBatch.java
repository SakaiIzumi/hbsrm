package net.bncloud.saas.event;

import lombok.Data;

import java.util.List;

@Data
public class RoleBatch {

    private List<Long> userIds;
    private List<Long> roleIds;
    private String subjectType;

    public static RoleBatch of(List<Long> userIds, List<Long> roleIds, String subjectType) {
        return new RoleBatch(userIds, roleIds, subjectType);
    }

    public RoleBatch() {

    }

    public RoleBatch(List<Long> userIds, List<Long> roleIds, String subjectType) {
        this.userIds = userIds;
        this.roleIds = roleIds;
        this.subjectType = subjectType;
    }
}
