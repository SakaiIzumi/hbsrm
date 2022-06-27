package net.bncloud.common.security;

import java.io.Serializable;
import java.util.Objects;

public class Role implements Serializable {
    private static final long serialVersionUID = 8424517537922621024L;
    private Long id;
    private String name;
    private boolean sysDefault;

    public Role() {
    }

    public Role(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Role(Long id, String name, boolean sysDefault) {
        this.id = id;
        this.name = name;
        this.sysDefault = sysDefault;
    }

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

    public boolean isSysDefault() {
        return sysDefault;
    }

    public void setSysDefault(boolean sysDefault) {
        this.sysDefault = sysDefault;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return Objects.equals(id, role.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
