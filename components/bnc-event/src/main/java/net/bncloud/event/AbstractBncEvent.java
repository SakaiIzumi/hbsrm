package net.bncloud.event;

import net.bncloud.common.security.LoginInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationEvent;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractBncEvent<T extends Serializable> extends ApplicationEvent {

    private static final long serialVersionUID = -7832283811963173882L;

    private final String eventCode;
    private final LoginInfo loginInfo;
    private final T data;

    private final Map<String, Object> additionalInformation = new HashMap<>();

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public AbstractBncEvent(Object source, String eventCode, LoginInfo loginInfo, T data) {
        super(source);
        if (StringUtils.isBlank(eventCode)) {
            throw new IllegalArgumentException("事件类型编码不能为空");
        }
        this.eventCode = eventCode;
        this.loginInfo = loginInfo;
        this.data = data;
    }

    public String getEventCode() {
        return eventCode;
    }

    public LoginInfo getLoginInfo() {
        return loginInfo;
    }

    public T getData() {
        return data;
    }

    public Map<String, Object> getAdditionalInformation() {
        return additionalInformation;
    }
}
