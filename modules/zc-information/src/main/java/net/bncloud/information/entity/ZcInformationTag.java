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
 * 智采消息标签
 * @author dr
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("zc_information_tag")
public class ZcInformationTag extends BaseEntity {
	private static final long serialVersionUID = 1L;

    /**
     * 编号-UUID生成
     */
    @ApiModelProperty(value = "编号-UUID生成")
    private String tag;

    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String name;

    /**
     * 是否有效 0-无效 1-有效
     */
    @ApiModelProperty(value = "是否有效 0-无效 1-有效")
    private String status;

    /**
     * 权限，对应sys_role主键，如：11,12,13，当传入的消息无指定接收者的时候该字段生效，反之无效
     */
    @ApiModelProperty(value = "权限，对应sys_role主键，如：11,12,13，当传入的消息无指定接收者的时候该字段生效，反之无效")
    private String roles;

    /**
     * 消息ID前缀，当传入消息带有msgID的时候该字段无效，反之，则使用该字段拼接UUID生成msgID
     */
    @ApiModelProperty(value = "消息ID前缀，当传入消息带有msgID的时候该字段无效，反之，则使用该字段拼接UUID生成msgID")
    private String msgIdPrefix;

    /**
     * 来源，sys_config管理
     */
    @ApiModelProperty(value = "来源，sys_config管理")
    private String source;

    /**
     * 接收的终端类型，sys_config管理，如：1,2
     */
    @ApiModelProperty(value = "接收的终端类型，sys_config管理，如：1,2")
    private String terminalType;

    /**
     * 应用内跳转路由，如：1
     */
    @ApiModelProperty(value = "应用内跳转路由")
    private String prRoute;

    /**
     * 钉钉跳转路由
     */
    @ApiModelProperty(value = "钉钉跳转路由")
    private String ddRoute;

    /**
     * 微信公众号模板ID
     */
    @ApiModelProperty(value = "微信公众号模板ID")
    private String wxTemplate;

    /**
     * 模板策略JSON，如：{}，给到应用内前端的消息内容，尽可丰富，以便多终端使用
     */
    @ApiModelProperty(value = "详细参数模板策略JSON，如：{}，给到应用内前端的消息内容，尽可丰富，以便多终端使用")
    private String msgTemplate;
    /**
     * 模板策略JSON，如：{}，给到应用内前端的消息内容，尽可丰富，以便多终端使用
     */
    @ApiModelProperty(value = "简略模板")
    private String messageTemplate;


    /**
     * 消息类型 0-提醒类型 1-办理类型
     */
    @ApiModelProperty(value = "消息类型 0-提醒类型 1-办理类型")
    private String msgType;

    @ApiModelProperty(value = "系统类型 0-智采 1-智易")
    private String systemType;

    @ApiModelProperty(value = "模板类型 字典modularType")
    private String moduleType;

    @ApiModelProperty(value = "发送方类型 org组织/sup供应商")
    private String sendType;

    @ApiModelProperty(value = "接收类型 org 组织 sup 供应商")
    private String receiverType;
}
