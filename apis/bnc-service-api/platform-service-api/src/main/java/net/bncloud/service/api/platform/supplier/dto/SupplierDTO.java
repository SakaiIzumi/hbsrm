package net.bncloud.service.api.platform.supplier.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@Getter
@Setter
@Accessors(chain = true)
public class SupplierDTO implements Serializable {
    private static final long serialVersionUID = 3297949418937375610L;

    @ApiModelProperty(value = "供应商编码")
    private String code;

    @ApiModelProperty(value = "供应商名称")
    private String name;

    @ApiModelProperty(value = "OA分类")
    private String oaType;

    @ApiModelProperty(value = "合作状态")
    private String relevanceStatus;

    @ApiModelProperty(value = "统一社会信用代码")
    private String creditCode;

}
