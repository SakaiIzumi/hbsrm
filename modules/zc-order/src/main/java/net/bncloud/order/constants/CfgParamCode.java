package net.bncloud.order.constants;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CfgParamCode {

    ORDER_NET_SIGN_SWITCH("order:order_net_sign","订单网签开关"),
    ORDER_ATUO_SEND("order:auto_send","订单自动发送");
    final String code;
    final String remark;
}
