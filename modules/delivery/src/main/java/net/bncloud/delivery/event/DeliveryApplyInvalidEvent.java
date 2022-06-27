package net.bncloud.delivery.event;


import net.bncloud.common.security.LoginInfo;
import net.bncloud.enums.EventCode;
import net.bncloud.event.BizEvent;

import java.io.Serializable;

/**
 * @ClassName DeliveryApplyIssueEvent
 * @Description: 送货申请作废事件
 * @Author Administrator
 * @Date 2021/3/23
 * @Version V1.0
 **/
public class DeliveryApplyInvalidEvent extends BizEvent {

//    private static final String eventCode = "delivery_apply:invalid";
    private static final String eventCode = EventCode.delivery_apply_invalid.getCode();

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source    the object on which the event initially occurred or with
     *                  which the event is associated (never {@code null})
     * @param loginInfo
     * @param data
     */
    public DeliveryApplyInvalidEvent(Object source, LoginInfo loginInfo, Serializable data) {
        super(source, eventCode, loginInfo, data);
    }
    public DeliveryApplyInvalidEvent(Object source, LoginInfo loginInfo, Serializable data, String Sources, String SourcesName) {
        super(source, eventCode, loginInfo, data,Sources,SourcesName);
    }
}
