package net.bncloud.quotation.service.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum MaterialSyncEnums {
    CREATE("A","创建"),
    VERIFING("B","审核中"),
    HAS_VERIFY("C","已审核"),
    RE_VERIFY("D","重新审核")
    ;

    private String code;
    private String name;
}
