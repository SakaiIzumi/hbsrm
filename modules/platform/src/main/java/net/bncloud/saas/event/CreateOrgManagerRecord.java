package net.bncloud.saas.event;

import lombok.*;
import net.bncloud.common.constants.ManagerType;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Setter
public class CreateOrgManagerRecord {

    private String name;
    private String mobile;
    private String managerType;
    private Boolean enabled;

    public static CreateOrgManagerRecord of(String name, String mobile, String managerType, Boolean enabled) {
        return new CreateOrgManagerRecord(name, mobile, managerType, enabled);
    }

    public static CreateOrgManagerRecord of(String name, String mobile) {
        return new CreateOrgManagerRecord(name, mobile, ManagerType.ORG.name(), true);
    }
}
