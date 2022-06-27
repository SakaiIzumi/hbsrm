package net.bncloud.delivery.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WarehouseEnum {
    DEFAULT_PUR_CODE("100","默认不需要改变warehouse的采购编码"),
    SYMBOL("-","需要添加的符号"),
    ;
    private String code;
    private String msg;

}
