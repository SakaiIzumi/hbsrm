package net.bncloud.order.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.bncloud.common.api.IResultCode;

@Getter
@AllArgsConstructor
public enum ColorCode  {
    NONE("none","","正常"),
    CHANGE_ORDER("changeOrder","danger", "订单变更");


    


    /**
     * code编码
     */
    final String code;
    final String color;
    /**
     * 中文信息描述
     */
    final String message;
}
