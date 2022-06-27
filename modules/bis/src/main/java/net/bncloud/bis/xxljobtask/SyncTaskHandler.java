package net.bncloud.bis.xxljobtask;

import cn.hutool.core.util.StrUtil;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import net.bncloud.bis.constant.BisSyncConstants;
import net.bncloud.bis.manager.*;
import net.bncloud.bis.srm.doc.manager.ContractFileManager;
import net.bncloud.bis.srm.financial.manager.SettlementPoolManager;
import net.bncloud.common.api.R;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

import static net.bncloud.bis.constant.BisSyncConstants.SYNC_MATERIAL_INFO_DATE_KEY;

/**
 * desc: 同步任务处理
 *    XxlJobHelper.log("xxxx");
 * @author Rao
 * @Date 2022/01/26
 **/
@Component
public class SyncTaskHandler {

    @Resource
    private SupplierManager supplierManager;
    @Resource
    private PurchaseOrderManager purchaseOrderManager;
    @Resource
    private PurchaseInStockOrderManager purchaseInStockOrderManager;
    @Resource
    private ContractFileManager contractFileManager;
    @Resource
    private ExperimentSupplierManager experimentSupplierManager;
    @Resource
    private SettlementPoolManager settlementPoolManager;
    @Resource
    private SyncMaterialManager syncMaterialManager;

    /**
     * 同步供应商档案
     * @return
     */
    @XxlJob(value = BisSyncConstants.SUPPLIER_SYNC_OA_TASK )
    public ReturnT<String> supplierSyncOaTask(){
        String params = XxlJobHelper.getJobParam();
        XxlJobHelper.log("supplierSyncOaTask params:{}",params);
        R<String> result = supplierManager.syncOaTableData(null);
        return new ReturnT<>(ReturnT.SUCCESS_CODE ,result.getMsg() );
    }

    /**
     * 采购订单同步（包含订单和送货计划）
     */
    //@XxlJob(BisSyncConstants.PURCHASE_ORDER_SYNC_ERP_TASK)
    @Deprecated
    public ReturnT<String> purchaseOrderSync(){
        String params = XxlJobHelper.getJobParam();
        XxlJobHelper.log("purchaseOrderSync params:{}",params);
        purchaseOrderManager.syncPurchaseOrder();
        return ReturnT.SUCCESS;
    }

    /**
     * 同步ERP采购入库单
     * @return
     */
    @XxlJob(value = BisSyncConstants.PURCHASE_IN_STOCK_ORDER_SYNC_ERP_TASK)
    public ReturnT<String> purchaseInStockOrderSyncErpTask(){
        String params = XxlJobHelper.getJobParam();
        XxlJobHelper.log("purchaseInStockOrderSyncErpTask params:{}",params);
        purchaseInStockOrderManager.syncData();
        return ReturnT.SUCCESS;
    }

    /**
     * 试点供应商同步采购订单
     * @return
     */
    @XxlJob(value = BisSyncConstants.EXPERIMENT_SUPPLIER_SYNC_PURCHASE_ORDER)
    public ReturnT<String> experimentSupplierSyncPurchaseOrder(){
        String params = XxlJobHelper.getJobParam();
        XxlJobHelper.log("params: {}",params);
        experimentSupplierManager.experimentSupplierSyncPurchaseOrder( params);
        return ReturnT.SUCCESS;
    }

    /**
     * 试点供应商同步采购入库订单
     * @return
     */
    @XxlJob(value = BisSyncConstants.EXPERIMENT_SUPPLIER_SYNC_IN_STOCK_ORDER)
    public ReturnT<String> experimentSupplierSyncInStockOrder(){
        String params = XxlJobHelper.getJobParam();
        XxlJobHelper.log("params: {}",params);
        experimentSupplierManager.experimentSupplierSyncInStockOrder( params);
        return ReturnT.SUCCESS;
    }

    /**
     * 初始化试点供应商采购订单
     *  desc： 同步2022-01-01 00:00:00 之前的未完成的数据
     * @return
     */
    @XxlJob(BisSyncConstants.INIT_EXPERIMENT_SUPPLIER_SYNC_PURCHASE_ORDER)
    public ReturnT<String> initExperimentSupplierSyncPurchaseOrder(){
        String params = XxlJobHelper.getJobParam();
        XxlJobHelper.log( "params:{}", params );
        experimentSupplierManager.initExperimentSupplierSyncPurchaseOrder( params );

        return ReturnT.SUCCESS;
    }

    /**
     * 同步ERP合同附件
     *
     * @return
     */
   /* @XxlJob(value = BisSyncConstants.CONTRACT_FILE_SYNC_ERP_TASK)
    public ReturnT<String> contractFileSyncErpTask(){
        XxlJobHelper.log("params: {}",params);
        contractFileManager.syncContractFile();
        return ReturnT.SUCCESS;
    }*/
    /**
     * 同步ERP合同和合同附件
     *
     * @return
     */
    @XxlJob(value = BisSyncConstants.CONTRACT_FILE_SYNC_ERP_TASK)
    public ReturnT<String> contractFileSyncErpTask(){
        String params = XxlJobHelper.getJobParam();
        XxlJobHelper.log("params: {}",params);
        contractFileManager.syncContractAndContractFile();
        return ReturnT.SUCCESS;
    }
    /**
     * 同步物料主数据
     */
    @XxlJob(value = SYNC_MATERIAL_INFO_DATE_KEY)
    public ReturnT<String> syncMaterialInfoDateKey(){
        String params = XxlJobHelper.getJobParam();
        XxlJobHelper.log( "params:{}", params );
        if(StringUtils.isEmpty(params)){
            params = "500";
        }
        syncMaterialManager.syncAllMaterialData(Integer.parseInt(params));
        return ReturnT.SUCCESS;
    }

    /**
     * 同步对账模块结算池数据
     *
     * @return
     */
    @XxlJob(value = BisSyncConstants.SETTLEMENT_POOL_SYNC_ERP_TASK)
    public ReturnT<String> settlementPoolSyncErpTask(){
        String params = XxlJobHelper.getJobParam();
        XxlJobHelper.log("params: {}",params);
        settlementPoolManager.syncSettlementPoolData();
        return ReturnT.SUCCESS;
    }

    /**
     * 初始化供应商对账模块结算池数据
     * @return
     */
    @XxlJob(BisSyncConstants.INIT_SUPPLIER_SETTLEMENT_POOL_SYNC_ERP_TASK)
    public ReturnT<String> initSupplierSettlementPoolSyncErpTask(){
        String params = XxlJobHelper.getJobParam();
        XxlJobHelper.log("initSupplierSettlementPoolSyncErpTask params: {}",params);
        if(StrUtil.isBlank( params )){
            XxlJobHelper.log("执行失败....");
            return new ReturnT<>(500,"执行时必须传递供应商编码参数");
        }
        settlementPoolManager.initSupplierSettlementPoolSyncErpTask( params );
        return ReturnT.SUCCESS;
    }

}
