package net.bncloud.delivery.event;


import net.bncloud.common.security.LoginInfo;
import net.bncloud.enums.EventCode;
import net.bncloud.event.BizEvent;

import java.io.Serializable;

/**
 * @ClassName DeliveryNoteDeleteEvent
 * @Description: 送货通知单删除事件
 * @Author Administrator
 * @Date 2021/3/23
 * @Version V1.0
 **/
public class DeliveryNoteDeleteEvent extends BizEvent {

//    private static final String eventCode = "delivery_note:delete";

    private static final String eventCode = EventCode.delivery_note_delete.getCode();
    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source    the object on which the event initially occurred or with
     *                  which the event is associated (never {@code null})
     * @param loginInfo
     * @param data
     */
    public DeliveryNoteDeleteEvent(Object source, LoginInfo loginInfo, Serializable data) {
        super(source, eventCode, loginInfo, data);
    }
    public DeliveryNoteDeleteEvent(Object source, LoginInfo loginInfo, Serializable data, String Sources, String SourcesName) {
        super(source, eventCode, loginInfo, data,Sources,SourcesName);
    }
}
