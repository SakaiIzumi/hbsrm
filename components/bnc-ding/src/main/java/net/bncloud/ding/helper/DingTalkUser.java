package net.bncloud.ding.helper;

public class DingTalkUser {
    private String deviceId;
    private Boolean isSys;
    private String sysLevel;
    private String userId;

    public DingTalkUser(String deviceId, Boolean isSys, String sysLevel, String userId) {
        this.deviceId = deviceId;
        this.isSys = isSys;
        this.sysLevel = sysLevel;
        this.userId = userId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Boolean getSys() {
        return isSys;
    }

    public void setSys(Boolean sys) {
        isSys = sys;
    }

    public String getSysLevel() {
        return sysLevel;
    }

    public void setSysLevel(String sysLevel) {
        this.sysLevel = sysLevel;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
