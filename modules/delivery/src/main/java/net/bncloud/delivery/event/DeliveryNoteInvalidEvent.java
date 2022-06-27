package net.bncloud.delivery.event;


import net.bncloud.common.security.LoginInfo;
import net.bncloud.enums.EventCode;
import net.bncloud.event.BizEvent;

import java.io.Serializable;

/**
 * @ClassName DeliveryNoteIssueEvent
 * @Description: 送货通知单作废事件
 * @Author Administrator
 * @Date 2021/3/23
 * @Version V1.0
 **/
public class DeliveryNoteInvalidEvent extends BizEvent {

//    private static final String eventCode = "delivery_note:invalid";

    private static final String eventCode = EventCode.delivery_note_invalid.getCode();
    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source    the object on which the event initially occurred or with
     *                  which the event is associated (never {@code null})
     * @param loginInfo
     * @param data
     */
    public DeliveryNoteInvalidEvent(Object source, LoginInfo loginInfo, Serializable data) {
        super(source, eventCode, loginInfo, data);
    }
    public DeliveryNoteInvalidEvent(Object source, LoginInfo loginInfo, Serializable data, String Sources, String SourcesName) {
        super(source, eventCode, loginInfo, data,Sources,SourcesName);
    }
}
