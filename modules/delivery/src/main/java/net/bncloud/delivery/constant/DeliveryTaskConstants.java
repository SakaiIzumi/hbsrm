package net.bncloud.delivery.constant;

/**
 * desc: 任务常量类
 *
 * @author Rao
 * @Date 2022/03/07
 **/
public interface DeliveryTaskConstants {

    /**
     * 供需平衡报表同步定时任务
     */
    String SUPPLY_AND_DEMAND_BALANCE_CLEAN_DATA = "deliver:" + "supplyAndDemandBalance:CleanData";

    /**
     * 同步采购信息到工厂信息
     */
    String SYNC_PURCHASE_TO_FACTORY_INFO = "syncPurchaseToFactoryInfo";

}
