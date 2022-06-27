package net.bncloud.saas.supplier.domain;


import lombok.*;
import net.bncloud.common.domain.AbstractAuditingEntity;
import net.bncloud.saas.purchaser.domain.Purchaser;
import net.bncloud.saas.tenant.domain.Organization;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "t_supplier_info", uniqueConstraints = {@UniqueConstraint(columnNames = {"code"})})
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Supplier extends AbstractAuditingEntity {


    private static final long serialVersionUID = 1524992233870249455L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 供应商编码
     */
    @Column(name = "code", columnDefinition = "varchar(50) comment '供应商编码'")
    private String code;

    @Column(name = "oa_code", columnDefinition = "varchar(50) comment '供应商编码'")
    private String oaCode;

    @Column(name = "name", columnDefinition = "varchar(50) comment '供应商名称'")
    private String name;

    @Column(name = "credit_code", columnDefinition = "varchar(100) comment '统一社会信用代码'")
    private String creditCode;

    @Column(name = "supplier_org_code", columnDefinition = "varchar(100) comment '供应商的组织机构代码'")
    private String supplierOrgCode;

    @Column(name = "remark", columnDefinition = "varchar(1024) comment '备注'")
    private String remark;

    @Column(name = "source_type", columnDefinition = "varchar(50) comment '供应商来源分类'")
    @Enumerated(EnumType.STRING)
    private SupplierSourceType sourceType;

    @Column(name = "oa_type", columnDefinition = "varchar(1024) comment '供应商oa分类'")
    private String oaType;

    @Column(name = "source_id", columnDefinition = "varchar(20) comment '来源ID'")
    private String sourceId;

    @Column(name = "source_code", columnDefinition = "varchar(20) comment '来源编码'")
    private String sourceCode;

    @Column(name = "avatar_url", columnDefinition = "varchar(20) comment '头像'")
    private String avatarUrl;

    @Column(name = "admin_name", columnDefinition = "varchar(20) comment 'admin_name(未使用)'")
    private String adminName;

    @Column(name = "admin_phone", columnDefinition = "varchar(20) comment 'admin_phone(未使用)'")
    private String adminPhone;

    @Column(name = "invite_date", columnDefinition = "varchar(20) comment '邀请日期'")
    private Date inviteDate;

    @Column(name = "relevance_status", columnDefinition = "varchar(20) comment '合作状态'")
    private String relevanceStatus;

    @Column(name = "manager_name", columnDefinition = "varchar(20) comment '管理员名称'")
    private String managerName;

    @Column(name = "manager_mobile", columnDefinition = "varchar(20) comment '管理员联系电话'")
    private String managerMobile;

    @OneToMany(mappedBy = "supplier", fetch = FetchType.EAGER)
    private List<SupplierManager> managers;

    @OneToMany(mappedBy = "supplier", fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    private List<SupplierLinkMan> linkMans;

    @OneToMany(mappedBy = "supplier", fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    private List<SupplierAccount> accounts;

    @OneToOne(mappedBy = "supplier")
    private SupplierExt supplierExt;

    @OneToMany(mappedBy = "supplier", fetch = FetchType.LAZY)
    private List<SupplierStaff> staffs;


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "ss_supplier_tag_item_ref",
            joinColumns = @JoinColumn(name = "supplier_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_item_id"),
            uniqueConstraints = {@UniqueConstraint(name = "supplier_tag_item_uni", columnNames = {"supplier_id", "tag_item_id"})}
    )
    private List<TagConfigItem> tags;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "ss_supplier_type_item_ref",
            joinColumns = @JoinColumn(name = "supplier_id"),
            inverseJoinColumns = @JoinColumn(name = "type_item_id"),
            uniqueConstraints = {@UniqueConstraint(name = "supplier_type_item_uni", columnNames = {"supplier_id", "type_item_id"})}
    )
    private List<TypeConfigItem> types;


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "ss_tenant_org_supplier_ref",
            joinColumns = @JoinColumn(name = "supplier_id"),
            inverseJoinColumns = @JoinColumn(name = "org_id"),
            uniqueConstraints = {@UniqueConstraint(name = "supplier_org_uni", columnNames = {"supplier_id", "org_id"})}
    )
    private List<Organization> organizations;


    @ManyToMany(mappedBy = "suppliers")
    private List<Purchaser> purchasers;


    public void addManager(SupplierManager manager) {
        if (managers == null) {
            managers = new ArrayList<>();
        }
        managers.add(manager);
    }

    public void addLinkMans(SupplierLinkMan linkMan) {
        if (linkMans == null) {
            linkMans = new ArrayList<>();
        }
        linkMans.add(linkMan);
    }

    public void removeLinkMans() {
    }


    public static Supplier of(Long id) {
        Supplier supplier = new Supplier();
        supplier.setId(id);
        return supplier;
    }

}

