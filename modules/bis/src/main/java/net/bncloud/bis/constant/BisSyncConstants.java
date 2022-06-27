package net.bncloud.bis.constant;

/**
 * desc: 同步常量
 *
 *  同步任务key 用于做 更新时间蹉key map。
 *
 * @author Rao
 * @Date 2022/01/18
 **/
public interface BisSyncConstants {

    /**
     * 供应商同步oa任务
     */
    String SUPPLIER_SYNC_OA_TASK = "supplierSyncOaTask";

    /**
     * 采购订单数据同步任务
     */
    String PURCHASE_ORDER_SYNC_ERP_TASK = "purchaseOrderSyncErpTask";

    /**
     * 订单同步
     */
    String ORDER_SYNC_ERP_TASK = "orderSyncErpTask";

    /**
     * oem订单同步
     */
    String OEM_ORDER_SYNC_ERP_TASK = "oemOrderSyncErpTask";

    /**
     * 送货计划同步
     */
    String DELIVERY_PLAN_SYNC_ERP_TASK = "deliveryPlanSyncErpTask";

    /**
     * 同步锁前缀key
     */
    String SYNC_LOCK_PREFIX_KEY = "SYNC_LOCK:";

    /**
     * 采购入库单数据同步任务
     */
    String PURCHASE_IN_STOCK_ORDER_SYNC_ERP_TASK = "purchaseInStockOrderSyncErpTask";

    /**
     * 计划订单同步任务
     */
    String PLAN_ORDER_SYNC_ERP_TASK = "planOrderSyncErpTask";

    //---------------------试点供应商

    /**
     * 试点供应商同步采购订单
     */
    String EXPERIMENT_SUPPLIER_SYNC_PURCHASE_ORDER = "experimentSupplierSyncPurchaseOrder";
    /**
     * 试点供应商同步采购入库单
     */
    String EXPERIMENT_SUPPLIER_SYNC_IN_STOCK_ORDER = "experimentSupplierSyncInStockOrder";

    /**
     * 计划订单同步 计划订单
     */
    String EXPERIMENT_SUPPLIER_SYNC_PLAN_ORDER = "PLAN_ORDER_SYNC_ERP_TASK_PLAN_ORDER";

    /**
     * 试点供应商初始化采购订单
     */
    String INIT_EXPERIMENT_SUPPLIER_SYNC_PURCHASE_ORDER = "initExperimentSupplierSyncPurchaseOrder";

    /**
     * 同步主物料时间
     */
    String SYNC_MATERIAL_INFO_DATE_KEY = "syncMaterialInfoDateKey";


    //------------------------ 采购订单同步时 需要新增的几个日期字段

    /**
     * 订单同步时 需要的关闭时间字段
     */
    String ORDER_SYNC_ERP_TASK_BY_CLOSE_DATE = ORDER_SYNC_ERP_TASK +"_" +"fCloseDate";

    /**
     * 送货计划同步时 需要的关闭时间字段
     */
    String DELIVERY_PLAN_SYNC_ERP_TASK_BY_CLOSE_DATE = DELIVERY_PLAN_SYNC_ERP_TASK + "_"+"fCloseDate";


    //--------------------------- redis key

    /**
     * erp sync 时间 map key
     */
    String TASK_SYNC_DATA_TIME_MAP_KEY = "bis:task:sync_data_time";

    /**
     * supplier existed table Ids
     */
    String SUPPLIER_TABLE_IDS_KEY = "bis:existed:supplier_table_ids";

    // -----------------合同等同步的任务key

    /**
     *合同数据和附件数据同步key
     */
    String CONTRACT_FILE_SYNC_ERP_TASK = "contractFileSyncErpTask";

    /**
     *结算池数据同步key
     */
    String SETTLEMENT_POOL_SYNC_ERP_TASK = "settlementPoolSyncErpTask";

    /**
     * 指定供应商结算池同步key
     */
    String INIT_SUPPLIER_SETTLEMENT_POOL_SYNC_ERP_TASK = "initSupplierSettlementPoolSyncErpTask";

    /**
     *合同数据同步key
     */
    String CONTRACT_DATA_SYNC_ERP_TASK = "contractDataSyncErpTask";

}
