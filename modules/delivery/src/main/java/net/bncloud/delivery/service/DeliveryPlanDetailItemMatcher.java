package net.bncloud.delivery.service;

import net.bncloud.delivery.entity.DeliveryNote;
import net.bncloud.delivery.entity.DeliveryNoteExcelImportDetailVo;
import net.bncloud.delivery.vo.ShippableDeliveryPlanDetailItemVo;
import net.bncloud.delivery.vo.ShippableDeliveryPlanDetailVo;

import java.util.List;

/**
 * desc: 送货计划明细项次匹配策略
 *
 * @author Rao
 * @Date 2022/03/11
 **/
public interface DeliveryPlanDetailItemMatcher {
    /**
     * 匹配送货明细
     * @param deliveryNoteExcelImportDetailVo
     * @param deliveryNote
     * @param shippableDeliveryPlanDetailItemVos
     */
    void matchDeliveryDetail(DeliveryNoteExcelImportDetailVo deliveryNoteExcelImportDetailVo, DeliveryNote deliveryNote, List<ShippableDeliveryPlanDetailItemVo> shippableDeliveryPlanDetailItemVos);

    /**
     * 多记录行匹配送货明细
     * @param deliveryNoteExcelImportDetailVoList
     * @param deliveryNote
     * @param shippableDeliveryPlanDetailItemVos
     */
    void matchDeliveryDetailWithDeliveryNoteExcelImportDetailVoList( List<DeliveryNoteExcelImportDetailVo> deliveryNoteExcelImportDetailVoList, DeliveryNote deliveryNote, List<ShippableDeliveryPlanDetailItemVo> shippableDeliveryPlanDetailItemVos);

}
