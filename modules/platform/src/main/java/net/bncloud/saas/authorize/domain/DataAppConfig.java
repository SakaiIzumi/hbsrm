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
@Table(name = "ss_sys_data_app_config")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class DataAppConfig extends AbstractAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "app_code", columnDefinition = "varchar(100) comment '应用编号'")
    private String appCode;

    @Column(name = "app_name", columnDefinition = "varchar(20) comment '名称'")
    private String appName;


    @OneToMany(mappedBy = "dataAppConfig")
    private List<DataSubject> dataSubjects;

}
