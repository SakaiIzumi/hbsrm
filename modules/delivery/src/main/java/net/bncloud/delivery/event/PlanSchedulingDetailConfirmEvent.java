package net.bncloud.delivery.event;

import net.bncloud.common.security.LoginInfo;
import net.bncloud.enums.EventCode;
import net.bncloud.event.BizEvent;

import java.io.Serializable;

/**
 * @author ddh
 * @description 计划排程明细确认事件
 * @since 2022/6/2
 */
public class PlanSchedulingDetailConfirmEvent extends BizEvent {
    private final static String eventCode = EventCode.plan_scheduling_detail_confirm.getCode();

    public PlanSchedulingDetailConfirmEvent(Object source, LoginInfo loginInfo, Serializable data) {
        super(source, eventCode, loginInfo, data);
    }

    public PlanSchedulingDetailConfirmEvent(Object source, LoginInfo loginInfo, Serializable data, String sources, String sourcesName) {
        super(source, eventCode, loginInfo, data, sources, sourcesName);
    }
}
