package net.bncloud.common.service.base.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.bncloud.common.util.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;

//调用消息中心参数类（弃用） dr
@Data
@Accessors(chain = true)
public class InformationParam<T> implements Serializable {
    @NotBlank
    private String tag;//对应zc_information_tag的tag

    @NotBlank
    private String clientId;//调用方传的客户端ID，微服务间调用建议调用方在配置文件里配置上对应clientId，以区分消息发送源，如区分智采，智易
    private String msgId;//调用方传入的消息ID，若调用方不传该字段值，须根据zc_information_tag的msg_id_prefix+UUID生成该值，以便区分消息源头

    private Long getUid;//接收者uid（若不传，则按消息标签配置角色发送，若传输，则只发送该用户）
    @NotBlank
    private String getSupplierNo;//接收者对应公司编码

    private Long sendUid;//发送者或对应联系人uid
    private String sendSupplierNo;//发送者对应公司编码

    private String msgTitle;//消息名称-若不传则前端无该显示值

    @NotBlank
    private T dataJson;//消息源数据
}