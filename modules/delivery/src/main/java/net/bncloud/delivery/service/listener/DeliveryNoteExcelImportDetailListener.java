package net.bncloud.delivery.service.listener;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.common.exception.ApiException;
import net.bncloud.common.exception.Asserts;
import net.bncloud.common.security.SecurityUtils;
import net.bncloud.common.util.CollectionUtil;
import net.bncloud.delivery.entity.DeliveryNote;
import net.bncloud.delivery.entity.DeliveryNoteExcelImportDetailVo;
import net.bncloud.delivery.entity.DeliveryPlan;
import net.bncloud.delivery.entity.DeliveryPlanDetail;
import net.bncloud.delivery.service.DeliveryPlanDetailItemService;
import net.bncloud.delivery.utils.DeliveryDetailUtils;
import net.bncloud.delivery.vo.ShippableDeliveryPlanDetailItemVo;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 送货单Excel导入明细监听
 * @author ddh
 * @version 1.0.0
 * @description
 * @since 2022/3/8
 */
@Slf4j
public class DeliveryNoteExcelImportDetailListener extends AnalysisEventListener<DeliveryNoteExcelImportDetailVo> {

    /**
     * 校验表头
     */
    private final static String HEAD_VALID_STR = "{0=产品编码, 1=产品名称, 2=条码, 3=物料分类(编码), 4=批次, 5=实际送货数量, 6=订单类型(编码), 7=入库仓(编码), 8=备注}";
    /**
     * 起始行
     */
    private final static Integer START_ROW = 2;

    private final Validator validator;

    /**
     * 物料产品验重
     */
    private final Map<String,String> productCodeMap = new HashMap<>();

    /**
     * 物料分类重
     */
    private final Set<String> materialTypeCheck = new HashSet<>();

    /**
     * 订单类型去重
     */
    private final Set<String> orderTypeCheck = new HashSet<>();

    /**
     * 入库仓验重
     */
    private final Set<String> warehouseCheck = new HashSet<>();


    private final Map<String, String> merchantCodeProductCodeMap;
    private final Set<String> merchantCodeSet;
    private final Set<String> productCodeSet;
    private final Set<String> materialClassificationMap;
    private final Set<String> orderTypeMap;
    private final Set<String> allWarehouseSet;

    /**
     * 剩余可发货数量存储桶 --> 减少查询
     */
    private final Map<String,Integer> productCodeRemainingQuantityTotalMap = new HashMap<>();

    /**
     * 超出比例
     */
    private final BigDecimal ratioOfSentProportion;

    /**
     * 当前送货单信息
     */
    private final DeliveryNote deliveryNote;

    private final DeliveryPlanDetailItemService deliveryPlanDetailItemService;


    /**
     * 存放excel解析到的项次数据
     */
    private final Map<String,List<ShippableDeliveryPlanDetailItemVo>> productCodeShippableDeliveryPlanDetailItemListMap = new HashMap<>();

    /**
     * 新的构造器
     * @param validator
     * @param ratioOfSentProportion
     * @param deliveryPlanDetailItemService
     * @param deliveryNote
     * @param productCodeMerchantCodeList
     * @param deliveryPlanList
     * @param allWarehouseSet
     */
    public DeliveryNoteExcelImportDetailListener(Validator validator,
                                                 BigDecimal ratioOfSentProportion,
                                                 DeliveryPlanDetailItemService deliveryPlanDetailItemService,
                                                 DeliveryNote deliveryNote,
                                                 List<DeliveryPlanDetail> productCodeMerchantCodeList,
                                                 List<DeliveryPlan> deliveryPlanList,
                                                 Set<String> allWarehouseSet) {

        this.validator = validator;
        this.ratioOfSentProportion = ratioOfSentProportion;
        this.deliveryPlanDetailItemService = deliveryPlanDetailItemService;
        this.deliveryNote = deliveryNote;

        this.merchantCodeProductCodeMap = productCodeMerchantCodeList.stream().filter(deliveryPlanDetail -> StrUtil.isNotBlank(deliveryPlanDetail.getMerchantCode())).collect(Collectors.toMap(DeliveryPlanDetail::getMerchantCode, DeliveryPlanDetail::getProductCode));
        this.merchantCodeSet = productCodeMerchantCodeList.stream().filter(deliveryPlanDetail -> StrUtil.isNotBlank(deliveryPlanDetail.getMerchantCode())).map(DeliveryPlanDetail::getMerchantCode).collect(Collectors.toSet());
        this.productCodeSet = productCodeMerchantCodeList.stream().filter(deliveryPlanDetail -> StrUtil.isNotBlank(deliveryPlanDetail.getProductCode())).map(DeliveryPlanDetail::getProductCode).collect(Collectors.toSet());

        this.materialClassificationMap = deliveryPlanList.stream().map(DeliveryPlan::getMaterialClassification).collect(Collectors.toSet());
        this.orderTypeMap = deliveryPlanList.stream().map(DeliveryPlan::getOrderType).collect(Collectors.toSet());
        this.allWarehouseSet = allWarehouseSet;

    }

    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        // 校验模板格式
        log.info("excel head map: {}",headMap);
//        Asserts.isTrue(HEAD_VALID_STR.equals( headMap.toString() ),"Excel模板不匹配！" );

    }

    /**
     a、产品编号无法匹配，请确认该产品数据是否正确
     b、条码无法匹配，请确认该条码数据是否正确
     c、X产品剩余可发数量不足，请重新调整实际发货数量
     d、X产品存在多条数据，请检查导入数据，确保X产品的只有一条数据 （330优化后不生效）
     e、填写的物料分类必须一致
     f、填写的订单类型必须一致
     g、填写的入货仓必须一致
     h、物料分类填写错误，请填写正确的物料分类编码
     i、订单类型填写错误，请填写正确的订单类型编码
     j、入货仓填写错误，请填写正确的入货仓编码

     330导入优化:需支持Excel文档填写多行相同的产品但是不同的批次  ，因此，d 限制放开
     第一行：产品AAA，实际送货数量为25，批次A001；
     第二行：产品AAA，实际送货数量为25，批次A002；
     第三行：产品AAA，实际送货数量为25，批次A003；
     ...
     *
     * @param deliveryNoteExcelImportDetailVo
     * @param context
     */
    @Override
    public void invoke(DeliveryNoteExcelImportDetailVo deliveryNoteExcelImportDetailVo, AnalysisContext context) {

        if(
                StrUtil.isBlank( deliveryNoteExcelImportDetailVo.getItemNo() ) && StrUtil.isBlank( deliveryNoteExcelImportDetailVo.getMaterialType() )  &&
                StrUtil.isBlank( deliveryNoteExcelImportDetailVo.getOrderType() ) && StrUtil.isBlank( deliveryNoteExcelImportDetailVo.getProductCode() ) && StrUtil.isBlank( deliveryNoteExcelImportDetailVo.getProductName() ) &&
                StrUtil.isBlank( deliveryNoteExcelImportDetailVo.getRemark() ) && StrUtil.isBlank( deliveryNoteExcelImportDetailVo.getWarehouse() ) && deliveryNoteExcelImportDetailVo.getRealDeliveryQuantity() == null
        ){
            return;
        }

        // 行号
        int rowNo = context.readRowHolder().getRowIndex() + 1 ;
        log.info("rowNo:{},data:{}",rowNo,deliveryNoteExcelImportDetailVo);

        // 校验必填项
        String msg = "第 "+rowNo+" 行，数据不符合要求；";

        Set<ConstraintViolation<DeliveryNoteExcelImportDetailVo>> constraintViolations = validator.validate( deliveryNoteExcelImportDetailVo);
        if(! constraintViolations.isEmpty()){
            String violationMsg = constraintViolations.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(","));
            msg += violationMsg;
            throw new ApiException(msg);
        }

        String merchantCode = deliveryNoteExcelImportDetailVo.getMerchantCode();
        String productCode = deliveryNoteExcelImportDetailVo.getProductCode();
        String orderType = deliveryNoteExcelImportDetailVo.getOrderType();
        String materialType = deliveryNoteExcelImportDetailVo.getMaterialType();
        String warehouse = deliveryNoteExcelImportDetailVo.getWarehouse();
        Long realDeliveryQuantity = deliveryNoteExcelImportDetailVo.getRealDeliveryQuantity();

        String supplierCode = SecurityUtils.getCurrentSupplier().orElseThrow(() -> new ApiException("获取当前登录的供应商Code失败！")).getSupplierCode();

        // 要求物料分类、订单类型、入货仓一致
        this.materialTypeCheck.add( materialType );
        this.orderTypeCheck.add( orderType );
        this.warehouseCheck.add( warehouse );

        //e、f、g
        Asserts.isTrue( this.materialTypeCheck.size() == 1,"填写的物料分类必须一致！" );
        Asserts.isTrue( this.orderTypeCheck.size() == 1,"填写的订单类型必须一致！" );
        Asserts.isTrue( this.warehouseCheck.size() == 1,"填写的入货仓必须一致！");

        // h、i、j
        Asserts.isTrue( this.materialClassificationMap.contains( materialType ),"物料分类填写错误，请填写正确的物料分类编码！" );
        Asserts.isTrue( this.orderTypeMap.contains( orderType ),"订单类型填写错误，请填写正确的订单类型编码！" );
        Asserts.isTrue( this.allWarehouseSet.contains( warehouse ),"入货仓填写错误，请填写正确的入货仓编码！");

        // 条码不为空
        /*if( StrUtil.isNotBlank( merchantCode )){
            //b
            Asserts.isTrue( this.merchantCodeSet.contains( merchantCode ),msg + "条码无法匹配，请确认该条码数据是否正确！" );
            // 当前行数据的 产品编码
            productCode = this.merchantCodeProductCodeMap.get( merchantCode);
            Asserts.notNull( productCode,msg  + "条码无法匹配物料编码，请确认该条码数据是否正确！" );
            //d
//            Asserts.isNull( productCodeMap.putIfAbsent( productCode,productCode ),msg + "依据条码找到物料编码"+ doGetProductCode +"发现在excel中重复了，请检查导入数据，确保只有一条数据！" );
            // 此次替换了 excel中的 物料编码
            deliveryNoteExcelImportDetailVo.setProductCode( productCode );

        }
        // 条码为空！
        else{
            Asserts.isTrue( StrUtil.isNotBlank( productCode ),msg + "产品编码、条码必须填写一个！" );
            // a
            Asserts.isTrue( this.productCodeSet.contains( productCode),"产品编号无法匹配，请确认该产品数据是否正确！" );
            //d
//            Asserts.isNull( this.productCodeMap.putIfAbsent( productCode,productCode ),msg + productCode + "产品存在多条数据，请检查导入数据，确保只有一条数据！" );
        }*/

        //c 查询项次数据 依据物料编码+订单类型+物料分类+入货仓+当前供应商+当前采购方
        // 获取此前存在
        Integer remainingQuantityTotal = this.productCodeRemainingQuantityTotalMap.get( deliveryNoteExcelImportDetailVo.getProductCode());

        // 如果没数值
        boolean realDeliveryQuantityIsNull = Objects.isNull( remainingQuantityTotal );
        if( realDeliveryQuantityIsNull ){
            List<ShippableDeliveryPlanDetailItemVo> shippableDeliveryPlanDetailItemList = deliveryPlanDetailItemService.getShippableDeliveryPlanDetailItemList( deliveryNoteExcelImportDetailVo.getProductCode(), orderType, materialType, warehouse, supplierCode, deliveryNote.getCustomerCode());
            Asserts.isTrue( CollectionUtil.isNotEmpty( shippableDeliveryPlanDetailItemList ) ,msg + "剩余可发数量不足！" );

            // 目前还是只考虑 int 类型  ，后续可能出现 半件啊，0.1件啊这种就有问题了
            remainingQuantityTotal = shippableDeliveryPlanDetailItemList.stream().mapToInt( shippableDeliveryPlanDetailItemVo -> Integer.parseInt(shippableDeliveryPlanDetailItemVo.getRemainingQuantity())).sum();
            // 存放
            this.productCodeRemainingQuantityTotalMap.put( deliveryNoteExcelImportDetailVo.getProductCode(), remainingQuantityTotal);

            // 存放待处理的项次数据
            this.productCodeShippableDeliveryPlanDetailItemListMap.putIfAbsent( deliveryNoteExcelImportDetailVo.getProductCode(), shippableDeliveryPlanDetailItemList);

        }
        else {
            Asserts.isTrue( remainingQuantityTotal > 0,msg + "剩余可发数量不足！");
        }
        DeliveryDetailUtils.verifyRemainingShippableQuantityAccord( remainingQuantityTotal,this.ratioOfSentProportion,realDeliveryQuantity,msg + "剩余可发数量不足，请重新调整实际发货数量，当前可发货数量为"+ remainingQuantityTotal );
        Optional.ofNullable( this.productCodeRemainingQuantityTotalMap.get( deliveryNoteExcelImportDetailVo.getProductCode() )).ifPresent( value -> this.productCodeRemainingQuantityTotalMap.put( deliveryNoteExcelImportDetailVo.getProductCode() ,(value - realDeliveryQuantity.intValue() ) ) );




    }

    /**
     * 获取匹配数据
     * @return
     */
    public Map<String, List<ShippableDeliveryPlanDetailItemVo>> getProductCodeShippableDeliveryPlanDetailItemListMap() {
        return productCodeShippableDeliveryPlanDetailItemListMap;
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        log.info("excel parse finish! data:{} ", JSON.toJSONString( productCodeShippableDeliveryPlanDetailItemListMap ));
    }
}
