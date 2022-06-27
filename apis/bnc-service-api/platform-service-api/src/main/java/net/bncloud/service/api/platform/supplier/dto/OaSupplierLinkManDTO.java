package net.bncloud.service.api.platform.supplier.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class OaSupplierLinkManDTO implements Serializable {
    private static final long serialVersionUID = 5126307696287406884L;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "职位")
    private String position;

    @ApiModelProperty(value = "手机")
    private String mobile;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "微信号")
    private String wechatId;

}
