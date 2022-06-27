package net.bncloud.information.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.bncloud.base.BaseEntity;
import net.bncloud.common.util.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;

/**
 * 智采消息表
 * @author dr
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("zc_information_sms")
public class ZcInformationSMS extends BaseEntity {
	private static final long serialVersionUID = 1L;

    /**
     * 对应zc_information_tag的tag
     */
    @ApiModelProperty(value = "对应zc_information_tag的tag")
    private String tag;


    /**
     * 入库时间
     */
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
    @JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
    @ApiModelProperty(value = "入库时间")
    private Timestamp addTime;


    /**
     * 接收者uid
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "接收者uid")
    private Long getUid;

    /**
     * 接收人名称
     */
    @ApiModelProperty(value = "接收人名称")
    private String getName;

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
     * 发送人名字
     */
    @ApiModelProperty(value = "发送人名字")
    private String sendName;

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



    @ApiModelProperty(value = "消息名称")
    private String msgTitle;

    @ApiModelProperty(value = "模板类型 字典modularType")
    private String moduleType;

//
    @ApiModelProperty(value = "系统类型 0-智采 1-智易")
    private String systemType;

    @ApiModelProperty(value = "业务id")
    private String businessId;

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

    private String sendTemp;

    private String sendData;

    private Integer sendStatus;

    private String responsMsg;

    private Integer sendMsgType;

}