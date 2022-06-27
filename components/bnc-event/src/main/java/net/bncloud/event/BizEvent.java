package net.bncloud.event;

import net.bncloud.common.security.LoginInfo;

import java.io.Serializable;

public abstract class BizEvent<T extends Serializable> extends AbstractBncEvent<T>  {

    private static final long serialVersionUID = 2167205308468673938L;
    private String sourcesCode;//消息来源Code
    private String sourcesName;//消息来源名字

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source    the object on which the event initially occurred or with
     *                  which the event is associated (never {@code null})
     * @param eventCode 事件编码
     * @param loginInfo 当前用户登录信息
     * @param data 业务数据
     */
    public BizEvent(Object source, String eventCode, LoginInfo loginInfo, T data) {
        super(source, eventCode, loginInfo, data);
    }

    /**
     *
     * @param source
     * @param eventCode
     * @param loginInfo
     * @param data
     * @param sources
     * @param sourcesName
     */
    public BizEvent(Object source, String eventCode, LoginInfo loginInfo, T data,String sources,String sourcesName) {
        super(source, eventCode, loginInfo, data);
        this.sourcesCode= sources;
        this.sourcesName= sourcesName;

    }

    public String getSourcesName() {
        return sourcesName;
    }

    public void setSourcesName(String sourcesName) {
        this.sourcesName = sourcesName;
    }

    public String getSources() {
        return sourcesCode;
    }

    public void setSources(String sources) {
        this.sourcesCode = sources;
    }
}
