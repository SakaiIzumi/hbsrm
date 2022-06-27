package net.bncloud.information.vo;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.bncloud.base.BaseEntity;

@Data
public class ZcInformationRouteParamVo {

    private static final long serialVersionUID = 1L;


    /**
     * 路由类型
     */
    @ApiModelProperty(value = "路由类型")
    private String routeType;

    /**
     * 路由地址
     */
    @ApiModelProperty(value = "消息对应的ID")
    private Long msgId;

    /**
     * 第三方模板ID
     */
    @ApiModelProperty(value = "路由参数")
    private JSONObject parameter;

    /**
     * 对应tag_id
     */
    @ApiModelProperty(value = "对应tag_id")
    private Long tagId;
    /**
     * 对应tag_id
     */
    @ApiModelProperty(value = "路由名称")
    private String routeName;

    @ApiModelProperty(value = "路由名称")
    private String routeURL;

}


