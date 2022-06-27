package net.bncloud.saas.purchaser.service.command;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import net.bncloud.saas.purchaser.domain.Purchaser;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@Builder
@ApiModel(value = "创建采购方")
public class UpdatePurchaserCommand {
    @ApiModelProperty(value = "采购方id")
    @NotNull(message = "采购方id不能为空")
    private Long id;
    @NotNull(message = "采购方编码不能为空")
    private String code;
    @ApiModelProperty(value = "采购方名")
    @NotNull(message = "采购方名不能为空")
    private String name;
    @ApiModelProperty(value = "所属法人")
    @NotNull(message = "所属法人不能为空")
    private String artificialPerson;
    @ApiModelProperty(value = "描述")
    private String description;
    @ApiModelProperty(value = "状态")
    private Boolean enabled;
}
