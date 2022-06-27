package net.bncloud.quotation.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Toby
 */
@AllArgsConstructor
@Getter
public enum QuotationScopeEnum {

    //询价范围
    OPEN("open","公开"),
    SPECIFIED("specified","指定"),
    ;

    private String code;

    private String name;
}
