package net.bncloud.delivery.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;
import net.bncloud.base.BaseEntity;

/**
 * <p>
 * 送货通知操作记录信息表
 * </p>
 *
 * @author huangtao
 * @since 2021-03-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_delivery_operation_log")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryOperationLog extends BaseEntity {

    private static final long serialVersionUID = 1L;


    /**
     * 单据id：送货计划id、送货单id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "单据id：送货计划id、送货单id")
    private Long billId;

    /**
     * 操作人工号
     */
    @ApiModelProperty(value = "操作人工号")
    private String operatorNo;

    /**
     * 操作人姓名
     */
    @ApiModelProperty(value = "操作人姓名")
    private String operatorName;

    /**
     * 操作内容
     */
    @ApiModelProperty(value = "操作内容")
    private String operatorContent;

    /**
     * 说明
     */
    @ApiModelProperty(value = "说明")
    private String remark;


}
