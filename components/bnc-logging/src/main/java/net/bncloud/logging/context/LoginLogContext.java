package net.bncloud.logging.context;

import cn.hutool.http.useragent.UserAgent;
import org.apache.commons.lang3.StringUtils;

import java.time.Instant;

public class LoginLogContext {
    private UserAgent userAgent;
    private String loginType;
    private Long userId;
    private String name;
    private String login;
    private String mobile;
    private String requestIp;
    private Instant loginAt;
    private Boolean success;
    private String failReason;
    private String token;


    public UserAgent getUserAgent() {
        return userAgent;
    }

    public LoginLogContext setUserAgent(UserAgent userAgent) {
        this.userAgent = userAgent;
        return this;
    }

    public String getLoginType() {
        return loginType;
    }

    public LoginLogContext setLoginType(String loginType) {
        this.loginType = loginType;
        return this;
    }

    public LoginLogContext setLoginTypeIfAbsent(String loginType) {
        if (StringUtils.isBlank(this.loginType)) {
            this.loginType = loginType;
        }
        return this;
    }

    public Long getUserId() {
        return userId;
    }

    public LoginLogContext setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public String getName() {
        return name;
    }

    public LoginLogContext setName(String name) {
        this.name = name;
        return this;
    }

    public String getLogin() {
        return login;
    }

    public LoginLogContext setLogin(String login) {
        this.login = login;
        return this;
    }

    public String getMobile() {
        return mobile;
    }

    public LoginLogContext setMobile(String mobile) {
        this.mobile = mobile;
        return this;
    }

    public String getRequestIp() {
        return requestIp;
    }

    public LoginLogContext setRequestIp(String requestIp) {
        this.requestIp = requestIp;
        return this;
    }

    public Instant getLoginAt() {
        return loginAt;
    }

    public LoginLogContext setLoginAt(Instant loginAt) {
        this.loginAt = loginAt;
        return this;
    }

    public Boolean getSuccess() {
        return success;
    }

    public LoginLogContext setSuccess(Boolean success) {
        this.success = success;
        return this;
    }

    public String getFailReason() {
        return failReason;
    }

    public LoginLogContext setFailReason(String failReason) {
        this.failReason = failReason;
        return this;
    }

    public String getToken() {
        return token;
    }

    public LoginLogContext setToken(String token) {
        this.token = token;
        return this;
    }
}
