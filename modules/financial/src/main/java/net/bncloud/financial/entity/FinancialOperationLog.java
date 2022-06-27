package net.bncloud.financial.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.bncloud.base.BaseEntity;

import java.io.Serializable;

/**
 * <p>
 * 对账单操作记录日志表
 * </p>
 *
 * @author Auto-generator
 * @since 2021-12-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_financial_operation_log")

public class FinancialOperationLog extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 单ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "单据id")
    private Long billId;

    @ApiModelProperty(value = "单据类型")
    private String billType;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "操作人工号")
    private Long operationNo;

    @ApiModelProperty(value = "操作人名称")
    private String operatorName;

    /**
     * 操作内容
     */
    @ApiModelProperty(value = "操作内容")
    private String content;

    /**
     * 说明
     */
    @ApiModelProperty(value = "说明")
    private String remark;

}
