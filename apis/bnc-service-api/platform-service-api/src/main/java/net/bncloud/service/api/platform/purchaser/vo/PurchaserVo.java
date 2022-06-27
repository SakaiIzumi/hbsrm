package net.bncloud.service.api.platform.purchaser.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * desc:
 *
 * @author Rao
 * @Date 2022/05/31
 **/
@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaserVo implements Serializable {
    private static final long serialVersionUID = -6282320403695759627L;

    @ApiModelProperty(value = "id")
    private Long id;
    @ApiModelProperty(value = "采购方编码")
    private String code;
    @ApiModelProperty(value = "采购方名字")
    private String name;
    @ApiModelProperty(value = "所属法人")
    private String artificialPerson;
    @ApiModelProperty(value = "描述")
    private String description;
    @ApiModelProperty(value = "状态")
    private Boolean enabled;
    @ApiModelProperty(value = "来源ID")
    private String sourceId;
    @ApiModelProperty(value = "来源编码")
    private String sourceCode;
    @ApiModelProperty(value = "公司id")
    private Long companyId;
    @ApiModelProperty(value = "公司编码")
    private String companyCode;
    @ApiModelProperty(value = "公司名称")
    private String companyName;

}
