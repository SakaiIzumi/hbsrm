package net.bncloud.information.vo;


import io.swagger.annotations.ApiModelProperty;
import net.bncloud.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.bncloud.information.entity.ZcInformationRoute;

import java.io.Serializable;

/**
 * <p>
 * 消息路由表
 * </p>
 *
 * @author Auto-generator
 * @since 2021-04-21
 */
@Data
public class ZcInformationRouteVo extends ZcInformationRoute implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 第三方模板ID
     */
    @ApiModelProperty(value = "路由参数")
    private String parameter;

}
