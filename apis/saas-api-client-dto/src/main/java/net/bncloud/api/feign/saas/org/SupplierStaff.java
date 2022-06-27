package net.bncloud.api.feign.saas.org;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.bncloud.common.constants.ManagerType;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SupplierStaff {

    private Long id;
    private String name;
    private String mobile;
    private boolean manager;
    private ManagerType managerType;
    private Long userId;

    public static SupplierStaff of(Long id, String name, String mobile, boolean manager, ManagerType managerType, Long userId) {
        return new SupplierStaff(id, name, mobile, manager, managerType, userId);
    }

    public static SupplierStaff of(Long id, String name, String mobile, boolean manager, ManagerType managerType) {
        return new SupplierStaff(id, name, mobile, manager, managerType, null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SupplierStaff that = (SupplierStaff) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
