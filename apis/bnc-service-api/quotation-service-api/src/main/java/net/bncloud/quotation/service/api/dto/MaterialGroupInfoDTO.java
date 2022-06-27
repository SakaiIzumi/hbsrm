package net.bncloud.quotation.service.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author lijiaju
 * @date 2022/2/24 18:38
 */
@Data
@ToString
@Accessors(chain = true)
public class MaterialGroupInfoDTO {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty("ERP组ID")
    private Long erpId;
    @ApiModelProperty("同步ERP暂未知字段用意")
    private String erpLeft;
    @ApiModelProperty("ERP组父ID")
    private Long erpParentId;
    @ApiModelProperty("同步ERP暂未知字段用意")
    private String erpNullParentId;
    @ApiModelProperty("编码")
    private String erpNumber;
    @ApiModelProperty("分组ID")
    private String erpGroupId;
    @ApiModelProperty("同步ERP暂未知字段用意")
    private String erpRight;
    @ApiModelProperty("分组名称")
    private String erpName;
}
