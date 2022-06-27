package net.bncloud.saas.supplier.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.Accessors;
import net.bncloud.common.domain.AbstractAuditingEntity;

import javax.persistence.*;

@Entity
@Table(name = "t_supplier_link_man")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupplierLinkMan extends AbstractAuditingEntity {
    private static final long serialVersionUID = 3917437660881823975L;

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "name",nullable = false, columnDefinition = "varchar(50) comment '姓名'")
    private String name;

    @Column(name = "position", columnDefinition = "varchar(20) comment '职位'")
    private String position;

    @Column(name = "mobile", columnDefinition = "varchar(20) comment '手机'")
    private String mobile;

    @Column(name = "qq", columnDefinition = "varchar(20) comment 'QQ'")
    private String qq;

    @Column(name = "department", columnDefinition = "varchar(20) comment '部门'")
    private String department;

    @Column(name = "link_phone", columnDefinition = "varchar(20) comment '联系电话'")
    private String linkPhone;

    @Column(name = "email", columnDefinition = "varchar(20) comment '邮箱'")
    private String email;

    @Column(name = "wechat_id", columnDefinition = "varchar(20) comment '微信号'")
    private String wechatId;

    @Column(name = "wisdom_easily_id", columnDefinition = "varchar(20) comment '智易号'")
    private String wisdomEasilyId;

    @Column(name = "fax", columnDefinition = "varchar(20) comment '传真'")
    private String fax;

    @Column(name = "allow_ops", nullable = false, columnDefinition = "bit(1) comment '允许操作'")
    private Boolean allowOps;

    @Column(name = "source_type", nullable = false, columnDefinition = "varchar(50) comment '来源类型'")
    @Enumerated(EnumType.STRING)
    private SupplierSourceType sourceType;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;
}
