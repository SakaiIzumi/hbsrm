package net.bncloud.saas.authorize.service.query;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleQuery {
    private Long id;
    private String name;
    private String description;
    private Long groupId;
    private String groupName;
    private String subjectType;

}
