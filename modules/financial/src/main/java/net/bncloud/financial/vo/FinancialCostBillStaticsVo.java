package net.bncloud.financial.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * 客户费用单统计数量实体类
 *
 * @author ddh
 * @version 1.0.0
 * @date 2021/12/16
 */
@Data
@Builder
public class FinancialCostBillStaticsVo {

    @ApiModelProperty("待发送的费用单数量")
    private Integer toBeSentNum;
    @ApiModelProperty("待确认的费用单数量")
    private Integer toBeConfirmedNum;
    @ApiModelProperty("被退回的费用单数量")
    private Integer toBeReturnNum;
}
