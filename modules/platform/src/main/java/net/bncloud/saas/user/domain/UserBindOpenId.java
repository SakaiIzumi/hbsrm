package net.bncloud.saas.user.domain;

import lombok.Getter;
import lombok.Setter;
import net.bncloud.common.domain.AbstractAuditingEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;

/**
 * 绑定第三方账号
 */
@Entity
@Table(name = "ss_user_bind_openid")
@Getter
@Setter
public class UserBindOpenId extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1799443143379447894L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String openId;
    private String unionId;

    private String nickname;
    private String mobile;
    private String gender;
    private String avatar;

    @Enumerated(EnumType.STRING)
    private OpenIdType openIdType;

    private String accessToken;
    private Instant accessTokenExpireAt;
    private String refreshToken;
    private Instant refreshTokenExpireAt;

    /**
     * 其他附加信息，JSON格式
     */
    @Column(name = "ext_attrs", length = 1024)
    private String extAttrs;
}
