package net.bncloud.delivery.event;

import net.bncloud.common.security.LoginInfo;
import net.bncloud.enums.EventCode;
import net.bncloud.event.BizEvent;

import java.io.Serializable;

/**
 * @author ddh
 * @description 计划排程明细发送事件
 * @since 2022/6/2
 */
public class PlanSchedulingDetailSendEvent extends BizEvent {
    private final static String eventCode = EventCode.plan_scheduling_detail_send.getCode();
    public PlanSchedulingDetailSendEvent(Object source,  LoginInfo loginInfo, Serializable data) {
        super(source, eventCode, loginInfo, data);
    }

    public PlanSchedulingDetailSendEvent(Object source,  LoginInfo loginInfo, Serializable data, String sources, String sourcesName) {
        super(source, eventCode, loginInfo, data, sources, sourcesName);
    }
}
