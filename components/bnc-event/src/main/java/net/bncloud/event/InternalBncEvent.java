package net.bncloud.event;

import net.bncloud.common.security.LoginInfo;

import java.io.Serializable;

public abstract class InternalBncEvent<T extends Serializable> extends AbstractBncEvent<T> {
    private static final long serialVersionUID = -1988211002819470956L;

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source    the object on which the event initially occurred or with
     *                  which the event is associated (never {@code null})
     * @param eventCode
     * @param loginInfo
     * @param data
     */
    public InternalBncEvent(Object source, String eventCode, LoginInfo loginInfo, T data) {
        super(source, eventCode, loginInfo, data);
    }
}
