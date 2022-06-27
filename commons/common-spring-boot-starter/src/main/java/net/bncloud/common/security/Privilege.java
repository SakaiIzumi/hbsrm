package net.bncloud.common.security;

import java.io.Serializable;
import java.util.Objects;

public class Privilege implements Serializable {

    private static final long serialVersionUID = 8442772773063408815L;
    private String code;
    private String name;

    public Privilege() {
    }

    public Privilege(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Privilege privilege = (Privilege) o;
        return Objects.equals(code, privilege.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }
}
