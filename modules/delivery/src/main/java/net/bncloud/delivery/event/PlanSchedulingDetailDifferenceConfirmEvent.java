package net.bncloud.delivery.event;

import net.bncloud.common.security.LoginInfo;
import net.bncloud.enums.EventCode;
import net.bncloud.event.BizEvent;

import java.io.Serializable;

/**
 * @author ddh
 * @description 计划排程明细差异确认（采购方确认）
 * @since 2022/6/6
 */
public class PlanSchedulingDetailDifferenceConfirmEvent extends BizEvent {
    private final static String eventCode = EventCode.plan_scheduling_detail_difference_confirm.getCode();

    public PlanSchedulingDetailDifferenceConfirmEvent(Object source, LoginInfo loginInfo, Serializable data) {
        super(source, eventCode, loginInfo, data);
    }

    public PlanSchedulingDetailDifferenceConfirmEvent(Object source, LoginInfo loginInfo, Serializable data, String sources, String sourcesName) {
        super(source, eventCode, loginInfo, data, sources, sourcesName);
    }
}
