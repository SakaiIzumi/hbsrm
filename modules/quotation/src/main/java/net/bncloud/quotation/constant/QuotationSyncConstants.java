package net.bncloud.quotation.constant;

/**
 * desc: 同步常量
 *
 * @author liyh
 * @Date 2022/03/18
 **/
public interface QuotationSyncConstants {

    /**
     * 锁前缀key
     */
    String SYNC_LOCK_PREFIX_KEY = "TRY_LOCK:";

    /**
     * 同步主物料时间
     */
    String TRY_LOCK_UPDATE_QUOTATION_STATUS = "tryLockUpdateQuotationStatus";

}
