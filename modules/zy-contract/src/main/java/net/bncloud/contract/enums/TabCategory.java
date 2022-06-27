package net.bncloud.contract.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName TabCategory
 * @Description: tab类型枚举
 * @Author Administrator
 * @Date 2021/3/16
 * @Version V1.0
 **/
@AllArgsConstructor
@Getter
public enum TabCategory {
    ALL("0","所有"),
    DRAFT("1","草稿"),
    TO_BE_ANSWERED("2","待答交"),
    REJECTED("3","被拒"),
    TO_BE_SIGNED_ONLINE("4","待网签"),
    ABNORMAL("5","异常"),
    VALID("6","有效"),
    NEAR_EXPIRY("6.1","临近到期"),
    INVALID("7","无效"),
    EXPIRED("8","过期")

    ;

    private String  code;

    private String name;
}
