package net.bncloud.bis.srm.financial.enums;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author: liulu
 * @Date: 2022-02-22 10:04
 */
public enum SettlementPoolDocumentTypeEnum {
    @ApiModelProperty("标准应付单")
    YFD01_SYS,
    @ApiModelProperty("费用应付单")
    YFD02_SYS
    ;
}
