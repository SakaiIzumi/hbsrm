package net.bncloud.delivery.event;

import net.bncloud.common.security.LoginInfo;
import net.bncloud.enums.EventCode;
import net.bncloud.event.BizEvent;

import java.io.Serializable;

/**
 * @author ddh
 * @version 1.0.0
 * @description 送货计划确认事件
 * @since 2022/1/17
 */
public class DeliveryPlanConfirmEvent extends BizEvent {
    private final static String eventCode = EventCode.delivery_plan_confirm.getCode();

    public DeliveryPlanConfirmEvent(Object source, LoginInfo loginInfo, Serializable data) {
        super(source, eventCode, loginInfo, data);
    }

    public DeliveryPlanConfirmEvent(Object source, LoginInfo loginInfo, Serializable data, String sources, String sourcesName) {
        super(source, eventCode, loginInfo, data, sources, sourcesName);
    }
}
