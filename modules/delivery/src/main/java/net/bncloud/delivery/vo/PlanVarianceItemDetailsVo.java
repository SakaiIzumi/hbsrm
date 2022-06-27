package net.bncloud.delivery.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author ddh
 * @description 计划差异项详情
 * @since 2022/6/13
 */
@Data
@Accessors(chain = true)
public class PlanVarianceItemDetailsVo implements Serializable {

    /**
     * 日期
     */
    private LocalDate deliveryDate;

    /**
     * 计划数量
     */
    @ApiModelProperty(value = "送货数量")
    private String deliveryQuantity;

    /**
     * 确认数量
     */
    @ApiModelProperty(value = "确认数量")
    private String confirmQuantity;

    /**
     * 差异数
     */
    private String varianceNumber;

    /**
     * 差异原因
     */
    private String differenceReason;
}
