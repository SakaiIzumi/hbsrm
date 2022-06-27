package net.bncloud.delivery.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.bncloud.base.BaseEntity;

import javax.validation.constraints.NotBlank;

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
public class Attachment extends BaseEntity {

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
