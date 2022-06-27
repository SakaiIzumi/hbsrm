package net.bncloud.saas.supplier.domain;

import lombok.*;
import net.bncloud.common.domain.AbstractAuditingEntity;

import javax.persistence.*;

/**
 * 供应商操作变更记录表
 */
@Entity
@Table(name = "t_supplier_ops_log")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SupplierOpsLog extends AbstractAuditingEntity {

    private static final long serialVersionUID = -626767568462515295L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "supplier_id", columnDefinition = "bigint(20) comment '供应商Id'")
    private Long supplierId;

    @Column(name = "content", columnDefinition = "varchar(1024) comment '操作内容'")
    private String content;

    @Column(name = "remark", columnDefinition = "varchar(1024) comment '说明'")
    private String remark;

    @Column(name = "ops_user_name", columnDefinition = "varchar(20) comment '操作人'")
    private String opsUserName;


}
