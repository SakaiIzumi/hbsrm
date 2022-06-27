package net.bncloud.saas.tenant.service.command;

import lombok.*;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Setter
@Getter
public class CreatedOrgManagerCommand {
    private String name;
    private String mobile;
    private Boolean enabled;


    public static CreatedOrgManagerCommand of(String name, String mobile,Boolean enabled) {
        return new CreatedOrgManagerCommand(name, mobile,enabled);
    }
}
