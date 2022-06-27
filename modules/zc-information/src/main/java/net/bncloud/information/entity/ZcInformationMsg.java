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

import java.sql.Timestamp;
import java.time.OffsetDateTime;

/**
 * 智采消息表
 * @author dr
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("zc_information_msg")
public class ZcInformationMsg extends BaseEntity {
	private static final long serialVersionUID = 1L;

    /**
     * 对应zc_information_tag的tag
     */
    @ApiModelProperty(value = "对应zc_information_tag的tag")
    private String tag;

    /**
     * 调用方传的客户端ID，系统内调用无需传该值
     */
    @ApiModelProperty(value = "调用方传的客户端ID，系统内调用无需传该值")
    private String clientId;

    /**
     * 调用方传入的消息ID，若调用方不传该字段值，须根据zc_information_tag的msg_id_prefix+UUID生成该值，以便区分消息源头
     */
    @ApiModelProperty(value = "调用方传入的消息ID，若调用方不传该字段值，须根据zc_information_tag的msg_id_prefix+UUID生成该值，以便区分消息源头")
    private String msgId;

    /**
     * 入库时间
     */
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
    @JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
    @ApiModelProperty(value = "入库时间")
    private Timestamp addTime;

    /**
     * 消息内容JSON，取决于zc_information_tag的msg_template，最终给到前端使用
     */
    @ApiModelProperty(value = "消息内容JSON，取决于zc_information_tag的msg_template，最终给到前端使用")
    private String msg;

    /**
     * 消息内容JSON，取决于zc_information_tag的msg_template，最终给到前端使用
     */
    @ApiModelProperty(value = "消息内容msg_template与json结合，最终给到前端使用")
    private String msgValue;

    /**
     * 接收者uid
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "接收者uid")
    private Long getUid;

    /**
     * 接收者手机号
     */
    @ApiModelProperty(value = "接收者手机号")
    private String getMobile;

    /**
     * 接收者公司编码
     */
    @ApiModelProperty(value = "接收者公司编码")
    private String getSupplierNo;

    /**
     * 接收者公司编码
     */
    @ApiModelProperty(value = "接收者公司编码")
    private String getSupplierName;

    /**
     * 发送者或对应联系人uid
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "发送者或对应联系人uid")
    private Long sendUid;

    /**
     * 发送者或对应联系手机号
     */
    @ApiModelProperty(value = "发送者或对应联系手机号")
    private String sendMobile;

    /**
     * 发送者或对应联系公司编码
     */
    @ApiModelProperty(value = "发送者或对应联系公司编码")
    private String sendSupplierNo;

    /**
     * 发送者或对应联系公司名称
     */
    @ApiModelProperty(value = "发送者或对应联系公司名称")
    private String sendSupplierName;

    /**
     * 展示终端 0-所有 1-微信 2-钉钉
     */
    @ApiModelProperty(value = "展示终端 0-所有 1-微信 2-钉钉")
    private String terminalType;

    /**
     * 操作状态 0-待办 1-已办
     */

    @ApiModelProperty(value = "操作状态 0-待办 1-已办")
    private String operStatus;

    /**
     * 是否已读 0-未读 1-已读
     */
    @ApiModelProperty(value = "是否已读 0-未读 1-已读")
    private String isRead;

    /**
     * 消息类型 0-提醒类型 1-办理类型
     */
    @ApiModelProperty(value = "消息类型 0-提醒类型 1-办理类型")
    private String msgType;

    @ApiModelProperty(value = "发送人名字")
    private String sendName;

    @ApiModelProperty(value = "接收人名称")
    private String getName;

    @ApiModelProperty(value = "消息名称")
    private String msgTitle;

    @ApiModelProperty(value = "消息源数据Json")
    private String dataJson;

    @ApiModelProperty(value = "模板类型 字典modularType")
    private String moduleType;

//
    @ApiModelProperty(value = "系统类型 0-智采 1-智易")
    private String systemType;


    @ApiModelProperty(value = "发送方类型 org组织/sup供应商")
    private String sendType;

    @ApiModelProperty(value = "接收类型 org 组织 sup 供应商")
    private String receiverType;


    @ApiModelProperty(value = "发送主体(组织或供应商)")
    private String sendSubjectName;
    @ApiModelProperty(value = "发送主体编号(组织或供应商)")
    private String sendSubjectCode;
    @ApiModelProperty(value = "接收主体(组织或供应商)")
    private String receiverSubjectName;
    @ApiModelProperty(value = "接收主体编码(组织或供应商)")
    private String receiverSubjectCode;

    @ApiModelProperty(value = "业务id")
    private Long businessId;


}