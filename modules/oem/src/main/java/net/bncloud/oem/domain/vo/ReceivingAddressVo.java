package net.bncloud.oem.domain.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.bncloud.oem.domain.entity.ReceivingAddress;

import java.io.Serializable;

/**
 * @author liyh
 * @description
 * @since 2022/4/24
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ReceivingAddressVo extends ReceivingAddress implements Serializable {
    private static final long serialVersionUID = 1L;

}
