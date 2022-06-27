package net.bncloud.contract.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * @ClassName ContractStatistics
 * @Description: 合同数量统计实体类
 * @Author Administrator
 * @Date 2021/3/15
 * @Version V1.0
 **/
@Data
@Builder
public class ContractStatisticsVo {

    /**
     * 待确认的合同数量
     */
    @ApiModelProperty(value = "待确认的合同数量")
    private Integer toBeConfirmedNum;

    /**
     * 被拒的合同数量
     */
    @ApiModelProperty(value = "被拒的合同数量")
    private Integer rejectedNum;

    /**
     * 异常的合同数量
     */
    @ApiModelProperty(value = "异常的合同数量")
    private Integer anomalousNum;

    /**
     * 临近到期的合同数量
     */
    @ApiModelProperty(value = "临近到期的合同数量")
    private Integer nearExpiryNum;
}
