package net.bncloud.delivery.utils;

import net.bncloud.common.exception.Asserts;
import net.bncloud.delivery.entity.DeliveryDetail;
import net.bncloud.delivery.vo.ShippableDeliveryPlanDetailItemVo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * desc: 送货明细工具
 *
 * @author Rao
 * @Date 2022/03/18
 **/
public abstract class DeliveryDetailUtils {

    /**
     * 验证剩余可发货数量是否符合要求
     * @param remainingQuantityTotal 剩余可发货数量
     * @param ratioOfSentProportion 百分比
     * @param realDeliveryQuantity 填写的实际发货数量
     */
    public static void verifyRemainingShippableQuantityAccord(int remainingQuantityTotal, BigDecimal ratioOfSentProportion,Long realDeliveryQuantity){
        DeliveryDetailUtils.verifyRemainingShippableQuantityAccord( remainingQuantityTotal,ratioOfSentProportion,realDeliveryQuantity,"剩余可发数量不足！" );
    }

    /**
     * 验证剩余可发货数量是否符合要求
     * @param remainingQuantityTotal 剩余可发货数量
     * @param ratioOfSentProportion 百分比
     * @param realDeliveryQuantity 填写的实际发货数量
     */
    public static void verifyRemainingShippableQuantityAccord(int remainingQuantityTotal, BigDecimal ratioOfSentProportion,Long realDeliveryQuantity,String msg){
        int excessValue = ratioOfSentProportion.multiply( new BigDecimal( remainingQuantityTotal+"")).divide( new BigDecimal("100"), RoundingMode.DOWN ).intValue();
        //数据库剩余可发数量 + 超额值 >= 实际送货数量
        Asserts.isTrue( remainingQuantityTotal + excessValue >= realDeliveryQuantity ,msg );
    }

    /**
     * 项次需要超额发送
     * @param remainingQuantityTotal 剩余可发货数量
     * @param ratioOfSentProportion 百分比
     * @param realDeliveryQuantity 填写的实际发货数量
     * @return 总的超额数  -1 表示无
     */
    public static int remainingQuantityNeedToOverSend(int remainingQuantityTotal, BigDecimal ratioOfSentProportion, Long realDeliveryQuantity){
        int excessValue = ratioOfSentProportion.multiply( new BigDecimal( remainingQuantityTotal+"")).divide( new BigDecimal("100"), RoundingMode.DOWN ).intValue();
        if( realDeliveryQuantity <= remainingQuantityTotal){
            return -1;
        }
        //超额值 >= 实际送货数量 - 数据库剩余可发数量
        boolean canRemainingQuantityNeedToOverSend = excessValue >= realDeliveryQuantity - remainingQuantityTotal;
        return canRemainingQuantityNeedToOverSend ? (int) (realDeliveryQuantity - remainingQuantityTotal) : -1;
    }

    /**
     * 分配 超出总值 给每个项次
     * @param excessValueTotal
     * @param shippableDeliveryPlanDetailItemVos
     */
    public static void allocateExcessValueTotalToDeliveryPlanDetailItemList(int excessValueTotal, List<ShippableDeliveryPlanDetailItemVo> shippableDeliveryPlanDetailItemVos){
        if( excessValueTotal > 0){

            for (long i = 1; i <= excessValueTotal;) {
                for (ShippableDeliveryPlanDetailItemVo shippableDeliveryPlanDetailItemVo : shippableDeliveryPlanDetailItemVos) {
                    // 添加 值
                    shippableDeliveryPlanDetailItemVo.addExcessValue( 1 );
                    // 分数往后走
                    i++;
                    if( i > excessValueTotal){
                        break;
                    }

                }
            }
        }
    }


}
