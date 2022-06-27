package net.bncloud.oem.domain.param;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author ddh
 * @description 退回收货
 * @since 2022/4/25
 */
@Data
public class ReturnReceivingParam implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 收货id
     */
    @NotNull(message = "收货id不能为空")
    private Long receivingId;
    /**
     * 备注
     */
    private String remark;
}
