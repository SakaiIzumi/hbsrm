package net.bncloud.delivery.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author ddh
 * @version 1.0.0
 * @description 送货计划的状态数量统计视图
 * @since 2022/1/17
 */
@Data
@Accessors(chain = true)
public class DeliveryPlanStatisticsVo implements Serializable {

    /**
     * 待确认
     */
    @ApiModelProperty(value = "待确认")
    private Integer toBeConfirmNum;

    /**
     * 差异待确认数量
     */
    private Integer differenceToBeConfirmedNum;


}
