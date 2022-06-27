package net.bncloud.bis.model.vo;

import lombok.Data;
import net.bncloud.msk3cloud.core.fieldparse.FieldKey;

import java.io.Serializable;

/**
 * desc: 采购入库单 同步
 *
 * @author Rao
 * @Date 2022/01/22
 **/
@Data
public class PurchaseInStockOrderVo implements Serializable {
    private static final long serialVersionUID = 8019519082399356469L;

    /**
     * erp ID
     */
    @FieldKey("FID")
    private Long fid;

    /**
     * 采购入库单据编号
     */
    @FieldKey("FBillNo")
    private String fBillNo;

    /**
     * 入库日期
     */
    @FieldKey("FDate")
    private String fDate;

    // --------------- FInStockEntry_Link
    /**
     * 源单内码 （收料通知单的 ID）
     */
    @FieldKey("FInStockEntry_Link_FSBillId")
    private Long fInStockEntryLinkFSBillId;

    /**
     * 源单分录内码 (收料通知单的 明细ID)
     */
    @FieldKey("FInStockEntry_Link_FSId")
    private Long fInStockEntryLinkFSId;

    // ------------------- FInStockEntry

    /**
     * 实收数量
     */
    @FieldKey("FRealQty")
    private Integer fRealQty;


}
