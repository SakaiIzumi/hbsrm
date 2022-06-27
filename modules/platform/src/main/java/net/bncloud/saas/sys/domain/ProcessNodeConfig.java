package net.bncloud.saas.sys.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.bncloud.common.domain.AbstractAuditingEntity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 流程节点设置
 * @author ddh
 * @version 1.0.0
 * @description
 * @since 2022/1/4
 */
@Getter
@Setter
@Entity
@Table(name = "ss_sys_process_node_config")
public class ProcessNodeConfig extends AbstractAuditingEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ApiModelProperty(name = "参数id")
    private Long configParamId;
    @ApiModelProperty(name = "业务节点名称")
    private String businessNodeName;
    @ApiModelProperty(name = "开关：true开，false关")
    private String status;
    @ApiModelProperty(name = "流程编码")
    private String processCode;






}
