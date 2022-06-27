package net.bncloud.quotation.entity;


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
 * 询价单操作记录日志表
 * </p>
 *
 * @author Auto-generator
 * @since 2022-03-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Builder
@TableName("t_quotation_operation_log")
@AllArgsConstructor
@NoArgsConstructor
public class QuotationOperationLog extends BaseEntity {

    private static final long serialVersionUID = 1L;


    /**
     * 单据id：询价单ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "单据id：询价单ID")
    private Long billId;

    /**
     * 单据类型：角色
     */
    @ApiModelProperty(value = "单据类型：角色")
    private String billType;

    /**
     * 操作人工号
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "操作人工号")
    private Long operationNo;

    /**
     * 操作人姓名
     */
    @ApiModelProperty(value = "操作人姓名")
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
