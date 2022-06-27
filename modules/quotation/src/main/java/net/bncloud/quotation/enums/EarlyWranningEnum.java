package net.bncloud.quotation.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liyh
 */
@AllArgsConstructor
@Getter
public enum EarlyWranningEnum {

    //询价范围
    OPEN("OPEN","开启"),
    CLOSE("CLOSE","关闭"),
    ;

    private String code;

    private String name;
}
