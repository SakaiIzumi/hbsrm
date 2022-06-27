package net.bncloud.information.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import net.bncloud.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import net.bncloud.common.util.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.OffsetDateTime;

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
@TableName("zc_information_route")

public class ZcInformationRoute extends BaseEntity {

    private static final long serialVersionUID = 1L;


    /**
     * 路由类型
     */
    @ApiModelProperty(value = "路由类型")
    private String routeType;

    /**
     * 路由地址
     */
    @ApiModelProperty(value = "路由地址")
    private String routeUrl;

    /**
     * 第三方模板ID
     */
    @ApiModelProperty(value = "第三方模板ID")
    private String templateId;


    /**
     * 第三方模板ID
     */
    @ApiModelProperty(value = "路由参数")
    private String parameterTemplate;
    /**
     * 是否启动0启动1关闭
     */
    @ApiModelProperty(value = "是否启动0启动1关闭")
    private Boolean disabled;

    /**
     * 对应tag_id
     */
    @ApiModelProperty(value = "对应tag_id")
    private Long tagId;

}


