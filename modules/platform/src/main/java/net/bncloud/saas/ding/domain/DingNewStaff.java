package net.bncloud.saas.ding.domain;

import lombok.Getter;
import lombok.Setter;
import net.bncloud.common.domain.AbstractAuditingEntity;

import javax.persistence.*;

@Entity
@Table(name = "ss_ding_new_staff")
@Getter
@Setter
public class DingNewStaff extends AbstractAuditingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long orgId;//所属组织

    private String employeeIds;//对应业务库员工表主键ID集合，以@1@,@2@形式存在

    private String ddId;//钉钉ID
    private String ddName;//钉钉名字
    private String ddMobile;//钉钉手机，和orgId在表中组成唯一约束
    private String ddeptIds;//钉钉部门IDS，以111,222形式存在
}