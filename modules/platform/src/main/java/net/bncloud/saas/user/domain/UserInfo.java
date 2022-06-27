package net.bncloud.saas.user.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import net.bncloud.common.domain.AbstractAuditingEntity;
import net.bncloud.saas.user.domain.vo.SupplierVO;
import net.bncloud.saas.user.domain.vo.UserOrgVO;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.List;

/**
 * 用户信息
 */
@Entity
@Table(name = "ss_user_info")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInfo extends AbstractAuditingEntity {
    private static final long serialVersionUID = 8241230185140444982L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 帐号
     */
    @Column(length = 50)
    private String code;
    @Column(length = 50)
    private String name;
    @Column(length = 50)
    private String nickname;
    @Column(length = 20)
    private String mobile;
    @Column(length = 10)
    private String stateCode;
    /** 邮箱 **/
    private String email;

    /**
     * male  男，female 女
     */
    private String gender;
    private String avatar;
    private String birthday;

    /**
     * 钉钉号
     */
    private String dingTalkCode;

    /**
     * 微信号
     */
    private String weChatCode;

    /**
     * QQ号
     */
    private String qqCode;

    @JsonIgnore
    @Embedded
    private UserPassword password;

    @Embedded
    private AccountStatus status;
    @Embedded
    private LoginInfo loginInfo;

    @Enumerated(EnumType.STRING)
    private UserSourceType sourceType;

    @ElementCollection(targetClass = UserOrgVO.class)
    @CollectionTable(
            name = "ss_user_joined_org",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")}
    )
    private List<UserOrgVO> orgList;

    @ElementCollection(targetClass = SupplierVO.class)
    @CollectionTable(
            name = "ss_user_joined_supplier",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")}
    )
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "supplier_id")),
            @AttributeOverride(name = "code", column = @Column(name = "supplier_code")),
            @AttributeOverride(name = "name", column = @Column(name = "supplier_name"))
    })
    private List<SupplierVO> supplierList;

    @JsonIgnoreProperties("userInfo")
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "userInfo", cascade = {CascadeType.REMOVE, CascadeType.PERSIST,
            CascadeType.MERGE})
    private UserCurrent currentInfo;
}