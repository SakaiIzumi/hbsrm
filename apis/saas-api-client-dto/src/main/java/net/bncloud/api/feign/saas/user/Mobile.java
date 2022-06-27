package net.bncloud.api.feign.saas.user;

public class Mobile {
    private String mobile;
    private String stateCode;
    private String loginTarget;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getLoginTarget() {
        return loginTarget;
    }

    public Mobile setLoginTarget(String loginTarget) {
        this.loginTarget = loginTarget;
        return this;
    }


}
