package net.bncloud.order.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.bncloud.common.api.IResultCode;
@Getter
@AllArgsConstructor
public enum ZcResultCode implements IResultCode {
    OUT_ERROR(5009, "超出有效库存！"),
    NOT_ENOUGH_ERROR(5010, "库存不足！"),
    NOT_STATUS_ERROR(5011, "订单状态异常！"),
    NOT_ZERO_ERROR(5012, "发货数量不能为0！"),
    NOT_ZERO_ERROR_SUB(5013, "释放数量不能为0！"),
    ;

    


    /**
     * code编码
     */
    final int code;
    /**
     * 中文信息描述
     */
    final String message;
}
