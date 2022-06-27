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
 * 订单合同与附件关联关系表
 * </p>
 *
 * @author huangtao
 * @since 2021-03-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_purchase_attachment_rel")
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
    private String attachmentId;

    /**
     * 业务编码
     */
    @ApiModelProperty(value = "业务编码")
    private String businessCode;

    /**
     * 业务名称
     */
    @ApiModelProperty(value = "业务名称")
    private String businessName;

    /**
     * 文件名称
     */
    @ApiModelProperty(value = "文件名称")
    private String attachmentName;

    /**
     * 文件地址
     */
    @ApiModelProperty(value = "文件地址")
    private String attachmentUrl;

    /**
     * 请求id
     */
    @ApiModelProperty(value = "请求id")
    private Integer requestId;



}
