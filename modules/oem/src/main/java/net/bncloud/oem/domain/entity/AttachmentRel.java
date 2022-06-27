package net.bncloud.oem.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.bncloud.base.BaseEntity;

 
/**
 * @author ddh
 * @since 2022/4/24
 * @description
 */
/**
    * 附件关联关系表 
    */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("t_oem_attachment_rel")
public class AttachmentRel extends BaseEntity {

    /**
    * 业务表单ID
    */
    @ApiModelProperty(value="业务表单ID")
    private Long businessFormId;

    /**
    * 附件ID t_sys_attachment.id
    */
    @ApiModelProperty(value="附件ID t_sys_attachment.id")
    private Long attachmentId;

    /**
    * 附件名称
    */
    @ApiModelProperty(value="附件名称")
    private String attachmentName;

    /**
    * 附件URL
    */
    @ApiModelProperty(value="附件URL")
    private String attachmentUrl;

    /**
    * 附件大小
    */
    @ApiModelProperty(value="附件大小")
    private Long attachmentSize;

    /**
    * 业务编码
    */
    @ApiModelProperty(value="业务编码")
    private String businessCode;

    /**
    * 业务名称
    */
    @ApiModelProperty(value="业务名称")
    private String businessName;

}