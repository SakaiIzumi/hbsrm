package net.bncloud.oem.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BillTypeEnum {
    STANDARD("CGDD01_SYS","RKD01_SYS","标准入库单"),
    OUTSOURCING("CGDD02_SYS","RKD03_SYS","委外入库单"),;

    private String code;
    private String changeCode;
    private String msg;



}
