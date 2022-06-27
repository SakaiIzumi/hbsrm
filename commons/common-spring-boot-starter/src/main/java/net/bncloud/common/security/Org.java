package net.bncloud.common.security;

import java.io.Serializable;

public class Org implements Serializable {

    private static final long serialVersionUID = -4228879825672601954L;
    private Long id;
    private String name;
    private boolean isAdmin;
    private Long companyId;
    private String companyName;

    public Org() {
    }

    public Org(Long id, String name, boolean isAdmin, Long companyId, String companyName) {
        this.id = id;
        this.name = name;
        this.isAdmin = isAdmin;
        this.companyId = companyId;
        this.companyName = companyName;
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

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @Override
    public String toString() {
        return "Org{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", isAdmin=" + isAdmin +
                ", companyId=" + companyId +
                ", companyName='" + companyName + '\'' +
                '}';
    }
}
