package net.bncloud.delivery.event;

import net.bncloud.common.security.LoginInfo;
import net.bncloud.enums.EventCode;
import net.bncloud.event.BizEvent;

import java.io.Serializable;

/**
 * @author ddh
 * @version 1.0.0
 * @description 送货计划发送事件
 * @since 2022/1/17
 */
public class DeliveryPlanSendEvent extends BizEvent {

    private final static String eventCode = EventCode.delivery_plan_send.getCode();

    public DeliveryPlanSendEvent(Object source, LoginInfo loginInfo, Serializable data) {
        super(source, eventCode, loginInfo, data);
    }

    public DeliveryPlanSendEvent(Object source, LoginInfo loginInfo, Serializable data, String sources, String sourcesName) {
        super(source, eventCode, loginInfo, data, sources, sourcesName);
    }
}
