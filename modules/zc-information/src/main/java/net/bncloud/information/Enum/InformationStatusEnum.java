package net.bncloud.information.Enum;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InformationStatusEnum {
    Effective("0","有效"),
    invalid("1","失效"),

    ;

    private String code;

    private String name;
}
