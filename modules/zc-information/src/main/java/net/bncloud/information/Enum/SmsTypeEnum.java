package net.bncloud.information.Enum;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SmsTypeEnum {
    winner("winner","中标"),
    loser("loser","未中标"),

    ;

    private String code;

    private String name;
}
