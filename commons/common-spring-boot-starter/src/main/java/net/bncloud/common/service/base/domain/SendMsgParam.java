package net.bncloud.common.service.base.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

//调用消息中心参数类 dr
@Data
@Accessors(chain = true)
public class SendMsgParam<T> implements Serializable {
    @NotBlank
    private String tag;//模板ID（对应zc_information_tag的tag）

    @NotBlank
    private List<Long> receives;//接收者uid集合（若不传，则按消息标签配置角色发送，若传输，则只发送该用户）

    private Long sendUid;//发送者或对应联系人uid

    @NotBlank
    private T dataJson;//消息源数据

//    @NotBlank
    private String systemType;//系统类型 0-智采 1-智易

    @NotBlank
    private String sendType;//发送类型 org 组织 sup 供应商
    @NotBlank
    private String receiverType;//接收类型 org 组织 sup 供应商
    @NotBlank
    private String module;//模板类型

    @NotBlank
    private String sources;//消息来源

    @NotBlank
    private String sourcesCode;//消息来源id编号

    @NotBlank
    private String receiver;//消息来源

    @NotBlank
    private String receiverCode;//消息来源id编号




    @NotBlank
    private List<Long> roles;//角色的ids

    @NotBlank
    private String sendUserName;//发送者username

    private List<Map<String,Object>> userInfos;//发短信的用户信息


    private String smsParams;

    private String smsTempCode;

    private Integer smsMsgType;

}