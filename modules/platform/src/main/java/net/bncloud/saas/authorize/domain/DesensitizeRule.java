package net.bncloud.saas.authorize.domain;


import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * 脱敏字段映射表
 */
@Table(name = "ss_sys_desensitize_rule")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class DesensitizeRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rule_name", columnDefinition = "varchar(50) comment '脱敏规则名'")
    private String ruleName;


    @Column(name = "rule_pattern", columnDefinition = "varchar(255) comment '脱敏规则'")
    private String rulePattern;
}
