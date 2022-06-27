package net.bncloud.oem.domain.param;

import lombok.Data;
import net.bncloud.oem.domain.entity.ReceivingAddress;

import java.io.Serializable;

/**
 * @author liyh
 * @description
 * @since 2022/4/24
 */
@Data
public class ReceivingAddressParam extends ReceivingAddress implements Serializable {
    private static final long serialVersionUID = 1L;

}
