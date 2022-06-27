package net.bncloud.saas.purchaser.domain;

import lombok.*;
import net.bncloud.common.domain.AbstractAuditingEntity;
import net.bncloud.saas.tenant.domain.Organization;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "ss_tenant_purchaser_staff")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class PurchaserStaff extends AbstractAuditingEntity {
    private static final long serialVersionUID = -1524895481910922392L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", columnDefinition = "varchar(60) comment '姓名'")
    private String name;

    @Column(name = "mobile", columnDefinition = "varchar(60) comment '电话'")
    private String mobile;

    @Column(name = "position", columnDefinition = "varchar(60) comment '岗位'")
    private String position;

    @Column(name = "job_no", columnDefinition = "varchar(60) comment '工号'")
    private String jobNo;

    @Column(name = "department", columnDefinition = "varchar(60) comment '部门'")
    private String department;

    @ManyToOne
    @JoinColumn(name = "purchaser_id", referencedColumnName = "id", columnDefinition = "bigint(20) comment '采购方关联id'")
    private Purchaser purchaser;
}
