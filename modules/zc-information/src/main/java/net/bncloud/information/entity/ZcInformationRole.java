package net.bncloud.information.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.bncloud.base.BaseEntity;

/**
 * <p>
 * 消息路由表
 * </p>
 *
 * @author Auto-generator
 * @since 2021-04-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("zc_information_role")

public class ZcInformationRole extends BaseEntity {

    private static final long serialVersionUID = 1L;


    /**
     * 路由类型
     */
    @ApiModelProperty(value = "路由类型")
    private Long roleId;

    /**
     * 路由地址
     */
    @ApiModelProperty(value = "消息id")
    private String msgId;


    /**
     * 是否启动0启动1关闭
     */
    @ApiModelProperty(value = "是否启动0启动1关闭")
    private Integer disabled;

    /**
     * 对应tag_id
     */
    @ApiModelProperty(value = "对应tag_id")
    private Long tagId;

}


