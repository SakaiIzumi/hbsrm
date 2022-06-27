package net.bncloud.saas.authorize.domain;

import lombok.Getter;
import lombok.Setter;
import net.bncloud.common.domain.AbstractAuditingEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "oauth_client_details")
@Getter
@Setter
public class ClientDetail extends AbstractAuditingEntity {

    private static final long serialVersionUID = 3528238954657145184L;
    @Id
    private String clientId;
    private String clientSecret;
    private String name;
    @Column(length = 1024)
    private String description;
    @Column(length = 1024)
    private String resourceIds;
    @Column(length = 512)
    private String scope;
    @Column(name = "authorized_grant_types", length = 100)
    private String grantTypes;
    @Column(name = "web_server_redirect_uri", length = 256)
    private String redirectUri;
    @Column(name = "authorities", length = 256)
    private String authorities;
    @Column(name = "access_token_validity", length = 256)
    private int access_token_validity;
    @Column(name = "refresh_token_validity", length = 256)
    private int refreshTokenValidity;
    @Column(name = "additional_information", length = 1024)
    private String additionalInformation;
    @Column(name = "autoapprove", length = 50)
    private String autoapprove;

    @Column(name = "origin_secret", length = 512)
    private String originSecret;

    private boolean disabled;
}
