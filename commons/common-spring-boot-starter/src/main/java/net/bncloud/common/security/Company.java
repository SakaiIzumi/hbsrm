package net.bncloud.common.security;

import java.io.Serializable;

public class Company implements Serializable {

    private static final long serialVersionUID = 5730314379749243678L;
    private Long id;
    private String name;
    private boolean isAdmin;

    public Company() {
    }

    public Company(Long id, String name, boolean isAdmin) {
        this.id = id;
        this.name = name;
        this.isAdmin = isAdmin;
    }

    public static Company of(Long id, String name, boolean isAdmin) {
        return new Company(id, name, isAdmin);
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

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    @Override
    public String toString() {
        return "Company{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", isAdmin=" + isAdmin +
                '}';
    }
}
