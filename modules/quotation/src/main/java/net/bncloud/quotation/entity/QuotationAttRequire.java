package net.bncloud.quotation.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.bncloud.base.BaseEntity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 附件需求清单
 * </p>
 *
 * @author Auto-generator
 * @since 2022-02-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_rfq_quotation_att_require")

public class QuotationAttRequire extends BaseEntity {

    private static final long serialVersionUID = 1L;


    /**
     * 询价单主键ID
     */
    @NotNull(message = "询价单主键不能为空")
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "询价单主键ID")
    private Long quotationBaseId;

    /**
     * 文件类型
     */
    @NotBlank(message = "文件类型不能为空")
    @ApiModelProperty(value = "文件类型")
    private String fileType;

    /**
     * 阶段类型
     */
    @NotBlank(message = "阶段类型不能为空")
    @ApiModelProperty(value = "阶段类型")
    private String periodType;

    /**
     * 是否必填，1是，0否
     */
    @ApiModelProperty(value = "是否必填，1是，0否")
    private String required;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

}
