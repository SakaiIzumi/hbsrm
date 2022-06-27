package net.bncloud.serivce.api.order.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName
 * @Description: 币别
 * @Author Administrator
 * @Date 2021/4/25
 * @Version V1.0
 **/
@AllArgsConstructor
@Getter
public enum OrderCurrencyEnum {
    PRE001("PRE001","人民币（CNY"),
    PRE002("PRE002","香港元（HKD）"),
    PRE003("PRE003","欧元（EUR）"),
    PRE004("PRE004","日本日圆（JPY）"),
    PRE005("PRE005","新台币元（TWD）"),
    PRE006("PRE006","英镑（GBP）"),
    PRE007("PRE007","美元（USD）"),
    PRE008("PRE008","马来西亚林吉特（MYR）"),
    PRE009("PRE009","越南盾（VND）"),
    PRE010("PRE010","菲律宾比索（PHP）"),
    PRE011("PRE011","新加坡元（SGD）"),
    PRE012("PRE012","印度尼西亚卢比（IDR）"),
    PRE013("PRE013","泰铢（THB）")


    ;
    private String code;
    private String name;
}
