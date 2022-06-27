package net.bncloud.order.constants;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CfgParamCode {

    ORDER_NET_SIGN_SWITCH(2,"order:order_net_sign","订单网签开关");

    final long companyId;
    final String code;
    final String remark;
}
