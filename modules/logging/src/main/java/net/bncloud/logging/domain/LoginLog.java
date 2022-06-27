package net.bncloud.logging.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;
import net.bncloud.common.web.jackson.CustomInstantDateSerializer;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.Instant;

/**
 * 登录日志
 */
@Entity
@Table(name = "login_log")
@Getter
@Setter
public class LoginLog implements Serializable {
    private static final long serialVersionUID = -8017427313406904920L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String login;
    private String loginType;
    private String mobile;
    private String name;
    private String requestIp;
    private String targetSystem;
    @JsonSerialize(using = CustomInstantDateSerializer.class)
    private Instant loginAt;

    private String platformName;
    private String osName;
    @Embedded
    private Browser browser;

    @Column(name = "is_mobile")
    private Boolean mobileClient;

    private Boolean success;

    @Column(length = 10240)
    private String token;

    private String failReason;


}
