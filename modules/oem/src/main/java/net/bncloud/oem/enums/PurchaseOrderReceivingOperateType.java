package net.bncloud.oem.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liyh
 */

@Getter
@AllArgsConstructor
public enum PurchaseOrderReceivingOperateType {

    CONFIRM("confirm","确认收货按钮"),
    REJECT("reject","退回收货按钮"),
    REMARK("remark","备注"),
    ;

    private String code;

    private String name;
}
