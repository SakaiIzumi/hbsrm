package net.bncloud.saas.authorize.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.bncloud.common.domain.AbstractAuditingEntity;

import javax.persistence.*;

/**
 * 数据维度 实体类
 */
@Table(name = "ss_sys_data_dimension")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class DataDimension extends AbstractAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * dimensionType对应多个dimensionCode ,  (采购方编码)在不同服务的维度名都是有所区别,不同开发者开发,表字段命名字段没有统一
     * 采购方编码 purchaser_code, 有的表字段叫purchaser_code ,有的则为customer_code,需要归为一类处理
     */
    @Column(name = "dimension_type", columnDefinition = "varchar(100) comment '维度分类 权限属性统称'")
    private String dimensionType;

    @Column(name = "dimension_name", columnDefinition = "varchar(100) comment '维度名称'")
    private String dimensionName;

    @Column(name = "dimension_code", columnDefinition = "varchar(100) comment '维度编号'")
    private String dimensionCode;

    @Column(name = "key_type", columnDefinition = "varchar(100) comment '数据类型'")
    private String keyType;


}
