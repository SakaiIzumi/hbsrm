package net.bncloud.quotation.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CalculateValidateEnum {
    CALCULATE_VALIDATE_DEFAULT_NUM("1","默认数字"),
    ;

    private String code;

    private String name;
}
