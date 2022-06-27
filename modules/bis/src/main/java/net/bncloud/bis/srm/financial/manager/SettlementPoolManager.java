package net.bncloud.bis.srm.financial.manager;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.bis.constant.BisSyncConstants;
import net.bncloud.bis.manager.ExperimentSupplierManager;
import net.bncloud.bis.manager.SegmentedQueryManager;
import net.bncloud.bis.properties.ApplicationProperties;
import net.bncloud.bis.srm.financial.model.erp.SettlementPoolSync;
import net.bncloud.bis.srm.financial.model.erp.SettlementPoolUpdate;
import net.bncloud.common.api.R;
import net.bncloud.common.api.ResultCode;
import net.bncloud.common.exception.ApiException;
import net.bncloud.common.util.DateTimeUtil;
import net.bncloud.common.util.StringUtil;
import net.bncloud.msk3cloud.constant.formid.FinancialConstants;
import net.bncloud.msk3cloud.core.condition.QueryCondition;
import net.bncloud.msk3cloud.core.condition.SaveCondition;
import net.bncloud.msk3cloud.core.params.K3CloudSaveParam;
import net.bncloud.msk3cloud.kingdee.K3cloudRemoteService;
import net.bncloud.msk3cloud.util.FieldKeyAnoUtils;
import net.bncloud.service.api.delivery.dto.DeliveryNoteDTO;
import net.bncloud.service.api.delivery.feign.SupplierDeliveryNoteFeignClient;
import net.bncloud.service.api.file.dto.FinancialCostBillDto;
import net.bncloud.service.api.file.dto.FinancialCostBillLineDto;
import net.bncloud.service.api.file.dto.FinancialDeliveryBillDto;
import net.bncloud.service.api.file.dto.FinancialDeliveryBillLineDto;
import net.bncloud.service.api.file.feign.FinancialFeignClient;
import net.bncloud.service.api.platform.purchaser.feign.PurchaserFeignClient;
import org.apache.commons.collections4.CollectionUtils;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static net.bncloud.bis.srm.financial.enums.SettlementPoolDocumentTypeEnum.*;

/**
 * @author: liulu
 * @Date: 2022-02-17 16:07
 */
@Slf4j
@Component
//@EnableScheduling
public class SettlementPoolManager {

    @Autowired
    private RedissonClient redissonClient;

    @Resource
    private FinancialFeignClient financialFeignClient;

    @Resource
    private PurchaserFeignClient purchaserFeignClient;

    @Resource
    private SupplierDeliveryNoteFeignClient supplierDeliveryNoteFeignClient;

    @Autowired
    private K3cloudRemoteService k3cloudRemoteService;

    @Autowired
    private SegmentedQueryManager segmentedQueryManager;

    @Autowired
    private ExperimentSupplierManager experimentSupplierManager;

    @Autowired
    private ApplicationProperties applicationProperties;
/*    @Scheduled(cron = "0/30 * * * * ?")
    public void testCall(){
        updateStatementStatus(Arrays.asList(119020L));
    }*/


    public void updateStatementStatus(List<Long> ids){
        log.info("更新ERP应付单对账状态：{}",ids);
        if(CollectionUtils.isEmpty(ids)){
            return;
        }
        for(Long id : ids){
            SettlementPoolUpdate update = new SettlementPoolUpdate();
            update.setFid(id);
            update.setFSrmDzzt("已对账");

            K3CloudSaveParam<SettlementPoolUpdate> k3CloudSaveParam = SaveCondition.build(update)
                    .needReturnField(FieldKeyAnoUtils.getFieldKeyList(SettlementPoolUpdate.class))
                    .needUpdateField(Arrays.asList("FID","F_srm_dzzt"))
                    .k3CloudSaveParam();

            try {
                SettlementPoolUpdate saveOrUpdateWithReturnObj = k3cloudRemoteService.saveOrUpdateWithReturnObj(FinancialConstants.AP_PAYABLE, k3CloudSaveParam, SettlementPoolUpdate.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void syncSettlementPoolData() {
        Date startDate = new Date();
        R<List<String>> purchaserCodeList = purchaserFeignClient.queryAllPurchaser();

        List<String> supplierCodeList = new ArrayList<>();
        experimentSupplierManager.doGetExperimentSupplierAfterExecute(null,experimentSupplier -> supplierCodeList.add(experimentSupplier.getSupplierCode()));
        // 获取上一次同步的时间蹉
        RMap<String, Date> syncDataTimeMap = redissonClient.getMap( BisSyncConstants.TASK_SYNC_DATA_TIME_MAP_KEY, JsonJacksonCodec.INSTANCE);
        Date lastSyncDataTime = syncDataTimeMap.getOrDefault(BisSyncConstants.SETTLEMENT_POOL_SYNC_ERP_TASK, applicationProperties.getSchedulingTask().getDefaultSyncDateTime(BisSyncConstants.SETTLEMENT_POOL_SYNC_ERP_TASK));

        Class<SettlementPoolSync> settlementPoolClass = SettlementPoolSync.class;
        List<String> fieldKeyList = FieldKeyAnoUtils.getFieldKeyList( settlementPoolClass);
        QueryCondition queryCondition = QueryCondition.build( FinancialConstants.AP_PAYABLE)
                .select(fieldKeyList)
                // 单据状态 已审核 需要其他状态枚举找产品
                .eq( "FDocumentStatus","C" )
                .ne("F_srm_dzzt","已对账")
                .or( qc -> qc.gt( "FApproveDate",lastSyncDataTime ).gt("FModifyDate",lastSyncDataTime)  )
                .in(CollectionUtils.isNotEmpty(supplierCodeList),"FSUPPLIERID.fnumber",supplierCodeList)
                .in(CollectionUtils.isNotEmpty(purchaserCodeList.getData()),"FPURCHASEORGID.fnumber",purchaserCodeList.getData());
        executeSync(settlementPoolClass, queryCondition);

        syncDataTimeMap.fastPut( BisSyncConstants.SETTLEMENT_POOL_SYNC_ERP_TASK,startDate );
    }

    /**
     * 组装送货单信息
     * @param settlementPoolSyncList
     * @return
     */
    private List<FinancialDeliveryBillDto> wrapperFinancialDeliveryBill(List<SettlementPoolSync> settlementPoolSyncList){
        List<FinancialDeliveryBillDto> collect = new ArrayList<>();
        Set<String> billNoSet = new HashSet<>();
        settlementPoolSyncList.forEach(item -> {
            if(billNoSet.contains(item.getFBillNo())) {
                return;
            }
            FinancialDeliveryBillDto bill = toFinancialDeliveryBillDto(item);

            //判断金蝶的收料通知单号是否为空，为空就不关联送货单号
            if(!StringUtil.isEmpty(item.getFAbcText())){
                Map<String,String> params=new HashMap<>();
                params.put("fNumber",item.getFAbcText());
                R<DeliveryNoteDTO> result = supplierDeliveryNoteFeignClient.getDeliveryNoteNo(JSON.toJSONString(params));
                if(result.isSuccess()){
                    if(result.getData()!=null){
                        bill.setDeliveryBillNo(result.getData().getDeliveryNo());
                        bill.setDeliveryDate(result.getData().getEstimatedTime());
                    }
                }else{
                    throw new ApiException(ResultCode.FAILURE.getCode(),"对账模块的同步结算池功能调用送货服务的查询送货单号失败！");
                }
            }
            List<FinancialDeliveryBillLineDto> billLineList = new ArrayList<>();
            Integer deliveryNum = 0;
            for(SettlementPoolSync line : settlementPoolSyncList){
                if(item.getFBillNo().equals(line.getFBillNo())){
                    billLineList.add(toFinancialDeliveryBillLineDto(line));
                    deliveryNum += line.getFPriceQty();
                }
            }
            bill.setDeliveryNum(deliveryNum);
            bill.setDeliveryBillLineList(billLineList);
            collect.add(bill);
            billNoSet.add(item.getFBillNo());
        });
        return collect;
    }

    private FinancialDeliveryBillDto toFinancialDeliveryBillDto(SettlementPoolSync settlementPoolSync){
        FinancialDeliveryBillDto bill = new FinancialDeliveryBillDto();
        bill.setErpBillId(settlementPoolSync.getFid());
        bill.setCustomerCode(settlementPoolSync.getFPurchaseOrgCode());
        bill.setCustomerName(settlementPoolSync.getFPurchaseOrgName());
        bill.setSupplierCode(settlementPoolSync.getFSupplierCode());
        bill.setSupplierName(settlementPoolSync.getFSupplierName());
        bill.setLastModifiedDate(settlementPoolSync.getFModifyDate());
        bill.setErpBillNo(settlementPoolSync.getFBillNo());
        bill.setSettlementPoolSyncStatus("Y");
        bill.setErpBillType(settlementPoolSync.getFBillTypeName());
        bill.setCurrencyCode(settlementPoolSync.getFCurrencyCode());
        bill.setCurrencyName(settlementPoolSync.getFCurrencyName());
        bill.setDeliveryAmount(settlementPoolSync.getFAllAmountFor());
        bill.setSourceType("purchase");
        bill.setRemark(settlementPoolSync.getFRemark());
        bill.setSigningTime(DateTimeUtil.parseDate(settlementPoolSync.getFDate()));
        return bill;
    }

    private FinancialDeliveryBillLineDto toFinancialDeliveryBillLineDto(SettlementPoolSync settlementPoolSync){
        FinancialDeliveryBillLineDto billLine = new FinancialDeliveryBillLineDto();
        billLine.setInstockNo(settlementPoolSync.getFInstockId());
        billLine.setErpLineId(settlementPoolSync.getFDetailId().toString());
        billLine.setTaxIncludedUnitPrice(settlementPoolSync.getFTaxPrice());
        billLine.setTaxIncludedAmount(settlementPoolSync.getFAllAmountFor_d());
        billLine.setNotTaxAmount(settlementPoolSync.getFNoTaxAmountFor_d());
        billLine.setTaxAmount(settlementPoolSync.getFTaxAmountFor_d());
        BigDecimal fEntryTaxRate = settlementPoolSync.getFEntryTaxRate();
        billLine.setTaxRate(fEntryTaxRate);
        boolean includeTax = Objects.nonNull(fEntryTaxRate) && fEntryTaxRate.compareTo(BigDecimal.ZERO) > 0;
        billLine.setHaveTax(includeTax);
        return billLine;
    }

    /**
     * 组装费用单信息
     * @param settlementPoolSyncList
     * @return
     */
    private List<FinancialCostBillDto> wrapperFinancialCostBill(List<SettlementPoolSync> settlementPoolSyncList){
        List<FinancialCostBillDto> collect = new ArrayList<>();
        Set<String> billNoSet = new HashSet<>();
        settlementPoolSyncList.forEach(item -> {
            if(billNoSet.contains(item.getFBillNo())) {
                return;
            }
            FinancialCostBillDto bill = toFinancialCostBillDto(item);
            List<FinancialCostBillLineDto> billLineList = new ArrayList<>();
            settlementPoolSyncList.forEach(line ->{
                if(item.getFBillNo().equals(line.getFBillNo())){
                    billLineList.add(toFinancialCostBillLineDto(line));
                }
            });
            bill.setCostBillLineList(billLineList);
            collect.add(bill);
            billNoSet.add(item.getFBillNo());
        });
        return collect;
    }

    private FinancialCostBillDto toFinancialCostBillDto(SettlementPoolSync settlementPoolSync){
        FinancialCostBillDto bill = new FinancialCostBillDto();
        bill.setErpBillId(settlementPoolSync.getFid());
        bill.setCustomerCode(settlementPoolSync.getFPurchaseOrgCode());
        bill.setCustomerName(settlementPoolSync.getFPurchaseOrgName());
        bill.setSupplierCode(settlementPoolSync.getFSupplierCode());
        bill.setSupplierName(settlementPoolSync.getFSupplierName());
        bill.setLastModifiedDate(settlementPoolSync.getFModifyDate());
        bill.setErpBillNo(settlementPoolSync.getFBillNo());
        bill.setErpBillType(settlementPoolSync.getFBillTypeName());
        bill.setCostBillType("0");
        bill.setRemark(settlementPoolSync.getFRemark());
        bill.setSettlementPoolSyncStatus("Y");
        bill.setPublishTime(DateTimeUtil.parseDate(settlementPoolSync.getFCreateDate()));
        bill.setPublisher(settlementPoolSync.getFCreatorName());
        bill.setConfirmTime(DateTimeUtil.parseDate(settlementPoolSync.getFApproveDate()));
        bill.setConfirmName(settlementPoolSync.getFApproverName());
        bill.setCurrencyCode(settlementPoolSync.getFCurrencyCode());
        bill.setCurrencyName(settlementPoolSync.getFCurrencyName());
        bill.setAllAmount(settlementPoolSync.getFAllAmountFor());
        bill.setSourceType("purchase");
        return bill;
    }

    private FinancialCostBillLineDto toFinancialCostBillLineDto(SettlementPoolSync settlementPoolSync){
        FinancialCostBillLineDto billLine = new FinancialCostBillLineDto();
        billLine.setErpLineId(settlementPoolSync.getFDetailId().toString());
        billLine.setCostName(settlementPoolSync.getFCostName());
        billLine.setCostReason(settlementPoolSync.getFRemark());
        billLine.setErpCostId(settlementPoolSync.getFCostId());
        billLine.setValuationNum(settlementPoolSync.getFPriceQty());
        billLine.setTaxIncludedUnitPrice(settlementPoolSync.getFTaxPrice());
        billLine.setTaxIncludedAmount(settlementPoolSync.getFAllAmountFor_d());
        billLine.setNotTaxAmount(settlementPoolSync.getFNoTaxAmountFor_d());
        billLine.setTaxAmount(settlementPoolSync.getFTaxAmountFor_d());
        BigDecimal fEntryTaxRate = settlementPoolSync.getFEntryTaxRate();
        billLine.setTaxRate(fEntryTaxRate);
        boolean includeTax = Objects.nonNull(fEntryTaxRate) && fEntryTaxRate.compareTo(BigDecimal.ZERO) > 0;
        billLine.setHaveTax(includeTax);
        return billLine;
    }

    /**
     * 初始化供应商对账单
     * @param supplierCodeStr
     */
    public void initSupplierSettlementPoolSyncErpTask(String supplierCodeStr) {
        R<List<String>> purchaserCodeList = purchaserFeignClient.queryAllPurchaser();
        experimentSupplierManager.doGetExperimentSupplierAfterExecute(supplierCodeStr,experimentSupplier -> {

            Class<SettlementPoolSync> settlementPoolClass = SettlementPoolSync.class;
            List<String> fieldKeyList = FieldKeyAnoUtils.getFieldKeyList( settlementPoolClass);
            QueryCondition queryCondition = QueryCondition.build( FinancialConstants.AP_PAYABLE)
                    .select(fieldKeyList)
                    // 单据状态 已审核 需要其他状态枚举找产品
                    .eq( "FDocumentStatus","C" )
                    .ne("F_srm_dzzt","已对账")
                    .or( qc -> qc.gt( "FApproveDate","2022-01-01 00:00:00" ).gt("FModifyDate","2022-01-01 00:00:00")  )
                    .eq("FSUPPLIERID.fnumber", experimentSupplier.getSupplierCode() )
                    .in(CollectionUtils.isNotEmpty(purchaserCodeList.getData()),"FPURCHASEORGID.fnumber",purchaserCodeList.getData());

            this.executeSync(settlementPoolClass, queryCondition);
        });


    }

    private void executeSync(Class<SettlementPoolSync> settlementPoolClass, QueryCondition queryCondition) {
        segmentedQueryManager.doWhile( queryCondition, null,null, 1000, settlementPoolClass, settlementPoolSync ->{
            //组装送货单
            List<SettlementPoolSync> deliveryBillCollect = settlementPoolSync
                    .stream()
                    .filter(item -> YFD01_SYS.equals(valueOf(item.getFBillTypeNumber())))
                    .collect(Collectors.toList());
            List<FinancialDeliveryBillDto> deliveryBillList = wrapperFinancialDeliveryBill(deliveryBillCollect);
            financialFeignClient.batchSaveDeliveryBill(deliveryBillList);

            //组装费用单
            List<SettlementPoolSync> costBillCollect = settlementPoolSync
                    .stream()
                    .filter(item -> YFD02_SYS.equals(valueOf(item.getFBillTypeNumber())))
                    .collect(Collectors.toList());
            List<FinancialCostBillDto> costBillList = wrapperFinancialCostBill(costBillCollect);
            financialFeignClient.batchSaveCostBill(costBillList);
        });
    }
}
