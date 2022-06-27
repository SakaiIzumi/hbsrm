package net.bncloud.common.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Toby
 */
@AllArgsConstructor
@Getter
public enum YesOrNoNumber {

    YES("1","是"),

    NO("0","否");

    private String code;

    private String name;
}
