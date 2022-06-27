package net.bncloud.saas.tenant.web.payload;

import lombok.Getter;
import lombok.Setter;
import net.bncloud.saas.tenant.service.command.CreateCompanyCommand;

@Getter
@Setter
public class RegisterCompanyPayload {

    private String companyName;
    private String creditCode;

    public CreateCompanyCommand toCommand() {
        // TODO 检查输入数据格式合法性
        return CreateCompanyCommand.of(companyName, creditCode);
    }
}
