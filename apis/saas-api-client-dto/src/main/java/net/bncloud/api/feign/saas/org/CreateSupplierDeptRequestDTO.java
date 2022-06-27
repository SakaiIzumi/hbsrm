package net.bncloud.api.feign.saas.org;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.LinkedHashSet;
import java.util.Set;

public class CreateSupplierDeptRequestDTO {
    @NotNull(message = "组织ID不能为空")
    private Long orgId;
    @NotBlank(message = "供应商编码不能为空")
    private String code;
    @NotBlank(message = "供应商名称不能为空")
    private String name;

    private final Set<SupplierStaff> staffs = new LinkedHashSet<>();

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
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

    public Set<SupplierStaff> getStaffs() {
        return staffs;
    }

    public CreateSupplierDeptRequestDTO addStaff(SupplierStaff staff) {
        this.staffs.add(staff);
        return this;
    }


}
