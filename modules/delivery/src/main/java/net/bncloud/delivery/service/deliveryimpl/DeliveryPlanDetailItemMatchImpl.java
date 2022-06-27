package net.bncloud.delivery.service.deliveryimpl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.common.exception.Asserts;
import net.bncloud.delivery.entity.DeliveryDetail;
import net.bncloud.delivery.entity.DeliveryNote;
import net.bncloud.delivery.entity.DeliveryNoteExcelImportDetailVo;
import net.bncloud.delivery.entity.DeliveryPlanDetailItem;
import net.bncloud.delivery.service.DeliveryDetailService;
import net.bncloud.delivery.service.DeliveryPlanDetailItemMatcher;
import net.bncloud.delivery.service.DeliveryPlanDetailItemService;
import net.bncloud.delivery.service.DeliveryPlanDetailService;
import net.bncloud.delivery.vo.ShippableDeliveryPlanDetailItemVo;
import org.springframework.context.annotation.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * desc:
 *
 * @author Rao
 * @Date 2022/03/11
 **/
@Slf4j
@Service
public class DeliveryPlanDetailItemMatchImpl implements DeliveryPlanDetailItemMatcher {

    @Autowired
    private DeliveryPlanDetailItemService deliveryPlanDetailItemService;

    @Lazy
    @Resource
    private DeliveryDetailService deliveryDetailService;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void matchDeliveryDetail(DeliveryNoteExcelImportDetailVo deliveryNoteExcelImportDetailVo, DeliveryNote deliveryNote, List<ShippableDeliveryPlanDetailItemVo> shippableDeliveryPlanDetailItemVos) {

        // 1、生成送货明细 一条项次生成一条明细
        log.info("[matchDeliveryDetail] params info >> deliveryNoteExcelImportDetailVo:{},deliveryNote:{},shippableDeliveryPlanDetailItemVos:{}", JSON.toJSONString(deliveryNoteExcelImportDetailVo),JSON.toJSONString(deliveryNote),JSON.toJSONString(shippableDeliveryPlanDetailItemVos) );

        // excel 填写的实际发货数量
        Long realDeliveryQuantityTotal = deliveryNoteExcelImportDetailVo.getRealDeliveryQuantity();

        // 要保存的送货明细
        List<DeliveryDetail> willSaveDeliveryDetailList = new ArrayList<>();

        for (ShippableDeliveryPlanDetailItemVo shippableDeliveryPlanDetailItemVo : shippableDeliveryPlanDetailItemVos) {
            // 当前项次的剩余可发货数量
            long remainingQuantity = Long.parseLong( shippableDeliveryPlanDetailItemVo.getRemainingQuantity() );

            // 要更新的送货项次
            DeliveryPlanDetailItem deliveryPlanDetailItemUpdate = new DeliveryPlanDetailItem();
            deliveryPlanDetailItemUpdate.setId( shippableDeliveryPlanDetailItemVo.getId() );

            // 说明这个项次还有剩余发送数量
            deliveryPlanDetailItemUpdate.setRemainingQuantity( "0" );

            // 实际发货数量
            long realDeliveryQuantity = remainingQuantity;
            if( remainingQuantity > realDeliveryQuantityTotal ){
                realDeliveryQuantity = realDeliveryQuantityTotal;
                deliveryPlanDetailItemUpdate.setRemainingQuantity( (remainingQuantity - realDeliveryQuantityTotal)+"" );
            }
            // 更新项次的可剩余发货数量
            deliveryPlanDetailItemService.updateById( deliveryPlanDetailItemUpdate );
            DeliveryDetail deliveryDetail = this.buildDeliveryDetailRecord( deliveryNoteExcelImportDetailVo,deliveryNote, shippableDeliveryPlanDetailItemVo, realDeliveryQuantity);

            willSaveDeliveryDetailList.add( deliveryDetail );

            // 生成一条明细减一条
            realDeliveryQuantityTotal -= remainingQuantity;
            if( realDeliveryQuantityTotal <= 0){
                // 循环结束
                break;
            }
        }

        // 2、超出计算  把多出的平均分配到每个明细
        this.allocateProportionedNumber(realDeliveryQuantityTotal, willSaveDeliveryDetailList);

        Asserts.isTrue( CollectionUtil.isNotEmpty( willSaveDeliveryDetailList ),"需要保存的送货明细内容是空的,送货单ID:"+ deliveryNote.getId() );

        log.info("[matchDeliveryDetail] >> will update size :{}",willSaveDeliveryDetailList.size());

        // 3、 送货单明细保存
        deliveryDetailService.saveBatch( willSaveDeliveryDetailList );

    }

    /**
     * 按项次分  分配  分钱算法
     * @param excessValueTotal
     * @param willSaveDeliveryDetailList
     */
    private void allocateProportionedNumber(Long excessValueTotal, List<DeliveryDetail> willSaveDeliveryDetailList) {
        if( excessValueTotal > 0){

            for (long i = 1; i <= excessValueTotal;) {
                // 已分配的项次
                Set<Long> assignedDeliveryPlanDetailItemSet = new HashSet<>();

                for (DeliveryDetail deliveryDetail : willSaveDeliveryDetailList) {
                    Long deliveryPlanDetailItemId = deliveryDetail.getDeliveryPlanDetailItemId();

                    if (! assignedDeliveryPlanDetailItemSet.contains( deliveryPlanDetailItemId )) {
                        assignedDeliveryPlanDetailItemSet.add( deliveryPlanDetailItemId );
                        deliveryDetail.setRealDeliveryQuantity( deliveryDetail.getRealDeliveryQuantity().add( new BigDecimal("1")) );
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

    /**
     * 多个行一起 匹配  同产品 但批次不同
     *
     * @param deliveryNoteExcelImportDetailVoList
     * @param deliveryNote
     * @param shippableDeliveryPlanDetailItemVos
     */
    @Override
    public void matchDeliveryDetailWithDeliveryNoteExcelImportDetailVoList(List<DeliveryNoteExcelImportDetailVo> deliveryNoteExcelImportDetailVoList, DeliveryNote deliveryNote, List<ShippableDeliveryPlanDetailItemVo> shippableDeliveryPlanDetailItemVos) {

        // 要保存的送货明细
        List<DeliveryDetail> willSaveDeliveryDetailList = new ArrayList<>();

        // 对每一行的excel数据进行处理
        for (DeliveryNoteExcelImportDetailVo deliveryNoteExcelImportDetailVo : deliveryNoteExcelImportDetailVoList) {

            // excel 填写的实际发货数量
            Long realDeliveryQuantityTotal = deliveryNoteExcelImportDetailVo.getRealDeliveryQuantity();

            for (ShippableDeliveryPlanDetailItemVo shippableDeliveryPlanDetailItemVo : shippableDeliveryPlanDetailItemVos) {

                // 当前项次的剩余可发货数量
                long remainingQuantity = Long.parseLong( shippableDeliveryPlanDetailItemVo.getRemainingQuantity() );
                if( remainingQuantity == 0){
                    // 说明当前项次是被消耗了的
                    continue;
                }

                // 要更新的送货项次
                DeliveryPlanDetailItem deliveryPlanDetailItemUpdate = new DeliveryPlanDetailItem();
                deliveryPlanDetailItemUpdate.setId( shippableDeliveryPlanDetailItemVo.getId() );

                // 说明这个项次还有剩余发送数量
                deliveryPlanDetailItemUpdate.setRemainingQuantity( "0" );

                // 实际发货数量
                long realDeliveryQuantity = remainingQuantity;

                // 说明项次 没有被消耗完
                if( remainingQuantity >= realDeliveryQuantityTotal ){
                    realDeliveryQuantity = realDeliveryQuantityTotal;
                    // 减去消耗的
                    shippableDeliveryPlanDetailItemVo.setRemainingQuantity( (remainingQuantity - realDeliveryQuantityTotal)+"" );
                    deliveryPlanDetailItemUpdate.setRemainingQuantity( (remainingQuantity - realDeliveryQuantityTotal)+"" );
                }
                // 要把消耗了的项次移除掉
                else {
                    shippableDeliveryPlanDetailItemVo.setRemainingQuantity( "0");

                    int excessValue = shippableDeliveryPlanDetailItemVo.getExcessValue();
                    if( excessValue > 0){
                        // 表示超送的还需要扣减
                        realDeliveryQuantityTotal -= excessValue;
                        // 当前送货明细送了多少
                        realDeliveryQuantity += excessValue;
                    }

                }

                // 更新项次的可剩余发货数量
                deliveryPlanDetailItemService.updateById( deliveryPlanDetailItemUpdate );

                DeliveryDetail deliveryDetail = this.buildDeliveryDetailRecord( deliveryNoteExcelImportDetailVo,deliveryNote, shippableDeliveryPlanDetailItemVo, realDeliveryQuantity);
                willSaveDeliveryDetailList.add( deliveryDetail );

                // 生成一条明细减一条
                realDeliveryQuantityTotal -= remainingQuantity;
                if( realDeliveryQuantityTotal <= 0){
                    // 循环结束
                    break;
                }

            }

        }


        Asserts.isTrue( CollectionUtil.isNotEmpty( willSaveDeliveryDetailList ),"需要保存的送货明细内容是空的,送货单ID:"+ deliveryNote.getId() );

        log.info("[matchDeliveryDetail] >> will update size :{}",willSaveDeliveryDetailList.size());

        // 3、 送货单明细保存
        deliveryDetailService.saveBatch( willSaveDeliveryDetailList );

    }

    private DeliveryDetail buildDeliveryDetailRecord(DeliveryNoteExcelImportDetailVo deliveryNoteExcelImportDetailVo, DeliveryNote deliveryNote, ShippableDeliveryPlanDetailItemVo shippableDeliveryPlanDetailItemVo, long realDeliveryQuantity) {
        // 新建送货明细
        DeliveryDetail deliveryDetail = new DeliveryDetail();
        deliveryDetail.setDeliveryId( deliveryNote.getId() );

        deliveryDetail.setItemNo( 0);
        deliveryDetail.setPlanNo( shippableDeliveryPlanDetailItemVo.getPlanNo() );
        deliveryDetail.setPlanDetailItemSourceId( shippableDeliveryPlanDetailItemVo.getPlanDetailItemSourceId()  );
        deliveryDetail.setPurchaseOrderCode( shippableDeliveryPlanDetailItemVo.getPurchaseOrderCode() );
        deliveryDetail.setProductCode( shippableDeliveryPlanDetailItemVo.getProductCode() );
        deliveryDetail.setProductName( shippableDeliveryPlanDetailItemVo.getProductName());
        deliveryDetail.setProductSpecs( shippableDeliveryPlanDetailItemVo.getProductSpecifications() );
        // 计划数量
        deliveryDetail.setPlanQuantity( new BigDecimal( shippableDeliveryPlanDetailItemVo.getPlanQuantity() ) );
        // 实际送货数量 要发多少
        deliveryDetail.setRealDeliveryQuantity( new BigDecimal( realDeliveryQuantity) );
        deliveryDetail.setBarCode( deliveryNoteExcelImportDetailVo.getMerchantCode() );
        deliveryDetail.setBatchNo( deliveryNoteExcelImportDetailVo.getItemNo() );
        deliveryDetail.setDeliveryUnitCode( shippableDeliveryPlanDetailItemVo.getPlanUnit() );
        // 计划单位名称 ?
        deliveryDetail.setDeliveryUnitName( shippableDeliveryPlanDetailItemVo.getPlanUnit() );
//        deliveryDetail.setReceiptQuantity( new BigDecimal("0"));
        deliveryDetail.setWarehouse( deliveryNoteExcelImportDetailVo.getWarehouse() );
        deliveryDetail.setRemark( deliveryNoteExcelImportDetailVo.getRemark() );
        deliveryDetail.setPlanUnit( deliveryNote.getPlanUnit() );
        deliveryDetail.setMaterialClassification( deliveryNoteExcelImportDetailVo.getMaterialType() );
        deliveryDetail.setCurrency( shippableDeliveryPlanDetailItemVo.getCurrency() );
        deliveryDetail.setDeliveryPlanDetailItemId( shippableDeliveryPlanDetailItemVo.getId() );
        deliveryDetail.setBillNo( shippableDeliveryPlanDetailItemVo.getBillNo() );
        deliveryDetail.setOrderType( deliveryNoteExcelImportDetailVo.getOrderType() );
        deliveryDetail.setProductUnitPrice( shippableDeliveryPlanDetailItemVo.getProductUnitPrice() );
        deliveryDetail.setTaxUnitPrice( shippableDeliveryPlanDetailItemVo.getTaxUnitPrice() );
        deliveryDetail.setRemainingQuantity( new BigDecimal("0"));
        return deliveryDetail;
    }
}
