package net.bncloud.logging.context;


import cn.hutool.http.useragent.UserAgent;
import lombok.Getter;
import lombok.Setter;
import net.bncloud.logging.spi.Principal;

import java.time.Instant;

@Getter
@Setter
public class RequestContext {

    private String requestId;

    private UserAgent userAgent;

    private String uri;

    private String httpMethod;

    private String requestParam;

    private Principal principal;

    private String resource;

    private String action;

    private String application;

    private String module;

    private String className;

    private String method;

    private String clientIp;

    private String serverIp;

    private Instant requestAt;
}
