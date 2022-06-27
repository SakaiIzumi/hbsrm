package net.bncloud.saas.ding.domain;

import lombok.Getter;
import lombok.Setter;
import net.bncloud.common.domain.AbstractAuditingEntity;

import javax.persistence.*;

@Entity
@Table(name = "ss_ding_new_depart")
@Getter
@Setter
public class DingNewDepart extends AbstractAuditingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long departId;//对应业务库部门表主键ID
    private Long ddId;//钉钉ID

    private String ddName;//钉钉名字
    private Long orgId;//所在组织ID
    private Integer level;//层级（组织级为1，逐级递增）

    private Long parentId;//父ID-对应自身的父数据
}
