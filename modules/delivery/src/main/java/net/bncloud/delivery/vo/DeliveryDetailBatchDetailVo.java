package net.bncloud.delivery.vo;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * desc: 批次详情返回对象
 *
 * @author Rao
 * @Date 2022/03/30
 **/
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Data
public class DeliveryDetailBatchDetailVo extends DeliveryPlanDetailItemVo implements Serializable {
    private static final long serialVersionUID = -4127265045483817435L;

    /**
     * 送货明细
     */
    private List<DeliveryDetailVo> deliveryDetailList;

    /**
     * 添加剩余可发货数量
     */
    public void addRemainingQuantity( BigDecimal quantity){
        String remainingQuantity = this.getRemainingQuantity();
        BigDecimal remainingQuantityBigDecimal = StrUtil.isNotBlank(remainingQuantity) ? new BigDecimal(remainingQuantity) : new BigDecimal("0");
        BigDecimal add = remainingQuantityBigDecimal.add(quantity);
        this.setRemainingQuantity( add.toString() );
    }

}
