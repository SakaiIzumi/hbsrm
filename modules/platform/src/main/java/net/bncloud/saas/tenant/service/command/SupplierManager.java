package net.bncloud.saas.tenant.service.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.bncloud.common.constants.ManagerType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SupplierManager {
    private Long sourceId;
    private String mobile;
    private String name;
//    private ManagerType managerType;

}
