package net.bncloud.saas.ding.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import net.bncloud.common.domain.AbstractAuditingEntity;
import net.bncloud.saas.ding.domain.pk.DingUserPK;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

@Entity
@Table(name = "ss_ding_user")
@Getter
@Setter
public class DingUser extends AbstractAuditingEntity {
    private static final long serialVersionUID = -6144970208142419901L;

    @EmbeddedId
    private DingUserPK id;

    private String unionId;

    private String name;

    private String avatar;

    @Column(name = "state_code", length = 10)
    private String stateCode;

    @Column(length = 20)
    private String mobile;

    /**
     * 是否号码隐藏：
     *
     * true：隐藏
     * false：不隐藏
     */
    private Boolean hideMobile;

    /**
     * 工号
     */
    private String jobNumber;

    private String title;

    private String email;

    /**
     * 办公地点。
     */
    private String workPlace;

    private String remark;

    /**
     * 是否激活了钉钉：
     *
     * true：已激活
     * false：未激活
     */
    @Column(name = "is_active")
    private Boolean active;

    /**
     * 是否完成了实名认证：
     *
     * true：已认证
     * false：未认证
     */
    private Boolean realAuthed;

    /**
     * 是否为企业的高管：
     *
     * true：是
     * false：不是
     */
    @Column(name = "is_senior")
    private Boolean senior;

    /**
     * 是否为企业的管理员：
     *
     * true：是
     * false：不是
     */
    @Column(name = "is_admin")
    private Boolean admin;

    /**
     * 是否为企业的老板：
     *
     * true：是
     * false：不是
     */
    @Column(name = "is_boss")
    private Boolean boss;

    @JsonIgnoreProperties("users")
    @MapsId("corpId")
    @ManyToOne
    @JoinColumn(name = "corp_id", referencedColumnName = "corp_id", nullable = false, updatable = false)
    private DingTalkCorp corp;

    private String deptIdList;

    @Override
    public String toString() {
        return "DingUser{" +
                "id=" + id +
                ", unionId='" + unionId + '\'' +
                ", name='" + name + '\'' +
                ", avatar='" + avatar + '\'' +
                ", stateCode='" + stateCode + '\'' +
                ", mobile='" + mobile + '\'' +
                ", hideMobile=" + hideMobile +
                ", jobNumber='" + jobNumber + '\'' +
                ", title='" + title + '\'' +
                ", email='" + email + '\'' +
                ", workPlace='" + workPlace + '\'' +
                ", remark='" + remark + '\'' +
                ", active=" + active +
                ", realAuthed=" + realAuthed +
                ", senior=" + senior +
                ", admin=" + admin +
                ", boss=" + boss +
                ", corp=" + corp +
                '}';
    }
}