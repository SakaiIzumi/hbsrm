package net.bncloud.saas.sys.service.command;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;

/**
 * @ClassName EditSignatureConfigCommand
 * @Description: 供应商网签
 * @Author Administrator
 * @Date 2021/4/27
 * @Version V1.0
 **/
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class EditSignatureConfigCommand {

    @NotNull(message = "ID不能为空")
    private Long id;

    private Boolean enabled;

}
