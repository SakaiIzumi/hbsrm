package net.bncloud.api.event;

public class PublishEventRequest {

    private Long userId;
    private Long orgId;
    private String userName;
    private String eventCode;
    private Object eventData;
    private String sources;
    private String sourcesCode;

    public PublishEventRequest() {
    }


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEventCode() {
        return eventCode;
    }

    public void setEventCode(String eventCode) {
        this.eventCode = eventCode;
    }

    public Object getEventData() {
        return eventData;
    }

    public void setEventData(Object eventData) {
        this.eventData = eventData;
    }

    public String getSources() {
        return sources;
    }

    public void setSources(String sources) {
        this.sources = sources;
    }

    public String getSourcesCode() {
        return sourcesCode;
    }

    public void setSourcesCode(String sourcesCode) {
        this.sourcesCode = sourcesCode;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }
}
