package net.bncloud.bis.manager;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.xxl.job.core.context.XxlJobHelper;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.bis.constant.BisSyncConstants;
import net.bncloud.bis.model.entity.ExperimentSupplier;
import net.bncloud.bis.model.vo.PurchaseOrderVo;
import net.bncloud.bis.properties.ApplicationProperties;
import net.bncloud.common.api.R;
import net.bncloud.common.base.globallock.DistributedLock;
import net.bncloud.common.base.globallock.lock.LockWrapper;
import net.bncloud.msk3cloud.constant.formid.SupplierChainConstants;
import net.bncloud.msk3cloud.core.condition.QueryCondition;
import net.bncloud.msk3cloud.kingdee.K3cloudRemoteService;
import net.bncloud.msk3cloud.util.FieldKeyAnoUtils;
import net.bncloud.oem.service.api.feign.OemPurchaseOrderFeignClient;
import net.bncloud.oem.service.api.feign.OemReceivingAddressFeignClient;
import net.bncloud.oem.service.api.vo.OemPurchaseOrderVo;
import net.bncloud.oem.service.api.vo.OemReceivingAddressVo;
import net.bncloud.serivce.api.order.dto.OrderErpDTO;
import net.bncloud.serivce.api.order.dto.OrderProductDetailsErpDTO;
import net.bncloud.serivce.api.order.feign.ZcOrderServiceFeignClient;
import net.bncloud.service.api.delivery.dto.DeliveryPlanDTO;
import net.bncloud.service.api.delivery.entity.DeliveryPlanDetailEntity;
import net.bncloud.service.api.delivery.entity.DeliveryPlanDetailItemEntity;
import net.bncloud.service.api.delivery.entity.DeliveryPlanEntity;
import net.bncloud.service.api.delivery.feign.DeliveryPlanFeignClient;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * desc: 采购订单
 *
 * @author Rao
 * @Date 2022/01/17
 **/
@Slf4j
@Component
public class PurchaseOrderManager {

    @Autowired
    private K3cloudRemoteService k3cloudRemoteService;
    @Resource
    private ZcOrderServiceFeignClient zcOrderServiceFeignClient;
    @Resource
    private DeliveryPlanFeignClient deliveryPlanFeignClient;
    @Autowired
    private SegmentedQueryManager segmentedQueryManager;
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private DistributedLock distributedLock;
    @Autowired
    private ApplicationProperties applicationProperties;
    @Resource
    private OemPurchaseOrderFeignClient oemPurchaseOrderFeignClient;
    @Resource
    private OemReceivingAddressFeignClient oemReceivingAddressFeignClient;

    /**
     * 同步订单数据
     * syncType
     * 1、采购订单同步
     * 2、送货计划同步
     */
    @Deprecated
    public void syncData(int syncType) {
        // 下版本删除接口
    }

    /**
     * 同步处理
     * @param allBillNoList
     */
    private void handleSync(List<String> allBillNoList, Function<Map<String, List<PurchaseOrderVo>>, R> function,Runnable updateTimeRun) {
        Class<PurchaseOrderVo> purchaseOrderVoClass = PurchaseOrderVo.class;
        List<String> fieldKeyList = FieldKeyAnoUtils.getFieldKeyList( purchaseOrderVoClass);

        // 等分
        List<List<String>> allBillNoSplitList = CollUtil.split(allBillNoList, 100);
        allBillNoSplitList.forEach(billNoList -> {

            try {

                QueryCondition queryCondition = QueryCondition.build( SupplierChainConstants.PUR_PURCHASEORDER)
                        .select(fieldKeyList)
                        .in("FBillNo", billNoList)
                        .page(1, QueryCondition.MAX_PAGE_LIMIT );

                List<PurchaseOrderVo> purchaseOrderVoList = k3cloudRemoteService.documentQueryWithClass(queryCondition.queryParam(), purchaseOrderVoClass);

                if (CollectionUtils.isEmpty(purchaseOrderVoList)) {
                    log.info(" [PurchaseOrder] syncData empty!");
                    return;
                }

                // 基于billNo 进行划分 明细
                Map<String, List<PurchaseOrderVo>> billNoEntryMap = purchaseOrderVoList.stream().collect(Collectors.groupingBy(PurchaseOrderVo::getFBillNo));
                try {
                    R result = function.apply(billNoEntryMap);
                    Assert.isTrue(result.isSuccess(),result.getMsg());

                } catch (Exception ex) {
                    log.error("[PurchaseOrder] feign order feign client fail !", ex);
                }

            } catch (Exception exception) {
                log.error("[PurchaseOrder] syncData error! billNoList:{}",billNoList, exception);
            }
        });
        // 更新时间
        updateTimeRun.run();
    }

    /**
     * 处理送货计划
     *
     * @param billNoEntryMap
     * @return
     */
    private List<DeliveryPlanDTO> handleDeliveryPlan(Map<String, List<PurchaseOrderVo>> billNoEntryMap) {

        List<DeliveryPlanDTO> deliveryPlanList = new ArrayList<>();

        // 一个采购订单就是一个送货计划
        for (Map.Entry<String, List<PurchaseOrderVo>> billNoEntry : billNoEntryMap.entrySet()) {

            DeliveryPlanDTO deliveryPlanDto = new DeliveryPlanDTO();

            // 送货计划
            PurchaseOrderVo purchaseOrderVo = billNoEntry.getValue().get(0);
            DeliveryPlanEntity deliveryPlanEntity = this.wrapperDeliveryPlanBody(purchaseOrderVo);
            deliveryPlanDto.setDeliveryPlanEntity( deliveryPlanEntity);

            List<PurchaseOrderVo> entries = billNoEntry.getValue();

            Map<Long, List<PurchaseOrderVo>> entryIdLineListMap = entries.stream().collect(Collectors.groupingBy( PurchaseOrderVo::getFEntryID));

            // 明细
            List<DeliveryPlanDetailEntity> deliveryPlanDetailEntityList = entries.stream()
                    // 去重
                    .collect(
                            Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(PurchaseOrderVo::getFEntryID))), ArrayList::new)
                    ).stream()
                    // 转换
                    .map(purchaseOrderEntry -> this.wrapperDeliveryPlanEntry(purchaseOrderEntry, entryIdLineListMap))
                    .collect(Collectors.toList());
            deliveryPlanDto.setPlanDetailEntityList(deliveryPlanDetailEntityList);

            deliveryPlanList.add(deliveryPlanDto);
        }

        return deliveryPlanList;

    }

    /**
     * 包装送货计划的明细
     *
     * @param purchaseOrderVo
     * @param entryIdLineListMap
     * @return
     */
    private DeliveryPlanDetailEntity wrapperDeliveryPlanEntry(PurchaseOrderVo purchaseOrderVo, Map<Long, List<PurchaseOrderVo>> entryIdLineListMap) {

        DeliveryPlanDetailEntity deliveryPlanDetailEntity = new DeliveryPlanDetailEntity();
        //商家编码
        deliveryPlanDetailEntity.setMerchantCode(purchaseOrderVo.getFMssjbm());
        // 产品编码
        deliveryPlanDetailEntity.setProductCode(purchaseOrderVo.getFMaterialId());
        // 名称
        deliveryPlanDetailEntity.setProductName(purchaseOrderVo.getFMaterialName());
        // 产品规格
        deliveryPlanDetailEntity.setProductSpecification(purchaseOrderVo.getFModel());
        //产品单价
        deliveryPlanDetailEntity.setProductUnitPrice(purchaseOrderVo.getFPrice());
        //产品含税单价
        deliveryPlanDetailEntity.setTaxUnitPrice(purchaseOrderVo.getFTaxPrice());
        //供应商编码
        deliveryPlanDetailEntity.setSupplierCode(purchaseOrderVo.getFSupplierId());
        //采购单号
        deliveryPlanDetailEntity.setPurchaseOrderNo(purchaseOrderVo.getFMsDdbh());
        //单据编号
        deliveryPlanDetailEntity.setBillNo(purchaseOrderVo.getFBillNo());
        //送货地址
        // deliveryPlanDetailEntity.setDeliveryAddress(purchaseOrderVo.getFELocationAddress());
        // 计划单位
        deliveryPlanDetailEntity.setPlanUnit( purchaseOrderVo.getFPlanUnitId() );
        // 计划数量
        // deliveryPlanDetailEntity.setPlanQuantity( purchaseOrderVo.getFQty()+"" );
        //来源系统主键ID
        deliveryPlanDetailEntity.setSourceId( purchaseOrderVo.getFEntryID() + "");
        // 明细项次
        List<PurchaseOrderVo> entryLineList = entryIdLineListMap.getOrDefault( purchaseOrderVo.getFEntryID(), new ArrayList<>());

        List<DeliveryPlanDetailItemEntity> deliveryPlanDetailItemEntityList = entryLineList.stream().map(this::wrapperDeliveryPlanEntryLine).collect(Collectors.toList());
        deliveryPlanDetailEntity.setPlanDetailItemList(deliveryPlanDetailItemEntityList);

        return deliveryPlanDetailEntity;

    }

    /**
     * 包装 送货计划明细项次
     *
     * @param purchaseOrderVo
     * @return
     */
    private DeliveryPlanDetailItemEntity wrapperDeliveryPlanEntryLine(PurchaseOrderVo purchaseOrderVo) {

        DeliveryPlanDetailItemEntity deliveryPlanDetailItemEntity = new DeliveryPlanDetailItemEntity();

        // 交货日期
        String fDeliveryDateplan = purchaseOrderVo.getFDeliveryDatePlan();
        deliveryPlanDetailItemEntity.setDeliveryDate( LocalDateTime.parse( fDeliveryDateplan ) );

        //送货数量
        deliveryPlanDetailItemEntity.setDeliveryQuantity(purchaseOrderVo.getFPlanQty()+"");

        //送货地址
        deliveryPlanDetailItemEntity.setDeliveryAddress(purchaseOrderVo.getFELocationAddress());

        //来源系统主键ID
        deliveryPlanDetailItemEntity.setSourceId( purchaseOrderVo.getFDetailId()+"" );

        //入货仓
        deliveryPlanDetailItemEntity.setWarehousing(purchaseOrderVo.getFSrmCkNumber());

        //剩余可发货数量
        deliveryPlanDetailItemEntity.setRemainingQuantity(purchaseOrderVo.getFPlanQty()+"");

        return deliveryPlanDetailItemEntity;

    }

    /**
     * 包装送货计划的body
     *
     * @param purchaseOrderVo
     * @return
     */
    private DeliveryPlanEntity wrapperDeliveryPlanBody(PurchaseOrderVo purchaseOrderVo) {

        DeliveryPlanEntity deliveryPlanEntity = new DeliveryPlanEntity();
        // 采购方编号
        deliveryPlanEntity.setPurchaseCode(purchaseOrderVo.getFPurchaseOrgId());
        //采购方编码
        deliveryPlanEntity.setSupplierCode(purchaseOrderVo.getFSupplierId());
        // 计划说明
        deliveryPlanEntity.setPlanDescription( purchaseOrderVo.getFAbcRemarks() );
        //单据编号
        deliveryPlanEntity.setBillNo(purchaseOrderVo.getFBillNo());
        //订单类型 ERP获取类型
        deliveryPlanEntity.setOrderType(purchaseOrderVo.getFBillTypeID());


        // 结算币种
        deliveryPlanEntity.setCurrency( purchaseOrderVo.getFSettleCurrId() );
        // 物料分类
        deliveryPlanEntity.setMaterialClassification( purchaseOrderVo.getFMsWlfl() );
        // 入库仓
        deliveryPlanEntity.setWarehousing( purchaseOrderVo.getFSrmCkNumber() );

        // 订单编号
        deliveryPlanEntity.setPurchaseOrderNo(purchaseOrderVo.getFMsDdbh());
        //来源系统主键ID
        deliveryPlanEntity.setSourceId(purchaseOrderVo.getFid()+"");

        // 采购日期
        deliveryPlanEntity.setPurchaseTime( LocalDateTime.parse( purchaseOrderVo.getFDate() ) );

        return deliveryPlanEntity;

    }

    /**
     * 处理订单信息
     *
     * @return
     */
    public List<OrderErpDTO> handlePurchaseOrder(Map<String, List<PurchaseOrderVo>> billNoEntryMap) {


        // 拆分数据格式 订单明细
        List<OrderErpDTO> orderErpList = new ArrayList<>();

        for (Map.Entry<String, List<PurchaseOrderVo>> billNoEntry : billNoEntryMap.entrySet()) {

            // 明细
            List<PurchaseOrderVo> entries = billNoEntry.getValue();

            // Order 信息
            PurchaseOrderVo purchaseOrderVo = entries.get(0);
            OrderErpDTO orderErpDto = this.wrapperOrderBody(purchaseOrderVo);

            // 订单明细  基于entryId去重
            List<OrderProductDetailsErpDTO> orderProductDetailsErpList = entries.stream()
                    // 去重
                    .collect(Collectors.collectingAndThen(
                            Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(PurchaseOrderVo::getFEntryID))), ArrayList::new)
                    ).stream()
                    // 转换
                    .map(this::wrapperOrderEntry)
                    .collect(Collectors.toList());
            // 订单明细
            orderErpDto.setOrderProductDetailsErpList(orderProductDetailsErpList);

            orderErpList.add(orderErpDto);

        }

        return orderErpList;

    }

    /**
     * 处理oem订单信息
     *
     * @return
     */
    public List<OemPurchaseOrderVo> handleOemPurchaseOrder(Map<String, List<PurchaseOrderVo>> billNoEntryMap) {
        // 拆分数据格式 订单明细
        List<OemPurchaseOrderVo> oemPurchaseOrderVoList = new ArrayList<>();

        for (Map.Entry<String, List<PurchaseOrderVo>> billNoEntry : billNoEntryMap.entrySet()) {

            // 明细 订单里面的明细
            List<PurchaseOrderVo> entries = billNoEntry.getValue();

            // Order 信息
            PurchaseOrderVo purchaseOrderVo = entries.get(0);
            OemPurchaseOrderVo oemPurchaseOrderVo = this.wrapperOemOrderBody(purchaseOrderVo);

            // 订单明细
            List<OemPurchaseOrderVo.PurchaseOrderMaterial> purchaseOrderMaterialList = entries.stream()
                    // 去重
                    .collect(Collectors.collectingAndThen(
                            Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(PurchaseOrderVo::getFEntryID))), ArrayList::new)
                    ).stream()
                    // 转换
                    .map(this::wrapperOemOrderEntry)
                    .collect(Collectors.toList());
            // 订单明细
            oemPurchaseOrderVo.setPurchaseOrderMaterialList(purchaseOrderMaterialList);

            oemPurchaseOrderVoList.add(oemPurchaseOrderVo);

        }

        return oemPurchaseOrderVoList;

    }

    /**
     * 处理oem地址信息
     *
     * @return
     */
    public List<OemReceivingAddressVo> handleOemAddress(Map<String, List<PurchaseOrderVo>> billNoEntryMap) {
        // 拆分数据格式 订单明细
        List<OemReceivingAddressVo> oemReceivingAddressVoList = new ArrayList<>();


        for (Map.Entry<String, List<PurchaseOrderVo>> billNoEntry : billNoEntryMap.entrySet()) {

            // 明细 订单里面的明细
            List<PurchaseOrderVo> entries = billNoEntry.getValue();

            // 获取订单信息
            PurchaseOrderVo purchaseOrderVo = entries.get(0);
            OemReceivingAddressVo oemReceivingAddressVo = this.wrapperOemOAddressBody(purchaseOrderVo);
            //添加到list
            oemReceivingAddressVoList.add(oemReceivingAddressVo);

        }

        return oemReceivingAddressVoList;

    }

    /**
     * 包装订单明细
     *
     * @param orderEntry
     * @return
     */
    private OrderProductDetailsErpDTO wrapperOrderEntry(PurchaseOrderVo orderEntry) {

        OrderProductDetailsErpDTO orderProductDetailsErpDto = new OrderProductDetailsErpDTO();
        //订单编号
        orderProductDetailsErpDto.setPurchaseOrderCode(orderEntry.getFBillNo());
        // 订单明细主键
        orderProductDetailsErpDto.setItemCode(orderEntry.getFEntryID() + "");
        // 产品编码
        orderProductDetailsErpDto.setProductCode(orderEntry.getFMaterialId());
        // 产品名称
        orderProductDetailsErpDto.setProductName(orderEntry.getFMaterialName());
        // 产品规格
        orderProductDetailsErpDto.setProductSpecs(orderEntry.getFModel());
        // 条码 F_MS_SJBM
        orderProductDetailsErpDto.setMerchantCode(orderEntry.getFMssjbm());
        // 交货日期
        String fDeliveryDate = orderEntry.getFDeliveryDate();
        // 条码 F_MS_SJBM
        orderProductDetailsErpDto.setMerchantCode(orderEntry.getFMssjbm());
        try {
            orderProductDetailsErpDto.setDeliveryTime( new Date( LocalDateTime.parse(fDeliveryDate).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() ));
        }catch (Exception ex){
            log.error("BillNo单据：{}，送货日期出错！",orderEntry.getFBillNo(),ex);
        }

        // 采购数量
        int fQty = Optional.ofNullable(orderEntry.getFQty()).orElse(0);
        orderProductDetailsErpDto.setPurchaseNum(new BigDecimal(fQty).setScale(2, RoundingMode.HALF_UP));
        //采购单位
        orderProductDetailsErpDto.setPurchaseUnit(orderEntry.getFUnitId());
        //采购编码
        orderProductDetailsErpDto.setPurchaseCode(orderEntry.getFUnitId());
        // 计价数量
        int fPriceUnitQty = Optional.ofNullable(orderEntry.getFPriceUnitQty()).orElse(0);
        orderProductDetailsErpDto.setMarkDownNum(new BigDecimal(fPriceUnitQty));
        //计价单位
        orderProductDetailsErpDto.setMarkDownUnit(orderEntry.getFPriceUnitId());
        //计价编码
        orderProductDetailsErpDto.setMarkDownCode(orderEntry.getFPriceUnitId());
        //收货仓库
        orderProductDetailsErpDto.setTakeOverWarehouse(orderEntry.getFSrmCkNumber());
        //（MS用）计价单位
        orderProductDetailsErpDto.setValuationUnit(orderEntry.getFPriceUnitId());
        // 单价
        orderProductDetailsErpDto.setUnitPrice(orderEntry.getFPrice());
        // 产品总价
        orderProductDetailsErpDto.setProductTotalPrice(orderEntry.getFEntryAmount());
        // 含税单价
        orderProductDetailsErpDto.setTaxPrice(orderEntry.getFTaxPrice());
        // 税率
        orderProductDetailsErpDto.setTaxRate(orderEntry.getFEntryTaxRate());
        // 税额
        orderProductDetailsErpDto.setTaxAmount(orderEntry.getFEntryTaxAmount());
        //税价合计
        orderProductDetailsErpDto.setAllAmount(orderEntry.getFAllAmount());
        //变更标志
        orderProductDetailsErpDto.setChangeCode(orderEntry.getFChangeFlag());
        // 来源ID
        orderProductDetailsErpDto.setSourceId( orderEntry.getFEntryID()+"");

        // 计划单位
        orderProductDetailsErpDto.setPlanUnit( orderEntry.getFPlanUnitId() );

        //剩余可发货数量
        orderProductDetailsErpDto.setRemainingQuantity(orderEntry.getFPlanQty()+"");

        //送货数量
        orderProductDetailsErpDto.setDeliveryQuantity(orderEntry.getFPlanQty()+"");

        //交货地址
        orderProductDetailsErpDto.setDeliveryAddress(orderEntry.getFELocationAddress());

        return orderProductDetailsErpDto;

    }

    /**
     * 包装oem订单明细(物料 第二层)
     *
     * @param orderEntry
     * @return
     */
    private OemPurchaseOrderVo.PurchaseOrderMaterial wrapperOemOrderEntry(PurchaseOrderVo orderEntry) {


        OemPurchaseOrderVo.PurchaseOrderMaterial purchaseOrderMaterial = new OemPurchaseOrderVo.PurchaseOrderMaterial();

        //id purchaseOrderId   remaining_quantity  receiving_times   received_quantity  take_over_status

        //delivery_type 交货方式  ???

        //answer_status 答交状态  ????


        // 交货日期  delivery_date
        String fDeliveryDate = orderEntry.getFDeliveryDate();
        purchaseOrderMaterial.setDeliveryDate(LocalDateTime.parse(fDeliveryDate));

        //material_code  name
        purchaseOrderMaterial.setMaterialCode(orderEntry.getFMaterialId());
        purchaseOrderMaterial.setMaterialName(orderEntry.getFMaterialName());

        //bar_code 条码
        purchaseOrderMaterial.setBarCode(orderEntry.getFMssjbm());

        //purchase_quantity 采购数量
        int fQty = Optional.ofNullable(orderEntry.getFQty()).orElse(0);
        purchaseOrderMaterial.setPurchaseQuantity(fQty+"");

        // todo source_erp_id   来源ERPID    fEntryID??
        //来源系统主键ID
        purchaseOrderMaterial.setSourceErpId( orderEntry.getFDetailId()+"" );

        //货主类型 BD_OwnerOrg、BD_Supplier、BD_Customer
        //purchaseOrderMaterial.setOwnerTypeid(orderEntry.getOwnerTypeid());

        //入库仓 仓库
        purchaseOrderMaterial.setWarehouse(orderEntry.getFSrmCkNumber());
        //采购数量
        purchaseOrderMaterial.setPurchaseQuantity(orderEntry.getFQty().toString());
        //条码/商家编码
        purchaseOrderMaterial.setBarCode(orderEntry.getFMssjbm());

        //含税单价  fTaxPrice
        purchaseOrderMaterial.setTaxPrice(orderEntry.getFTaxPrice().toString());


        return purchaseOrderMaterial;

    }

    /**
     * 包装 订单主体
     *
     * @param purchaseOrderVo
     * @return
     */
    private OrderErpDTO wrapperOrderBody(PurchaseOrderVo purchaseOrderVo) {

        OrderErpDTO orderErpDto = new OrderErpDTO();

        // 订单金额
        String fBillAllAmount = purchaseOrderVo.getFBillAllAmount();
        orderErpDto.setOrderPrice( new BigDecimal( fBillAllAmount ) );

        //ERP中订单的关闭状态
        orderErpDto.setOffState(purchaseOrderVo.getFCloseStatus());
        //订单关闭状态的关闭时间 FCloseDate
        orderErpDto.setClosedTime(purchaseOrderVo.getFCloseDate());

        // 供应商方编号
        orderErpDto.setSupplierCode(purchaseOrderVo.getFSupplierId());
        // 采购方编码
        orderErpDto.setPurchaseCode(purchaseOrderVo.getFPurchaseOrgId());
        // 单据编号
        orderErpDto.setPurchaseOrderCode(purchaseOrderVo.getFBillNo());
        // 采购日期
        String fDate = purchaseOrderVo.getFDate();
        orderErpDto.setPurchaseTime( Date.from( LocalDateTime.parse( fDate ).atZone( ZoneId.systemDefault()).toInstant() ) );
        //币别
        orderErpDto.setCurrency(purchaseOrderVo.getFSettleCurrId());
        //采购人编码
        orderErpDto.setPurchaseUserCode(purchaseOrderVo.getFPurchaserIdFnumber());
        // 采购人
        orderErpDto.setPurchaseUserName(purchaseOrderVo.getFPurchaserIdName());
        //付款条件
        orderErpDto.setPaymentTerms(purchaseOrderVo.getFPayConditionId());
        //采购部门
        orderErpDto.setPurchaseDepartment(purchaseOrderVo.getFPurchaseDeptId());
        //收货地址
        orderErpDto.setReceivingAddress(purchaseOrderVo.getFAbcJhdd());
        //订单备注
        orderErpDto.setOrderRemarks(purchaseOrderVo.getFAbcRemarks());
        // 订单类型
        orderErpDto.setOrderType(purchaseOrderVo.getFBillTypeID());
        // 订单编号
        orderErpDto.setOrderNo(purchaseOrderVo.getFMsDdbh());
        // 验收方式
        orderErpDto.setAcceptanceMethod(purchaseOrderVo.getFAccType());
        // 付款方式
        orderErpDto.setPaymentMethod(purchaseOrderVo.getFAbcbaseProperty1());
        //账期(天)
        String fAbcbaseProperty = Optional.ofNullable( purchaseOrderVo.getFAbcbaseProperty() ).orElse("-1");
        try {
            Integer integer = new BigDecimal(fAbcbaseProperty).intValue();
            orderErpDto.setAccountingPeriod(integer);
        } catch (Exception ex) {
            log.error("[PurchaseOrder] parse properties fAbcbaseProperty fail!",ex);
        }

        // 物料分类
        orderErpDto.setItemClass(purchaseOrderVo.getFMsWlfl());
        //  运输方式
        orderErpDto.setShippingType(purchaseOrderVo.getFAbcCombo());
        //  运输方式
        orderErpDto.setLogisticsMode(purchaseOrderVo.getFAbcCombo());

        //变更原因
        orderErpDto.setChangeReason(purchaseOrderVo.getFChangeReason());

        // 来源ID
        orderErpDto.setSourceId( purchaseOrderVo.getFid()+"" );

        return orderErpDto;

    }

    /**
     * 包装 oem订单主体
     *
     * @param purchaseOrderVo
     * @return
     */
    private OemPurchaseOrderVo wrapperOemOrderBody(PurchaseOrderVo purchaseOrderVo) {

        OemPurchaseOrderVo oemPurchaseOrderVo = new OemPurchaseOrderVo();

        //id  收货状态     //订单编号（智采生成） order_no
        //oem供应商编码 和oem名字,srm里面进行设置
        //地址编码  receiving_address_code  自己生成
        //todo 设置地址
        oemPurchaseOrderVo.setOemSupplierAddress(StrUtil.trim(purchaseOrderVo.getFaBCrEMARKS1() ) );
        //oemPurchaseOrderVo.setOemSupplierAddress(StrUtil.trim("测试地址" ) );
        //采购方和供应商的编码,名字在srm进行设置
        oemPurchaseOrderVo.setPurchaseCode(purchaseOrderVo.getFPurchaseOrgId());
        oemPurchaseOrderVo.setSupplierCode(purchaseOrderVo.getFSupplierId());


        //confirm_date 确认日期 ????

        // 单据编号 purchase_order_code
        oemPurchaseOrderVo.setPurchaseOrderCode(purchaseOrderVo.getFBillNo());

        // 供应商方编号
        oemPurchaseOrderVo.setSupplierCode(purchaseOrderVo.getFSupplierId());
        // 采购方编码
        oemPurchaseOrderVo.setPurchaseCode(purchaseOrderVo.getFPurchaseOrgId());

        //采购时间 = 采购日期 purchaseDate
        String fDate = purchaseOrderVo.getFDate();
        oemPurchaseOrderVo.setPurchaseDate(  LocalDateTime.parse(fDate) );

        // 来源ID
        oemPurchaseOrderVo.setSourceErpId( purchaseOrderVo.getFid()+"" );

        // 单据类型
        oemPurchaseOrderVo.setOrderType( purchaseOrderVo.getFBillTypeID() );

        return oemPurchaseOrderVo;

    }

    /**
     * 包装 oem地址主体
     *
     * @param purchaseOrderVo
     * @return
     */
    private OemReceivingAddressVo wrapperOemOAddressBody(PurchaseOrderVo purchaseOrderVo) {

        OemReceivingAddressVo addressVo = new OemReceivingAddressVo();

        //id  status

        //地址的code  地址名?   oem供应商名称 supplier_code   supplier_name  sourceType数据来源

        //地址是这个吗
        //addressVo.setAddress(purchaseOrderVo.getFAbcBaseProperty4());
        return addressVo;
    }

    /**
     * 获取全部单据编号Id
     * 条件 ：
     *  1、
     *
     * @param supplierCode 供应商code 用于查询条件
     * @param syncLastDateTimeKey Redis存的时间key，用于区分订单同步和送货计划同步
     * @return
     */
    public List<String> getAllBillNoListWithSupplierCode(String supplierCode, String syncLastDateTimeKey,String syncLastCloseDateTimeKey ) {
        Set<String> allBillNoSet = new HashSet<>();
        QueryCondition queryCondition = QueryCondition.build( SupplierChainConstants.PUR_PURCHASEORDER)
                .select("FBillNo")
                // 单据状态 (已审核 C和重新审核)
                .in("FDocumentStatus", "C","D")
                // 关闭状态 未关闭 需要其他状态枚举找产品
//                .eq("FCloseStatus", "A")
                // 限制供应商
                .eq( "FSupplierId.fnumber",supplierCode )
                ;

        // 获取上一次同步的时间蹉
        RMap<String, Date> syncDataTimeMap = redissonClient.getMap( BisSyncConstants.TASK_SYNC_DATA_TIME_MAP_KEY, JsonJacksonCodec.INSTANCE);
        Date lastSyncDataTime = syncDataTimeMap.getOrDefault( syncLastDateTimeKey, applicationProperties.getSchedulingTask().getDefaultSyncDateTime( supplierCode ) );

        // 获取关闭时间同步的时间值
        Date syncLastCloseDateTime = syncDataTimeMap.getOrDefault( syncLastCloseDateTimeKey, applicationProperties.getSchedulingTask().getDefaultSyncDateTime( supplierCode ) );

        // FApproveDate 审核时间  FModifyDate 修改时间 FCloseDate 关闭时间
        queryCondition.or( qc -> qc.gt( "FApproveDate",lastSyncDataTime ).gt("FModifyDate",lastSyncDataTime).gt("FCloseDate",syncLastCloseDateTime)  );
        segmentedQueryManager.whileHandle( queryCondition, 1000,PurchaseOrderVo.class, purchaseOrderVoList -> {
            Set<String> billNoSet = purchaseOrderVoList.stream().map(PurchaseOrderVo::getFBillNo).collect( Collectors.toSet());
            allBillNoSet.addAll( billNoSet );
        } );

        return new ArrayList<>(allBillNoSet);

    }

    /**
     * 获取F_abc_BaseProperty4不为空的oem单据编号Id
     * 条件 ：
     *  1、
     *
     * @param supplierCode 供应商code 用于查询条件
     * @param syncLastDateTimeKey Redis存的时间key，用于区分订单同步和送货计划同步
     * @return
     */
    public List<String> getAllOemBillNoListWithSupplierCode(String supplierCode, String syncLastDateTimeKey,String syncLastCloseDateTimeKey ) {
        Set<String> allBillNoSet = new HashSet<>();
        QueryCondition queryCondition = QueryCondition.build( SupplierChainConstants.PUR_PURCHASEORDER)
                .select("FBillNo")
                // 单据状态 (已审核 C和重新审核)
                .in("FDocumentStatus", "C","D")
                // 关闭状态 未关闭 需要其他状态枚举找产品
//                .eq("FCloseStatus", "A")
                // 限制供应商
                .eq( "FSupplierId.fnumber",supplierCode )

                .isNotNull("F_ABC_REMARKS1")
                .ne("F_ABC_REMARKS1","")
                ;

        // 获取上一次同步的时间蹉
        RMap<String, Date> syncDateTimeMap = redissonClient.getMap( BisSyncConstants.TASK_SYNC_DATA_TIME_MAP_KEY, JsonJacksonCodec.INSTANCE);
        Date lastSyncDataTime = syncDateTimeMap.getOrDefault( syncLastDateTimeKey, applicationProperties.getSchedulingTask().getDefaultSyncDateTime( supplierCode ) );

        // 获取关闭时间同步的时间值
        Date syncLastCloseDateTime = syncDateTimeMap.getOrDefault( syncLastCloseDateTimeKey, applicationProperties.getSchedulingTask().getDefaultSyncDateTime( supplierCode ) );

        // FApproveDate 审核时间  FModifyDate 修改时间 FCloseDate 关闭时间
        queryCondition.or( qc -> qc.gt( "FApproveDate",lastSyncDataTime ).gt("FModifyDate",lastSyncDataTime).gt("FCloseDate",syncLastCloseDateTime)  );
        segmentedQueryManager.whileHandle( queryCondition, 1000,PurchaseOrderVo.class, purchaseOrderVoList -> {
            Set<String> billNoSet = purchaseOrderVoList.stream().map(PurchaseOrderVo::getFBillNo).collect( Collectors.toSet());
            allBillNoSet.addAll( billNoSet );
        } );

        return new ArrayList<>(allBillNoSet);

    }

    /**
     * 获取全部单据编号Id
     *
     * @return
     */
    public List<String> getAllBillNoList(String syncDateTimeKey) {
        Set<String> allBillNoSet = new HashSet<>();
        QueryCondition queryCondition = QueryCondition.build( SupplierChainConstants.PUR_PURCHASEORDER)
                .select("FBillNo")
                // 单据状态 已审核 需要其他状态枚举找产品
                .eq("FDocumentStatus", "C")
                // 关闭状态 未关闭 需要其他状态枚举找产品
                .eq("FCloseStatus", "A");

        // 获取上一次同步的时间蹉
        RMap<String, Date> syncDataTimeMap = redissonClient.getMap( BisSyncConstants.TASK_SYNC_DATA_TIME_MAP_KEY, JsonJacksonCodec.INSTANCE);
        Date lastSyncDataTime = syncDataTimeMap.getOrDefault( syncDateTimeKey, applicationProperties.getSchedulingTask().getDefaultSyncDateTime( syncDateTimeKey ) );

        queryCondition.or( qc -> qc.gt( "FApproveDate",lastSyncDataTime ).gt("FModifyDate",lastSyncDataTime)  );

        segmentedQueryManager.whileHandle( queryCondition, 1000,PurchaseOrderVo.class, purchaseOrderVoList -> {
            Set<String> billNoSet = purchaseOrderVoList.stream().map(PurchaseOrderVo::getFBillNo).collect( Collectors.toSet());
            allBillNoSet.addAll( billNoSet );
        } );

        return new ArrayList<>(allBillNoSet);

    }

    /**
     * 同步订单
     */
    public void syncOrder() {

        Date startDate = new Date();
        // 单据编号
        List<String> allBillNoList = this.getAllBillNoList(BisSyncConstants.ORDER_SYNC_ERP_TASK);

        if (CollectionUtils.isEmpty(allBillNoList)) {
            log.warn("[PurchaseOrder] syncData allBillNoSet is empty ,current sync data fail !");
            return;
        }

        this.handleSync( allBillNoList, billNoEntryMap -> {
            List<OrderErpDTO> orderErpList = this.handlePurchaseOrder(billNoEntryMap);
            return zcOrderServiceFeignClient.syncErp(orderErpList);
        },() ->{
            RMap<String, Date> syncDataTimeMap = redissonClient.getMap( BisSyncConstants.TASK_SYNC_DATA_TIME_MAP_KEY, JsonJacksonCodec.INSTANCE);
            syncDataTimeMap.fastPut( BisSyncConstants.ORDER_SYNC_ERP_TASK,startDate );
        } );


    }

    /**
     * 同步送货计划先同步送货单
     */
    public void syncDeliveryPlan() {

        // 单据编号
        Date startDate = new Date();
        List<String> allBillNoList = this.getAllBillNoList(BisSyncConstants.DELIVERY_PLAN_SYNC_ERP_TASK);

        if (CollectionUtils.isEmpty(allBillNoList)) {
            log.warn("[PurchaseOrder] syncData allBillNoSet is empty ,current sync data fail !");
            return;
        }

        this.handleSync( allBillNoList, billNoEntryMap -> {
            List<DeliveryPlanDTO> deliveryPlanDtoList = this.handleDeliveryPlan(billNoEntryMap);
            return deliveryPlanFeignClient.receiveDeliveryPlans(deliveryPlanDtoList);
        },() ->{
            RMap<String, Date> syncDataTimeMap = redissonClient.getMap( BisSyncConstants.TASK_SYNC_DATA_TIME_MAP_KEY, JsonJacksonCodec.INSTANCE);
            syncDataTimeMap.fastPut( BisSyncConstants.DELIVERY_PLAN_SYNC_ERP_TASK,startDate );
        } );

    }


    /**
     * 同步采购订单(包含订单和送货计划)
     */
    public void syncPurchaseOrder() {
        String lockKey = BisSyncConstants.SYNC_LOCK_PREFIX_KEY + BisSyncConstants.PURCHASE_ORDER_SYNC_ERP_TASK;
        LockWrapper lockWrapper = new LockWrapper().setKey(lockKey).setWaitTime(0).setLeaseTime(10).setUnit(TimeUnit.MINUTES);
        distributedLock.tryLock( lockWrapper, () ->{
            this.syncOrder();
            this.syncDeliveryPlan();
            return "同步成功";
        },() -> {
            throw new RuntimeException("正在同步中，请稍后！");
        } );

    }

    /**
     * 试点供应商同步采购订单
     * @param experimentSupplier
     */
    public void experimentSupplierSyncPurchaseOrder(ExperimentSupplier experimentSupplier) {

        CompletableFuture<Void> orderAndDeliverPlanFuture = CompletableFuture.runAsync(() -> {
            try {
                this.experimentSupplierSyncOrderHandle(experimentSupplier);
            } catch (Exception ex) {
                XxlJobHelper.log("[experimentSupplierSyncOrderHandle] sync error!");
                XxlJobHelper.log(ex);
            }
            try {
                this.experimentSupplierSyncDeliveryHandle(experimentSupplier);
            } catch (Exception ex) {
                XxlJobHelper.log("[experimentSupplierSyncDeliveryHandle] sync error!");
                XxlJobHelper.log(ex);
            }
        });

        // 甲供物料订单同步
        CompletableFuture<Void> oemOrderFuture = CompletableFuture.runAsync(() -> {
            try {
                this.experimentSupplierSyncOemOrderHandle(experimentSupplier);
            } catch (Exception ex) {
                XxlJobHelper.log("[experimentSupplierSyncOemOrderHandle] sync error!");
                XxlJobHelper.log(ex);
            }
        });

        CompletableFuture.allOf( orderAndDeliverPlanFuture, oemOrderFuture ).join();

    }

    /**
     * 试点供应商同步送货计划处理
     * @param experimentSupplier
     */
    private void experimentSupplierSyncDeliveryHandle(ExperimentSupplier experimentSupplier) {
        Date startDate = new Date();
        String supplierCode = experimentSupplier.getSupplierCode();
        String syncLastDataTimeKey = supplierCode +"_" + BisSyncConstants.DELIVERY_PLAN_SYNC_ERP_TASK;
        String syncLastCloseDateTimeKey = supplierCode + "_" + BisSyncConstants.DELIVERY_PLAN_SYNC_ERP_TASK_BY_CLOSE_DATE;
        List<String> allBillNoList = this.getAllBillNoListWithSupplierCode(supplierCode, syncLastDataTimeKey,syncLastCloseDateTimeKey);
        if (CollectionUtils.isEmpty(allBillNoList)) {
            log.warn("[PurchaseOrder] syncData allBillNoSet is empty ,current sync data fail !");
            return;
        }

        this.handleSync( allBillNoList, billNoEntryMap -> {
            List<DeliveryPlanDTO> deliveryPlanDtoList = this.handleDeliveryPlan(billNoEntryMap);
            return deliveryPlanFeignClient.receiveDeliveryPlans(deliveryPlanDtoList);
        },() ->{
            RMap<String, Date> syncDataTimeMap = redissonClient.getMap( BisSyncConstants.TASK_SYNC_DATA_TIME_MAP_KEY, JsonJacksonCodec.INSTANCE);
            // 更新回时间
            syncDataTimeMap.fastPut( syncLastDataTimeKey  ,startDate );
        } );
    }

    /**
     * 试点供应商同步订单处理
     * @param experimentSupplier
     */
    private void experimentSupplierSyncOrderHandle(ExperimentSupplier experimentSupplier) {
        Date startDate = new Date();
        String supplierCode = experimentSupplier.getSupplierCode();
        String syncLastDataTimeKey = supplierCode +"_" + BisSyncConstants.ORDER_SYNC_ERP_TASK;
        String syncLastCloseDateTimeKey = supplierCode +"_" + BisSyncConstants.ORDER_SYNC_ERP_TASK_BY_CLOSE_DATE;
        List<String> allBillNoList = this.getAllBillNoListWithSupplierCode(supplierCode, syncLastDataTimeKey,syncLastCloseDateTimeKey);
        if (CollectionUtils.isEmpty(allBillNoList)) {
            log.warn("[PurchaseOrder] syncData allBillNoSet is empty ,current sync data fail !");
            return;
        }

        this.handleSync( allBillNoList, billNoEntryMap -> {
            List<OrderErpDTO> orderErpList = this.handlePurchaseOrder(billNoEntryMap);
            return zcOrderServiceFeignClient.syncErp(orderErpList);
        },() ->{
            RMap<String, Date> syncDataTimeMap = redissonClient.getMap( BisSyncConstants.TASK_SYNC_DATA_TIME_MAP_KEY, JsonJacksonCodec.INSTANCE);
            // 更新回时间
            syncDataTimeMap.fastPut( syncLastDataTimeKey  ,startDate );
        } );


    }

    /**
     * oem试点供应商同步oem订单处理
     * @param experimentSupplier
     */
    private void experimentSupplierSyncOemOrderHandle(ExperimentSupplier experimentSupplier) {
        Date startDate = new Date();
        String supplierCode = experimentSupplier.getSupplierCode();
        String syncLastDataTimeKey = supplierCode +"_" + BisSyncConstants.OEM_ORDER_SYNC_ERP_TASK;
        //关闭时间段,先用着试点采购订单的
        String syncLastCloseDateTimeKey = supplierCode +"_" + BisSyncConstants.ORDER_SYNC_ERP_TASK_BY_CLOSE_DATE;
        List<String> allBillNoList = this.getAllOemBillNoListWithSupplierCode(supplierCode, syncLastDataTimeKey,syncLastCloseDateTimeKey);
        if (CollectionUtils.isEmpty(allBillNoList)) {
            log.warn("[PurchaseOrder] syncData allBillNoSet is empty ,current sync data fail !");
            return;
        }

        this.handleSync( allBillNoList, billNoEntryMap -> {
            List<OemPurchaseOrderVo> oemPurchaseOrderVoList = this.handleOemPurchaseOrder(billNoEntryMap);
            //List<OemReceivingAddressVo> oemReceivingAddressVos = this.handleOemAddress(billNoEntryMap);
            return oemPurchaseOrderFeignClient.syncOemPurchaseOrder(oemPurchaseOrderVoList);
            //R<String> addressR = oemReceivingAddressFeignClient.syncOemReceivingAddress(oemReceivingAddressVos);
        },() ->{
            RMap<String, Date> syncDataTimeMap = redissonClient.getMap( BisSyncConstants.TASK_SYNC_DATA_TIME_MAP_KEY, JsonJacksonCodec.INSTANCE);
            // 更新回时间
            syncDataTimeMap.fastPut( syncLastDataTimeKey  ,startDate );
        } );


    }

    /**
     * 初始化供应商的订单和送货计划
     * @param experimentSupplier
     */
    public void initExperimentSupplierSyncPurchaseOrder(ExperimentSupplier experimentSupplier) {
        try {
            String supplierCode = experimentSupplier.getSupplierCode();
            Date initDate = new SimpleDateFormat(net.bncloud.common.util.DateUtil.PATTERN_DATETIME).parse("2022-01-01 00:00:00");
            QueryCondition queryCondition = QueryCondition.build( SupplierChainConstants.PUR_PURCHASEORDER)
                    .select("FBillNo")
                    // 关闭状态 未关闭 需要其他状态枚举找产品
                    .eq("FCloseStatus", "A")
                    // 小于某个时间
                    .or( qc -> qc.gt( "FApproveDate",initDate ).gt("FModifyDate",initDate)  )
                    // 限制供应商
                    .eq( "FSupplierId.fnumber",supplierCode );

            Set<String> allBillNoSet = new HashSet<>();
            segmentedQueryManager.whileHandle( queryCondition, 1000,PurchaseOrderVo.class, purchaseOrderVoList -> {
                Set<String> billNoSet = purchaseOrderVoList.stream().map(PurchaseOrderVo::getFBillNo).collect( Collectors.toSet());
                allBillNoSet.addAll( billNoSet );
            } );
            if (CollectionUtils.isEmpty(allBillNoSet)) {
                log.warn("[PurchaseOrder] syncData allBillNoSet is empty ,current sync data fail !");
                return;
            }

            ArrayList<String> allBillNoList = new ArrayList<>(allBillNoSet);

            this.handleSync( allBillNoList, billNoEntryMap -> {
                // 同步订单
                try {
                    List<OrderErpDTO> orderErpList = this.handlePurchaseOrder(billNoEntryMap);
                    zcOrderServiceFeignClient.syncErp(orderErpList);
                } catch (Exception ex){
                    log.error("[initExperimentSupplierSyncPurchaseInStockOrderAndPurchaseOrder] syncErpOrder error!",ex);
                }

                // 同步送货计划
                try {
                    List<DeliveryPlanDTO> deliveryPlanDtoList = this.handleDeliveryPlan(billNoEntryMap);
                    deliveryPlanFeignClient.receiveDeliveryPlans(deliveryPlanDtoList);
                } catch (Exception ex){
                    log.error("[initExperimentSupplierSyncPurchaseInStockOrderAndPurchaseOrder] receiveDeliveryPlans error!",ex);
                }

                return R.success();
            },() -> log.info("[initExperimentSupplierSyncPurchaseInStockOrderAndPurchaseOrder] finish!"));


        }catch (Exception ex){
            log.error("[initExperimentSupplierSyncPurchaseInStockOrderAndPurchaseOrder] 查询供应商的历史订单数据的编号失败!",ex);
        }

    }


}
