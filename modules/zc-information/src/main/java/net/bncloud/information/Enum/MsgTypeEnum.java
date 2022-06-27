package net.bncloud.information.Enum;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MsgTypeEnum {
    remind("0","提醒"),
    handle("1","办理"),

    ;

    private String code;

    private String name;
}
