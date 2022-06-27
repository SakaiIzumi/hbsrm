package net.bncloud.common.service.base.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

//调用消息中心参数类 dr
@Data
@Accessors(chain = true)
public class HandlerMsgParam implements Serializable {


    @NotBlank
    private Long businessId;//模板类型 枚举 ModularType

    @NotBlank
    private String eventCode;//事件类型

    @NotBlank
    private String receiverType;//接收主体
}