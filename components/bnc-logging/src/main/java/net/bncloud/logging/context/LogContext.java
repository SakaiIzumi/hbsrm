package net.bncloud.logging.context;

import cn.hutool.http.useragent.UserAgent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.bncloud.logging.spi.Principal;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;

@AllArgsConstructor
@Getter
public class LogContext implements Serializable {
    private static final long serialVersionUID = 398308541050940811L;

    private final String requestId;

    /** This is <i>WHO</i>*/
    private final Principal principal;

    /** This is <i>WHAT</i>*/
    private final String resource;

    /** This is <i>ACTION</i>*/
    private final String action;

    /** This is <i>Application from which operation has been performed</i>*/
    private final String application;

    private final String module;

    /** Client IP*/
    private final String clientIp;

    /** Server IP*/
    private final String serverIp;

    private final UserAgent userAgent;

    private final String uri;

    private final String httpMethod;

    private final String className;

    private final String method;

    private final String request;

    private final String response;

    private final String exception;

    private final Instant requestAt;

    private final Instant responseAt;

    private final Duration duration;

    private final Boolean success;

    public LogContext(RequestContext req, ResponseContext resp, Instant requestAt, Instant responseAt) {
        this(req.getRequestId(), req.getPrincipal(), req.getResource(), req.getAction(), req.getApplication(), req.getModule(),
                req.getClientIp(), req.getServerIp(), req.getUserAgent(), req.getUri(), req.getHttpMethod(),
                req.getClassName(), req.getMethod(), req.getRequestParam(),
                resp.getResponse(), resp.getException(),
                requestAt, responseAt, Duration.between(requestAt, responseAt), resp.getSuccess());
    }
}
