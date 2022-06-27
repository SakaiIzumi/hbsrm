package net.bncloud.logging.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.Instant;

@Entity
@Table(name = "logging_sys_log")
@Getter
@Setter
public class SysLog implements Serializable {

    private static final long serialVersionUID = 5497506611996871864L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String requestId;
    private Long userId;
    private String login;
    private String name;
    private String application;
    private String module;
    private String action;
    private String resource;
    private String clientIp;
    private String serverIp;
    private String uri;
    private String httpMethod;
    private String className;
    private String classMethod;
    private Instant requestAt;
    private Instant responseAt;
    private Boolean success;
    private Long duration;

    private String platformName;
    private String osName;
    @Embedded
    private Browser browser;
    @Column(name = "is_mobile")
    private Boolean mobile;

    @Lob
    @Column(name = "request")
    private String request;
    @Lob
    @Column(name = "response")
    private String response;
    @Lob
    @Column(name = "exception")
    private String exception;
}
