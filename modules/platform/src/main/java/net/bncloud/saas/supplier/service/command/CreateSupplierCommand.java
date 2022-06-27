package net.bncloud.saas.supplier.service.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import net.bncloud.common.constants.ManagerType;
import net.bncloud.saas.supplier.domain.Supplier;
import net.bncloud.saas.supplier.domain.SupplierManager;
import net.bncloud.saas.supplier.domain.SupplierSourceType;
import net.bncloud.saas.supplier.domain.SupplierStaff;
import org.apache.commons.lang3.StringUtils;

@Getter
@Setter
@Builder
public class CreateSupplierCommand {
    private Long orgId;
    private String code;
    private String name;
    /**
     * 统一社会信用代码
     */
    private String creditCode;
    /**
     * 供应商的组织机构代码
     */
    private String supplierOrgCode;

    private String remark;

    private String sourceId;
    private String sourceCode;

    private String managerName;
    private String managerMobile;

    public Supplier createSupplier() {
        Supplier supplier = new Supplier();
//        supplier.setOrgId(orgId);
        supplier.setCode(code);
        supplier.setName(name);
        supplier.setCreditCode(creditCode);
        supplier.setSupplierOrgCode(supplierOrgCode);
        supplier.setRemark(remark);
        supplier.setSourceId(sourceId);
        supplier.setSourceCode(sourceCode);
        supplier.setSourceType(SupplierSourceType.USER_CREATE);
        supplier.setManagerMobile(managerMobile);
        supplier.setManagerName(managerName);
        return supplier;
    }

    public SupplierManager createMainManager() {
        if (StringUtils.isNotBlank(managerName) && StringUtils.isNotBlank(managerMobile)) {
            SupplierManager manager = new SupplierManager();
            manager.setMobile(managerMobile);
            manager.setName(managerName);
            manager.setManagerType(ManagerType.MAIN);
            return manager;
        }
        return null;
    }


}
