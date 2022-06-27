package net.bncloud.bis.manager;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.bis.constant.BisSyncConstants;
import net.bncloud.bis.model.entity.ExperimentSupplier;
import net.bncloud.bis.model.erp.PurchaseInStockOrder;
import net.bncloud.bis.service.api.vo.PurchaseInStockOrderCreateVo;
import net.bncloud.common.api.R;
import net.bncloud.msk3cloud.constant.formid.SupplierChainConstants;
import net.bncloud.msk3cloud.core.condition.QueryCondition;
import net.bncloud.msk3cloud.core.condition.SaveCondition;
import net.bncloud.msk3cloud.core.params.K3CloudBatchSaveParam;
import net.bncloud.msk3cloud.core.params.K3CloudSaveParam;
import net.bncloud.msk3cloud.kingdee.K3cloudRemoteService;
import net.bncloud.msk3cloud.kingdee.entity.common.Number;
import net.bncloud.msk3cloud.util.FieldKeyAnoUtils;
import net.bncloud.service.api.delivery.dto.DeliveryDetailUpdateDTO;
import net.bncloud.service.api.delivery.feign.DeliveryPlanFeignClient;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * desc: 采购入库单
 *
 * @author Rao
 * @Date 2022/01/21
 **/
@Component
@Slf4j
public class PurchaseInStockOrderManager {

    @Autowired
    private SegmentedQueryManager segmentedQueryManager;
    @Resource
    private DeliveryPlanFeignClient deliveryPlanFeignClient;
    @Autowired
    private K3cloudRemoteService k3cloudRemoteService;
    @Autowired
    private RedissonClient redissonClient;

    /**
     * 同步采购入库单数据
     */
    public void syncData() {

        Date startDate = new Date();
        Set<Long> allIdSet = this.getAllNeedSyncInStockOrderId(null, BisSyncConstants.TASK_SYNC_DATA_TIME_MAP_KEY);

        if (allIdSet.isEmpty()) {
            log.warn("[PurchaseInStockOrder] syncData is empty! ");
            return;
        }

        this.handleSync(allIdSet, purchaseInStockOrderVos -> {

            List<DeliveryDetailUpdateDTO> deliveryDetailUpdateDtoList = getDeliveryDetailUpdateDtoList(purchaseInStockOrderVos);

            // 更新到下游服务起
            return deliveryPlanFeignClient.syncErpDeliveryNoteInfos(deliveryDetailUpdateDtoList);

        }, () -> {
            RMap<String, Date> syncDataTimeMap = redissonClient.getMap(BisSyncConstants.TASK_SYNC_DATA_TIME_MAP_KEY, JsonJacksonCodec.INSTANCE);
            syncDataTimeMap.fastPut(BisSyncConstants.PURCHASE_IN_STOCK_ORDER_SYNC_ERP_TASK, startDate);
        });

    }

    /**
     * 转换
     *
     * @param purchaseInStockOrderVos
     * @return
     */
    private List<DeliveryDetailUpdateDTO> getDeliveryDetailUpdateDtoList(List<net.bncloud.bis.model.vo.PurchaseInStockOrderVo> purchaseInStockOrderVos) {
        return purchaseInStockOrderVos.stream().map(purchaseInStockOrderVo -> {
            DeliveryDetailUpdateDTO deliveryDetailUpdateDto = new DeliveryDetailUpdateDTO();
            deliveryDetailUpdateDto.setErpId(purchaseInStockOrderVo.getFInStockEntryLinkFSId());
            Integer fRealQty = Optional.ofNullable(purchaseInStockOrderVo.getFRealQty()).orElse(-1);
            deliveryDetailUpdateDto.setReceiptQuantity(new BigDecimal(fRealQty));
            try {
                LocalDateTime localDateTime = LocalDateTime.parse(purchaseInStockOrderVo.getFDate());
                deliveryDetailUpdateDto.setWarehouseDate(new Date(localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()));
            } catch (Exception ex) {
                // 报警
                log.error("[PurchaseInStockOrder] syncData parse warehouseDate error! ", ex);
            }
            //入库单号
            deliveryDetailUpdateDto.setReceiptNo(purchaseInStockOrderVo.getFBillNo());
            return deliveryDetailUpdateDto;
        }).collect(Collectors.toList());
    }

    /**
     * 处理同步
     */
    private void handleSync(Set<Long> allIdSet, Function<List<net.bncloud.bis.model.vo.PurchaseInStockOrderVo>, R> func, Runnable updateTimeRun) {

        List<List<Long>> allIdSplitList = CollUtil.split(allIdSet, 100);

        List<String> fieldKeyList = FieldKeyAnoUtils.getFieldKeyList(net.bncloud.bis.model.vo.PurchaseInStockOrderVo.class);

        allIdSplitList.forEach(idList -> {
            try {
                QueryCondition condition = QueryCondition.build(SupplierChainConstants.STK_IN_STOCK)
                        .select(fieldKeyList)
                        .in("FID", idList)
                        .page(1, QueryCondition.MAX_PAGE_LIMIT );
                List<net.bncloud.bis.model.vo.PurchaseInStockOrderVo> purchaseInStockOrderVos = k3cloudRemoteService.documentQueryWithClass( condition.queryParam(), net.bncloud.bis.model.vo.PurchaseInStockOrderVo.class);

                R apply = func.apply(purchaseInStockOrderVos);
                Assert.isTrue(apply.isSuccess(), apply.getMsg());

            } catch (Exception ex) {
                log.error("[PurchaseInStockOrder] syncData error! ids:{} ", idList, ex);
            }

        });
        updateTimeRun.run();

    }

    /**
     * 同步采购入库单
     *
     * @param experimentSupplier
     */
    public void experimentSupplierSyncPurchaseInStockOrder(ExperimentSupplier experimentSupplier) {

        Date startDate = new Date();

        String supplierCode = experimentSupplier.getSupplierCode();
        String syncDateTimeKey = supplierCode + "_" + BisSyncConstants.PURCHASE_IN_STOCK_ORDER_SYNC_ERP_TASK;
        Set<Long> allIdSet = getAllNeedSyncInStockOrderId(supplierCode, syncDateTimeKey);

        if (allIdSet.isEmpty()) {
            log.warn("[PurchaseInStockOrder] syncData is empty! ");
            return;
        }

        this.handleSync(allIdSet, purchaseInStockOrderVos -> {

            List<DeliveryDetailUpdateDTO> deliveryDetailUpdateDtoList = getDeliveryDetailUpdateDtoList(purchaseInStockOrderVos);

            // 更新到下游服务起
            return deliveryPlanFeignClient.syncErpDeliveryNoteInfos(deliveryDetailUpdateDtoList);

        }, () -> {

            RMap<String, Date> syncDataTimeMap = redissonClient.getMap(BisSyncConstants.TASK_SYNC_DATA_TIME_MAP_KEY, JsonJacksonCodec.INSTANCE);
            syncDataTimeMap.fastPut(syncDateTimeKey, startDate);

        });

    }

    /**
     * 获取需要同步的采购入库单Id集合
     *
     * @param supplierCode 供应商code
     * @return
     */
    private Set<Long> getAllNeedSyncInStockOrderId(String supplierCode, String syncDateTimeKey) {
        QueryCondition queryCondition = QueryCondition.build(SupplierChainConstants.STK_IN_STOCK)
                .select("FID")
                // 单据状态 已审核 需要其他状态枚举找产品
                .eq("FDocumentStatus", "C")
                .eq("FInStockEntry_Link_FSTableName", "T_PUR_ReceiveEntry")
                // 限制供应商
                .eq(StrUtil.isNotBlank(supplierCode), "FSupplierId.fnumber", supplierCode);

        Set<Long> allIdSet = new HashSet<>();
        segmentedQueryManager.doWhile(queryCondition, syncDateTimeKey, "FApproveDate", 1000, net.bncloud.bis.model.vo.PurchaseInStockOrderVo.class, purchaseInStockOrderVos -> {
            Set<Long> idSet = purchaseInStockOrderVos.stream().map(net.bncloud.bis.model.vo.PurchaseInStockOrderVo::getFid).collect(Collectors.toSet());
            allIdSet.addAll(idSet);
        });
        return allIdSet;
    }

    /**
     * 创建采购入库单
     * @param purchaseInStockOrderCreateVo
     * @return
     */
    public R<String> createPurchaseInStockOrder(PurchaseInStockOrderCreateVo purchaseInStockOrderCreateVo) {

        PurchaseInStockOrder purchaseInStockOrder = new PurchaseInStockOrder();
        BeanUtils.copyProperties(purchaseInStockOrderCreateVo,purchaseInStockOrder );
        purchaseInStockOrder.setFid(null);
        purchaseInStockOrder.setFBillTypeId( new Number( purchaseInStockOrderCreateVo.getFBillTypeId().getFNumber() ));
        purchaseInStockOrder.setFMsWlfl( new Number(purchaseInStockOrderCreateVo.getFMsWlfl().getFNumber()));
        purchaseInStockOrder.setFPurchaseOrgId( new Number(purchaseInStockOrderCreateVo.getFPurchaseOrgId().getFNumber() ) );
        purchaseInStockOrder.setFStockOrgId( new Number( purchaseInStockOrderCreateVo.getFStockOrgId().getFNumber() ) );

        K3CloudSaveParam<PurchaseInStockOrder> k3CloudSaveParam = SaveCondition.build(purchaseInStockOrder)
                .needReturnField(FieldKeyAnoUtils.getFieldKeyList(PurchaseInStockOrder.class))
                .needReturnField(FieldKeyAnoUtils.getFieldKeyList(PurchaseInStockOrder.FInStockEntry.class))
                .setAutoSubmitAndAudit(true)
                .k3CloudSaveParam();
        try {
            PurchaseInStockOrder purchaseInStockOrderResult = k3cloudRemoteService.saveOrUpdateWithReturnObj( SupplierChainConstants.STK_IN_STOCK, k3CloudSaveParam, PurchaseInStockOrder.class);
            log.info("[STK_InStock] 返回结果：{} ", JSON.toJSONString(purchaseInStockOrderResult) );

            //PurchaseInStockOrderCreateVo purchaseInStockOrderCreateVoReturn = new PurchaseInStockOrderCreateVo();
            //BeanUtils.copyProperties( purchaseInStockOrderResult, purchaseInStockOrderCreateVoReturn);

            //purchaseInStockOrderCreateVoReturn.setFid(purchaseInStockOrderResult.getFid());
            return R.data(purchaseInStockOrderResult.getFid());

        } catch (Exception exception) {
            log.error("[STK_InStock] createPurchaseInStockOrder error!",exception);
            return R.fail( exception.getMessage());
        }


    }

    /**
     * 批量创建采购入库单
     * @param purchaseInStockOrderCreateVoList
     * @return
     */
    public R<List<String>> batchCreatePurchaseInStockOrder(List<PurchaseInStockOrderCreateVo> purchaseInStockOrderCreateVoList) {

        List<PurchaseInStockOrder> purchaseInStockOrderList = purchaseInStockOrderCreateVoList.stream().map(purchaseInStockOrderCreateVo -> {
            PurchaseInStockOrder purchaseInStockOrder = new PurchaseInStockOrder();
            BeanUtils.copyProperties(purchaseInStockOrderCreateVo, purchaseInStockOrder);
            purchaseInStockOrder.setFid(null);
            purchaseInStockOrder.setFBillTypeId( new Number( purchaseInStockOrderCreateVo.getFBillTypeId().getFNumber() ));
            purchaseInStockOrder.setFMsWlfl( new Number(purchaseInStockOrderCreateVo.getFMsWlfl().getFNumber()));
            purchaseInStockOrder.setFPurchaseOrgId( new Number(purchaseInStockOrderCreateVo.getFPurchaseOrgId().getFNumber() ) );
            purchaseInStockOrder.setFStockOrgId( new Number( purchaseInStockOrderCreateVo.getFStockOrgId().getFNumber() ) );
            return purchaseInStockOrder;
        }).collect(Collectors.toList());

        /*K3CloudBatchSaveParam<PurchaseInStockOrder> batchSaveParam = SaveCondition.build(purchaseInStockOrderList)
                .needReturnField(FieldKeyAnoUtils.getFieldKeyList(PurchaseInStockOrder.class))
                .needReturnField(FieldKeyAnoUtils.getFieldKeyList(PurchaseInStockOrder.FInStockEntry.class))
                .setAutoSubmitAndAudit(true)
                .k3CloudBatchSaveParam();*/
        SaveCondition<PurchaseInStockOrder> purchaseInStockOrderSaveCondition = SaveCondition.build(purchaseInStockOrderList)
                .needReturnField(FieldKeyAnoUtils.getFieldKeyList(PurchaseInStockOrder.class))
                .needReturnField(FieldKeyAnoUtils.getFieldKeyList(PurchaseInStockOrder.FInStockEntry.class))
                .setAutoSubmitAndAudit(true);
        K3CloudBatchSaveParam<PurchaseInStockOrder> batchSaveParam = purchaseInStockOrderSaveCondition.k3CloudBatchSaveParam();
        try {
            List<PurchaseInStockOrder> returnPurchaseInStockOrderList = k3cloudRemoteService.batchSaveOrUpdateWithReturnObj(SupplierChainConstants.STK_IN_STOCK, batchSaveParam, PurchaseInStockOrder.class);
            log.info("[STK_InStock] 返回结果：{} ", JSON.toJSONString( returnPurchaseInStockOrderList) );

            return R.data( returnPurchaseInStockOrderList.stream().map(purchaseInStockOrder -> {
                //PurchaseInStockOrderCreateVo purchaseInStockOrderCreateVoReturn = new PurchaseInStockOrderCreateVo();
                //BeanUtils.copyProperties(purchaseInStockOrder, purchaseInStockOrderCreateVoReturn);
                return purchaseInStockOrder.getFid();
            }).collect(Collectors.toList()) );

        } catch (Exception exception) {
            log.error("[STK_InStock] createPurchaseInStockOrder error!",exception);
            return R.fail( exception.getMessage());
        }

    }
}
