package net.bncloud.oem.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liyh
 * @version 1.0.0
 * @description
 * @since 2022/4/24
 */
@Getter
@AllArgsConstructor
public enum PurchaseOrderAddressStatus {
    //   状态：0待维护，1已维护
    TO_BE_MAINTAINED("0","待维护"),
    HAVE_MAINTAINED("1","已维护"),
    ;
    private String code;
    private String name;
}
