package net.bncloud.information.Enum;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InformationReadStatusEnum {
    UNREAD("0","未读"),
    READ("1","已读"),

    ;

    private String code;

    private String name;
}
