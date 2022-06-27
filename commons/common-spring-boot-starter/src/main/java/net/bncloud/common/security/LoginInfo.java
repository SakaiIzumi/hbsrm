package net.bncloud.common.security;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public class LoginInfo implements Serializable {
    private static final long serialVersionUID = 8510385245248405885L;
    private Long id;
    private String name;
    private String mobile;
    private Platform platform;
    private Company currentCompany;
    private Org currentOrg;
    private Supplier currentSupplier;
    private Set<Role> roles;
    private List<MenuNavDto> menuNavs;
    private String currentMenuNav;
    private String currentSubjectType;
    private List<Org> orgs;
    private List<Supplier> suppliers;

    public LoginInfo() {
    }

    public LoginInfo(Long id, String mobile, String name, Platform platform,
                     Company currentCompany,
                     Org currentOrg,
                     Supplier currentSupplier,
                     Set<Role> roles,
                     List<MenuNavDto> menuNavs,
                     String currentMenuNav,
                     String currentSubjectType,
                     List<Org> orgs,
                     List<Supplier> suppliers
    ) {
        this.id = id;
        this.mobile = mobile;
        this.name = name;
        this.platform = platform;
        this.currentCompany = currentCompany;
        this.currentOrg = currentOrg;
        this.currentSupplier = currentSupplier;
        this.roles = roles;
        this.menuNavs = menuNavs;
        this.currentMenuNav = currentMenuNav;
        this.currentSubjectType = currentSubjectType;
        this.orgs = orgs;
        this.suppliers = suppliers;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    public Company getCurrentCompany() {
        return currentCompany;
    }

    public void setCurrentCompany(Company currentCompany) {
        this.currentCompany = currentCompany;
    }

    public Org getCurrentOrg() {
        return currentOrg;
    }

    public void setCurrentOrg(Org currentOrg) {
        this.currentOrg = currentOrg;
    }

    public Supplier getCurrentSupplier() {
        return currentSupplier;
    }

    public void setCurrentSupplier(Supplier currentSupplier) {
        this.currentSupplier = currentSupplier;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public boolean isSupplier() {
        return currentSupplier != null;
    }

    public List<MenuNavDto> getMenuNavs() {
        return menuNavs;
    }

    public void setMenuNavs(List<MenuNavDto> menuNavs) {
        this.menuNavs = menuNavs;
    }

    public String getCurrentMenuNav() {
        return currentMenuNav;
    }

    public void setCurrentMenuNav(String currentMenuNav) {
        this.currentMenuNav = currentMenuNav;
    }

    public String getCurrentSubjectType() {
        return currentSubjectType;
    }

    public void setCurrentSubjectType(String currentSubjectType) {
        this.currentSubjectType = currentSubjectType;
    }

    public List<Org> getOrgs() {
        return orgs;
    }

    public void setOrgs(List<Org> orgs) {
        this.orgs = orgs;
    }

    public List<Supplier> getSuppliers() {
        return suppliers;
    }

    public void setSuppliers(List<Supplier> suppliers) {
        this.suppliers = suppliers;
    }
}
