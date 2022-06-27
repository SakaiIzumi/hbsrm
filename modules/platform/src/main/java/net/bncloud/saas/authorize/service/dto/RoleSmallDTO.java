package net.bncloud.saas.authorize.service.dto;

import java.io.Serializable;
import java.util.Objects;

public class RoleSmallDTO implements Serializable {
    private static final long serialVersionUID = 8513633470202143595L;
    private Long id;

    private String name;

    private String description;

    private RoleGroupDTO group;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public RoleGroupDTO getGroup() {
        return group;
    }

    public void setGroup(RoleGroupDTO group) {
        this.group = group;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RoleSmallDTO that = (RoleSmallDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", group=" + group +
                '}';
    }
}
