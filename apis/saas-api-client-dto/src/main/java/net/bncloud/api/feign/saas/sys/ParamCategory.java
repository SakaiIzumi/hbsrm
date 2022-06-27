package net.bncloud.api.feign.saas.sys;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
public class ParamCategory {
    private String categoryCode;
    private String categoryName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParamCategory that = (ParamCategory) o;
        return Objects.equals(categoryCode, that.categoryCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(categoryCode);
    }

    @Override
    public String toString() {
        return "ParamCategory{" +
                "categoryCode='" + categoryCode + '\'' +
                ", categoryName='" + categoryName + '\'' +
                '}';
    }
}
