package net.bncloud.saas.tenant.service.command;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class CreateOrgCommand {
    private String name;
    private String id; //无用值

    public CreateOrgCommand of(String name) {
        return new CreateOrgCommand(name, null);
    }

}
