package net.bncloud.delivery.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import net.bncloud.delivery.entity.DeliveryPlanBoard;

import java.io.Serializable;
import java.util.List;

/**
 * @author ddh
 * @version 1.0.0
 * @description 送货计划看板信息表
 * @since 2022/1/17
 */
@Data
@Accessors(chain = true)
public class DeliveryPlanBoardVo implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 总的产品数
     */
    @ApiModelProperty(value = "总的产品数")
    private Integer totalProductNum;

    /**
     * 一周中的第几天
     */
    @ApiModelProperty(value = "一周中的第几天")
    private String weekNum;

    /**
     * 日期
     */
    @ApiModelProperty(value = "日期")
    private String date;

    /**
     * 送货计划看板列表
     */
    @ApiModelProperty(value = "送货计划看板列表")
    private List<DeliveryPlanBoard> deliveryPlanBoardList;
}
