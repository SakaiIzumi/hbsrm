package net.bncloud.saas.authorize.service.dto;

import net.bncloud.api.feign.saas.user.RoleGroupType;
import net.bncloud.api.feign.saas.user.SubjectType;
import net.bncloud.convert.base.BaseDTO;

public class RoleGroupDTO extends BaseDTO {

    private static final long serialVersionUID = -3741421592605783931L;
    private Long id;
    private String name;
    private RoleGroupType type;
    private SubjectType subjectType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RoleGroupType getType() {
        return type;
    }

    public void setType(RoleGroupType type) {
        this.type = type;
    }

    public SubjectType getSubjectType() {
        return subjectType;
    }

    public void setSubjectType(SubjectType subjectType) {
        this.subjectType = subjectType;
    }
}
