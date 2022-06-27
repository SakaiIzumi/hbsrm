package net.bncloud.saas.tenant.service.command;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransferOrgManagerCommand {

    private Long userId;
    private String name;
    private String mobile;
    private String smsCode;
}
