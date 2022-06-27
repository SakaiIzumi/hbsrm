package net.bncloud.information.Enum;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OperStatusEnum {
    UNHANDLED("0","未办理"),
    HANDLED("1","已办理"),

    ;

    private String code;

    private String name;
}
