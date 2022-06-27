package net.bncloud.delivery.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;
import net.bncloud.base.BaseEntity;

import java.math.BigDecimal;

/**
 * <p>
 * 订单合同与附件关联关系表
 * </p>
 *
 * @author huangtao
 * @since 2021-03-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_delivery_attachment_rel")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttachmentRel extends BaseEntity {

    private static final long serialVersionUID = 1L;


    /**
     * 业务表单ID 合同表的主键ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "业务表单ID 合同表的主键ID")
    private Long businessFormId;

    /**
     * 附件ID t_sys_attachment.id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "附件ID t_sys_attachment.id")
    private Long attachmentId;

    /**
     * 附件名称
     */
    @ApiModelProperty(value = "附件名称")
    private String attachmentName;

    /**
     * 附件地址
     */
    @ApiModelProperty(value = "附件地址")
    private String attachmentUrl;

    /**
     * 附件大小
     */
    @ApiModelProperty(value = "附件大小")
    private Long attachmentSize;

    /**
     * 业务编码
     */
    @ApiModelProperty(value = "业务编码")
    private String businessCode;

    /**
     * 业务名称
     */
    @ApiModelProperty(value = "业务名称")
    @TableField
    private String businessName;



}
