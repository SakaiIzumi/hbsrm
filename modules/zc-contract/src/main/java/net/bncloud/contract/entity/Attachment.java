package net.bncloud.contract.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import net.bncloud.base.BaseEntity;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @ClassName Attachment
 * @Description: 附件
 * @Author Administrator
 * @Date 2021/3/13
 * @Version V1.0
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Attachment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 附件ID
     */
    @ApiModelProperty(value = "附件ID")
    @NotBlank(message = "附件ID")
    private String attachmentId;

    /**
     * 附件名称
     */
    @ApiModelProperty(value = "附件名称")
    @NotBlank(message = "附件名称不能为空")
    private String attachmentName;

    /**
     * 附件地址
     */
    @ApiModelProperty(value = "附件地址")
    @NotBlank(message = "附件地址不能为空")
    private String attachmentUrl;
}
