package net.bncloud.quotation.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.bncloud.base.BaseEntity;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 附件信息
 * </p>
 *
 * @author Auto-generator
 * @since 2022-02-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_rfq_quotation_attachment")

public class QuotationAttachment extends BaseEntity {

	private static final long serialVersionUID = 1L;


    /**
     * 文件ID
     */
    @NotBlank(message = "文件ID不能为空")
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
    @NotBlank(message = "文件名称不能为空")
    @ApiModelProperty(value = "文件名称")
    private String fileName;

    /**
     * 文件地址
     */
    @NotBlank(message = "文件地址不能为空")
    @ApiModelProperty(value = "文件地址")
    private String fileUrl;

    /**
     * 业务表单ID
     */
    @NotBlank(message = "业务表单ID不能为空")
    @ApiModelProperty(value = "业务表单ID")
    private Long businessFormId;

    /**
     *业务类型，quotation_base 询价单,quotation_pricing 定价单
     */
    @ApiModelProperty(value = "业务类型")
    private String businessType;

    /**
     * 创建人姓名
     */
    private String createdByName;

}
