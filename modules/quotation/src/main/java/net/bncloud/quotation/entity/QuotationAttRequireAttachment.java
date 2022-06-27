package net.bncloud.quotation.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.bncloud.base.BaseEntity;

/**
 * <p>
 * 附件需求上传文件表
 * </p>
 *
 * @author Auto-generator
 * @since 2022-03-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_rfq_quotation_att_require_attachment")

public class QuotationAttRequireAttachment extends BaseEntity {

    private static final long serialVersionUID = 1L;


    /**
     * 询价单ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "询价单ID")
    private Long quotationBaseId;

    /**
     * 询价单附件需求表ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "询价单附件需求表ID")
    private Long quotationAttRequireId;

    /**
     * 供应商ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "供应商ID")
    private Long supplierId;

    /**
     * 报价次数
     */
    @ApiModelProperty(value = "报价次数")
    private Integer roundNumber;

    /**
     * 文件ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "文件ID")
    private Long fileId;

    /**
     * 文件类型
     */
    @ApiModelProperty(value = "文件类型")
    private String fileType;

    /**
     * 文件名称
     */
    @ApiModelProperty(value = "文件名称")
    private String fileName;

    /**
     * 文件地址
     */
    @ApiModelProperty(value = "文件地址")
    private String fileUrl;

}
