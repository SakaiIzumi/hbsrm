package net.bncloud.delivery.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName DeliveryStatisticsVo
 * @Description: 送货单数量统计实体类
 * @Author Administrator
 * @Date 2021/3/18
 * @Version V1.0
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryStatisticsVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 申请退回的数量
     */
    @ApiModelProperty(value = "申请退回的数量")
    private Integer applySendBack;

    /**
     * 送货退回的数量
     */
    @ApiModelProperty(value = "送货退回的数量")
    private Integer deliverySendBack;


    /**
     * 待签收的数量
     */
    @ApiModelProperty(value = "代签收的数量")
    private Integer toBeSignedNum;

    /**
     * 申请待批准的数量
     */
    @ApiModelProperty(value = "申请待批准的数量")
    private Integer applicationPendingNum;

    /**
     *待ERP签收数量
     */
    @ApiModelProperty(value = "待ERP签收数量")
    private Integer toBeSignedByERPNum;



}
