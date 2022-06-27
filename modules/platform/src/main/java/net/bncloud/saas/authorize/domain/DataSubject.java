package net.bncloud.saas.authorize.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.bncloud.common.domain.AbstractAuditingEntity;

import javax.persistence.*;
import java.util.List;

/**
 * 数据主题
 */
@Table(name = "ss_sys_data_subject")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class DataSubject extends AbstractAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "app_id")
    private DataAppConfig dataAppConfig;

    @Column(name = "name", columnDefinition = "varchar(20) comment '主题名称'")
    private String name;

    @Column(name = "key_value", columnDefinition = "varchar(20) comment '主题key:mapperId或请求api的值'")
    private String keyValue;

    @Column(name = "key_type", columnDefinition = "varchar(20) comment '主题类型 mapper或api '")
    private String keyType;


}
