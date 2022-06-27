package net.bncloud.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TerminalType {


    PC("1","PC端"),
    WX("2","微信"),
    DD("3","钉钉"),
    EMAIL("4","邮件");


    private String code;

    private String name;
}
