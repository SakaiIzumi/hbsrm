package net.bncloud.saas.user.domain;

import javax.persistence.Embeddable;
import java.time.Instant;

@Embeddable
public class LoginInfo {
    /** 上次登录的IP **/
    private String lastLoginIp;
    /** 上次登录的时间 **/
    private Instant lastLoginTime;
    /** 登录次数 **/
    private Integer loginCount;
    /** 登录错误次数 **/
    private Integer loginErrorCount;
    /** 开始登陆错误时间（登陆成功后会清空） **/
    private Instant firstLoginErrorTime;

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public Instant getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Instant lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Integer getLoginCount() {
        return loginCount;
    }

    public void setLoginCount(Integer loginCount) {
        this.loginCount = loginCount;
    }

    public Integer getLoginErrorCount() {
        return loginErrorCount;
    }

    public void setLoginErrorCount(Integer loginErrorCount) {
        this.loginErrorCount = loginErrorCount;
    }

    public Instant getFirstLoginErrorTime() {
        return firstLoginErrorTime;
    }

    public void setFirstLoginErrorTime(Instant firstLoginErrorTime) {
        this.firstLoginErrorTime = firstLoginErrorTime;
    }
}
