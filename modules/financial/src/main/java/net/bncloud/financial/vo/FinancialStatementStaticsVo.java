package net.bncloud.financial.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author ddh
 * @version 1.0.0
 * @description 对账单状态数量统计信息
 * @since 2022/1/6
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "对账单状态数量统计")
public class FinancialStatementStaticsVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("待发送的对账单数量")
    private Integer toBeSentNum;
    @ApiModelProperty("待确认的对账单数量")
    private Integer toBeConfirmedNum;
    @ApiModelProperty("已确认的对账单数量")
    private Integer confirmedNum;


}
