package net.bncloud.financial.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.bncloud.base.BaseEntity;
import net.bncloud.financial.enums.AttachmentBizType;

/**
 * <p>
 * 对账附件信息表
 * </p>
 *
 * @author Auto-generator
 * @since 2021-12-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_financial_attachment")

public class FinancialAttachment extends BaseEntity {

    private static final long serialVersionUID = 1L;


    /**
     * 业务表单ID
     */
    @ApiModelProperty(value = "业务表单ID")
    private String businessFormId;

    /**
     * 附件ID
     */
    @ApiModelProperty(value = "附件ID")
    private String attachmentId;

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
     * {@link AttachmentBizType}
     */
    @ApiModelProperty("附件业务类型 0-对账单  1-发票通知单 2-费用单")
    private Integer attachmentBizType;

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

}
