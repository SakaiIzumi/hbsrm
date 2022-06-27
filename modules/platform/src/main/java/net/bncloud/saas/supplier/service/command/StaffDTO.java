package net.bncloud.saas.supplier.service.command;

import lombok.Getter;
import lombok.Setter;
import net.bncloud.common.constants.ManagerType;

@Getter
@Setter
public class StaffDTO {
    private String name;
    private String mobile;
    private boolean manager;
    private ManagerType managerType;
}
