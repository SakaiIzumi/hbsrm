package net.bncloud.contract.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import net.bncloud.base.BaseEntity;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import net.bncloud.common.util.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.OffsetDateTime;

/**
 * <p>
 * 订单操作记录
 * </p>
 *
 * @author huangtao
 * @since 2021-03-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_purchase_contract_operation_log")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractOperationLog extends BaseEntity {

    private static final long serialVersionUID = 1L;


    /**
     * 合同ID t_purchase_order.order_id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "合同ID t_purchase_order.order_id")
    private Long contractId;

    /**
     * 操作人工号
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "操作人工号")
    private Long operatorNo;

    /**
     * 操作人姓名
     */
    @ApiModelProperty(value = "操作人姓名")
    private String operatorName;

    /**
     * 操作内容
     */
    @ApiModelProperty(value = "操作内容")
    private String operateContent;

    /**
     * 说明
     */
    @ApiModelProperty(value = "说明")
    private String remark;



}
