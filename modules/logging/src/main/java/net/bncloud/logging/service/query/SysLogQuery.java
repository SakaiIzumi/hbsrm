package net.bncloud.logging.service.query;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class SysLogQuery {
    private String application;
    private String action;
    private String uri;
    private String httpMethod;
    private String clientIp;
    private String className;
    private String classMethod;
    private String name;
    private String login;
    private Boolean mobile;
    private String browserName;
    private String osName;
    private Boolean success;
    private Boolean mobileClient;

    private Instant start;
    private Instant end;
}
