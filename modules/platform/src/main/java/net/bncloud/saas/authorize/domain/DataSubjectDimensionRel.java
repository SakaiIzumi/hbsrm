package net.bncloud.saas.authorize.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.bncloud.common.domain.AbstractAuditingEntity;

import javax.persistence.*;

/**
 * 数据适用维度关系 实体类
 */
@Table(name = "ss_sys_data_subject_dimension_rel")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class DataSubjectDimensionRel extends AbstractAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dimension_code", columnDefinition = "varchar(255) comment '维度code'")
    private String dimensionCode;

    @Column(name = "alias", columnDefinition = "varchar(50) comment '别名'")
    private String alias;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    private DataSubject dataSubject;

}
