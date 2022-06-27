package net.bncloud.saas.user.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import net.bncloud.common.domain.AbstractAuditingEntity;
import net.bncloud.saas.user.domain.vo.SupplierVO;
import net.bncloud.saas.user.domain.vo.UserOrgVO;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "ss_user_info_current")
@Getter
@Setter
public class UserCurrent extends AbstractAuditingEntity {
    private static final long serialVersionUID = -4810185924231125796L;
    @Id
    private Long userId;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "companyId", column = @Column(name = "current_company_id")),
            @AttributeOverride(name = "companyName", column = @Column(name = "current_company_name")),
            @AttributeOverride(name = "orgId", column = @Column(name = "current_org_id")),
            @AttributeOverride(name = "orgName", column = @Column(name = "current_org_name"))
    })
    private UserOrgVO currentOrg;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "current_supplier_id")),
            @AttributeOverride(name = "code", column = @Column(name = "current_supplier_code")),
            @AttributeOverride(name = "name", column = @Column(name = "current_supplier_name"))
    })
    private SupplierVO currentSupplier;

    @JsonIgnoreProperties("currentInfo")
    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private UserInfo userInfo;


    //TODO 新增默认导航列
    @Column(name = "current_menu_nav_type")
    private String currentMenuNavType;




}
