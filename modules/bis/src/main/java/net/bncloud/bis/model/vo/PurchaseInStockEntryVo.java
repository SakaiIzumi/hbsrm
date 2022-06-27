package net.bncloud.bis.model.vo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * desc: 采购入库单明细 信息
 *
 * @author Rao
 * @Date 2022/01/22
 **/
@NoArgsConstructor
@Data
public class PurchaseInStockEntryVo implements Serializable {
    private static final long serialVersionUID = -8137989147635340102L;

    /**
     * 实体主键
     */
    @JSONField(name = "FEntryID")
    private Long fEntryId;
    @JSONField(name = "FRowType")
    private String fRowType;
    @JSONField(name = "FWWInType")
    private String fWWInType;
    /**
     * 物料编码
     */
    @JSONField(name = "FMaterialId")
    private FMaterialId fMaterialId;
    @JSONField(name = "FCMKBarCode")
    private String fCMKBarCode;
    @JSONField(name = "FMaterialName")
    private String fMaterialName;
    @JSONField(name = "FUOM")
    private String fuom;
    @JSONField(name = "FUnitID")
    private FUnitID fUnitID;
    @JSONField(name = "FAuxPropId")
    private String fAuxPropId;
    @JSONField(name = "FParentMatId")
    private FParentMatId fParentMatId;
    @JSONField(name = "FMustQty")
    private Integer fMustQty;
    @JSONField(name = "FRealQty")
    private Integer fRealQty;
    @JSONField(name = "FPriceUnitID")
    private FPriceUnitID fPriceUnitID;
    @JSONField(name = "FPriceUnitQty")
    private Integer fPriceUnitQty;
    @JSONField(name = "FPrice")
    private String fPrice;
    @JSONField(name = "FTaxCombination")
    private FTaxCombination fTaxCombination;
    @JSONField(name = "FLot")
    private FLot fLot;
    @JSONField(name = "FAmount")
    private String fAmount;
    @JSONField(name = "FDiscount")
    private String fDiscount;
    @JSONField(name = "FStockId")
    private FStockId fStockId;
    @JSONField(name = "FDisPriceQty")
    private String fDisPriceQty;
    @JSONField(name = "FStockLocId")
    private String fStockLocId;
    @JSONField(name = "FStockStatusId")
    private FStockStatusId fStockStatusId;
    @JSONField(name = "FMtoNo")
    private String fMtoNo;
    @JSONField(name = "FGiveAway")
    private String fGiveAway;
    @JSONField(name = "FNote")
    private String fNote;
    @JSONField(name = "FProduceDate")
    private String fProduceDate;
    @JSONField(name = "FBaseUnitID")
    private FBaseUnitID fBaseUnitID;
    @JSONField(name = "FIsFree")
    private String fIsFree;
    @JSONField(name = "FAmount_LC")
    private String famountLc;
    @JSONField(name = "FSysPrice")
    private String fSysPrice;
    @JSONField(name = "FUpPrice")
    private String fUpPrice;
    @JSONField(name = "FDownPrice")
    private String fDownPrice;
    @JSONField(name = "FOWNERTYPEID")
    private String fownertypeid;
    @JSONField(name = "FReceiveStockID")
    private FReceiveStockID fReceiveStockID;
    @JSONField(name = "FReceiveLot")
    private FReceiveLot fReceiveLot;
    @JSONField(name = "FReceiveStockLocId")
    private String fReceiveStockLocId;
    @JSONField(name = "FExtAuxUnitId")
    private FExtAuxUnitId fExtAuxUnitId;
    @JSONField(name = "FExtAuxUnitQty")
    private Integer fExtAuxUnitQty;
    @JSONField(name = "FGrossWeight")
    private String fGrossWeight;
    @JSONField(name = "FNetWeight")
    private String fNetWeight;
    @JSONField(name = "FContractlNo")
    private String fContractlNo;
    @JSONField(name = "FReturnJoinQty")
    private Integer fReturnJoinQty;
    @JSONField(name = "FCheckInComing")
    private String fCheckInComing;
    @JSONField(name = "FBaseReturnJoinQty")
    private String fBaseReturnJoinQty;
    @JSONField(name = "FProjectNo")
    private String fProjectNo;
    @JSONField(name = "FSNUnitID")
    private FSNUnitID fSNUnitID;
    @JSONField(name = "FSampleDamageQty")
    private Integer fSampleDamageQty;
    @JSONField(name = "FSampleDamageBaseQty")
    private String fSampleDamageBaseQty;
    @JSONField(name = "FSNQty")
    private Integer fSNQty;
    @JSONField(name = "FSECRETURNJOINQTY")
    private Integer fsecreturnjoinqty;
    @JSONField(name = "FIsReceiveUpdateStock")
    private String fIsReceiveUpdateStock;
    @JSONField(name = "FInvoicedStatus")
    private String fInvoicedStatus;
    @JSONField(name = "FInvoicedJoinQty")
    private String fInvoicedJoinQty;
    @JSONField(name = "FPriceBaseQty")
    private String fPriceBaseQty;
    @JSONField(name = "FSetPriceUnitID")
    private FSetPriceUnitID fSetPriceUnitID;
    @JSONField(name = "FRemainInStockUnitId")
    private FRemainInStockUnitId fRemainInStockUnitId;
    @JSONField(name = "FReceiveMtoNo")
    private String fReceiveMtoNo;
    @JSONField(name = "FPURBASENUM")
    private String fpurbasenum;
    @JSONField(name = "FStockBaseDen")
    private String fStockBaseDen;
    @JSONField(name = "FSRCBIZUNITID")
    private FSRCBIZUNITID fsrcbizunitid;
    @JSONField(name = "FRETURNSTOCKJNBASEQTY")
    private String freturnstockjnbaseqty;
    @JSONField(name = "FCOSTPRICE_LC")
    private String fcostpriceLc;
    @JSONField(name = "FPOORDERENTRYID")
    private Integer fpoorderentryid;
    @JSONField(name = "FBILLINGCLOSE")
    private String fbillingclose;
    @JSONField(name = "FPriceListEntry")
    private FPriceListEntry fPriceListEntry;
    @JSONField(name = "FStockBaseAPJoinQty")
    private String fStockBaseAPJoinQty;
    @JSONField(name = "FRemainInStockQty")
    private Integer fRemainInStockQty;
    @JSONField(name = "FAPNotJoinQty")
    private Integer fAPNotJoinQty;
    @JSONField(name = "FAPJoinAmount")
    private String fAPJoinAmount;
    @JSONField(name = "FRemainInStockBaseQty")
    private String fRemainInStockBaseQty;
    @JSONField(name = "FMaterialType")
    private String fMaterialType;
    @JSONField(name = "FTaxPrice")
    private String fTaxPrice;
    @JSONField(name = "FEntryTaxRate")
    private String fEntryTaxRate;
    @JSONField(name = "FDiscountRate")
    private String fDiscountRate;
    @JSONField(name = "FEntryTaxAmount")
    private String fEntryTaxAmount;
    @JSONField(name = "FAllAmount")
    private String fAllAmount;
    @JSONField(name = "FCostPrice")
    private String fCostPrice;
    @JSONField(name = "FEntryCostAmount")
    private String fEntryCostAmount;
    @JSONField(name = "FBOMId")
    private FBOMId fBOMId;
    @JSONField(name = "FSupplierLot")
    private String fSupplierLot;
    @JSONField(name = "FExpPeriod")
    private String fExpPeriod;
    @JSONField(name = "FEXPUnit")
    private String fEXPUnit;
    @JSONField(name = "FExpiryDate")
    private String fExpiryDate;
    @JSONField(name = "FShelfLife")
    private String fShelfLife;
    @JSONField(name = "FBaseUnitQty")
    private String fBaseUnitQty;
    @JSONField(name = "FAuxUnitID")
    private FAuxUnitID fAuxUnitID;
    @JSONField(name = "FAuxUnitQty")
    private Integer fAuxUnitQty;
    @JSONField(name = "FPriceCoefficient")
    private String fPriceCoefficient;
    @JSONField(name = "FTaxAmount_LC")
    private String ftaxamountLc;
    @JSONField(name = "FAllAmount_LC")
    private String fallamountLc;
    @JSONField(name = "FBaseUnitPrice")
    private String fBaseUnitPrice;
    @JSONField(name = "FProcessFee")
    private String fProcessFee;
    @JSONField(name = "FProcessFee_LC")
    private String fprocessfeeLc;
    @JSONField(name = "FMaterialCosts")
    private String fMaterialCosts;
    @JSONField(name = "FMaterialCosts_LC")
    private String fmaterialcostsLc;
    @JSONField(name = "FCostAmount_LC")
    private String fcostamountLc;
    @JSONField(name = "FOWNERID")
    private FOWNERID fownerid;
    @JSONField(name = "FKeeperTypeId")
    private String fKeeperTypeId;
    @JSONField(name = "FKeeperID")
    private FKeeperID fKeeperID;
    @JSONField(name = "FReceiveOwnerTypeId")
    private String fReceiveOwnerTypeId;
    @JSONField(name = "FReceiveOwnerId")
    private FReceiveOwnerId fReceiveOwnerId;
    @JSONField(name = "FTaxNetPrice")
    private String fTaxNetPrice;
    @JSONField(name = "FReceiveStockStatus")
    private FReceiveStockStatus fReceiveStockStatus;
    @JSONField(name = "FReceiveStockFlag")
    private String fReceiveStockFlag;
    @JSONField(name = "FStockFlag")
    private String fStockFlag;
    @JSONField(name = "FSRCBILLTYPEID")
    private String fsrcbilltypeid;
    @JSONField(name = "FSRCBillNo")
    private String fSRCBillNo;
    @JSONField(name = "FSRCRowId")
    private Integer fSRCRowId;
    /**
     * FPOOrderNo
     */
    @JSONField(name = "FPOOrderNo")
    private String fPOOrderNo;
    @JSONField(name = "FINVOICEDQTY")
    private String finvoicedqty;
    @JSONField(name = "FJOINEDQTY")
    private Integer fjoinedqty;
    @JSONField(name = "FUNJOINQTY")
    private Integer funjoinqty;
    @JSONField(name = "FJOINEDAMOUNT")
    private String fjoinedamount;
    @JSONField(name = "FUNJOINAMOUNT")
    private String funjoinamount;
    @JSONField(name = "FFULLYJOINED")
    private String ffullyjoined;
    @JSONField(name = "FJOINSTATUS")
    private String fjoinstatus;
    @JSONField(name = "FReqTraceNo")
    private String fReqTraceNo;
    @JSONField(name = "FAllotBaseQty")
    private String fAllotBaseQty;
    @JSONField(name = "FBaseAPJoinQty")
    private String fBaseAPJoinQty;
    @JSONField(name = "FBaseJoinQty")
    private String fBaseJoinQty;
    @JSONField(name = "FIsScanEntry")
    private String fIsScanEntry;
    @JSONField(name = "FPayableCloseStatus")
    private String fPayableCloseStatus;
    @JSONField(name = "FPAYABLECLOSEDATE")
    private String fpayableclosedate;
    @JSONField(name = "FTHIRDENTRYID")
    private String fthirdentryid;
    @JSONField(name = "FPriceDiscount")
    private String fPriceDiscount;
    @JSONField(name = "FPriLstEntryId")
    private Integer fPriLstEntryId;
    @JSONField(name = "FConsumeSumQty")
    private Integer fConsumeSumQty;
    @JSONField(name = "FBaseConsumeSumQty")
    private String fBaseConsumeSumQty;
    @JSONField(name = "FRejectsDiscountAmount")
    private String fRejectsDiscountAmount;
    @JSONField(name = "FBeforeDisPriceQty")
    private Integer fBeforeDisPriceQty;
    @JSONField(name = "FRECSUBENTRYID")
    private Integer frecsubentryid;
    @JSONField(name = "FReceiveAuxPropId")
    private String fReceiveAuxPropId;
    @JSONField(name = "FRowId")
    private String fRowId;
    @JSONField(name = "FParentRowId")
    private String fParentRowId;
    @JSONField(name = "FEntryPruCost")
    private List<FEntryPruCost> fEntryPruCost;

    @NoArgsConstructor
    @Data
    public static class FMaterialId {
        @JSONField(name = "FNumber")
        private String fNumber;
    }

    @NoArgsConstructor
    @Data
    public static class FUnitID {
        @JSONField(name = "FNumber")
        private String fNumber;
    }

    @NoArgsConstructor
    @Data
    public static class FParentMatId {
        @JSONField(name = "FNumber")
        private String fNumber;
    }

    @NoArgsConstructor
    @Data
    public static class FPriceUnitID {
        @JSONField(name = "FNumber")
        private String fNumber;
    }

    @NoArgsConstructor
    @Data
    public static class FTaxCombination {
        @JSONField(name = "FNumber")
        private String fNumber;
    }

    @NoArgsConstructor
    @Data
    public static class FLot {
        @JSONField(name = "FNumber")
        private String fNumber;
    }

    @NoArgsConstructor
    @Data
    public static class FStockId {
        @JSONField(name = "FNumber")
        private String fNumber;
    }

    @NoArgsConstructor
    @Data
    public static class FStockStatusId {
        @JSONField(name = "FNumber")
        private String fNumber;
    }

    @NoArgsConstructor
    @Data
    public static class FBaseUnitID {
        @JSONField(name = "FNumber")
        private String fNumber;
    }

    @NoArgsConstructor
    @Data
    public static class FReceiveStockID {
        @JSONField(name = "FNumber")
        private String fNumber;
    }

    @NoArgsConstructor
    @Data
    public static class FReceiveLot {
        @JSONField(name = "FNumber")
        private String fNumber;
    }

    @NoArgsConstructor
    @Data
    public static class FExtAuxUnitId {
        @JSONField(name = "FNumber")
        private String fNumber;
    }

    @NoArgsConstructor
    @Data
    public static class FSNUnitID {
        @JSONField(name = "FNumber")
        private String fNumber;
    }

    @NoArgsConstructor
    @Data
    public static class FSetPriceUnitID {
        @JSONField(name = "FNumber")
        private String fNumber;
    }

    @NoArgsConstructor
    @Data
    public static class FRemainInStockUnitId {
        @JSONField(name = "FNumber")
        private String fNumber;
    }

    @NoArgsConstructor
    @Data
    public static class FSRCBIZUNITID {
        @JSONField(name = "FNumber")
        private String fNumber;
    }

    @NoArgsConstructor
    @Data
    public static class FPriceListEntry {
        @JSONField(name = "FNumber")
        private String fNumber;
    }

    @NoArgsConstructor
    @Data
    public static class FBOMId {
        @JSONField(name = "FNumber")
        private String fNumber;
    }

    @NoArgsConstructor
    @Data
    public static class FAuxUnitID {
        @JSONField(name = "FNumber")
        private String fNumber;
    }

    @NoArgsConstructor
    @Data
    public static class FOWNERID {
        @JSONField(name = "FNumber")
        private String fNumber;
    }

    @NoArgsConstructor
    @Data
    public static class FKeeperID {
        @JSONField(name = "FNumber")
        private String fNumber;
    }

    @NoArgsConstructor
    @Data
    public static class FReceiveOwnerId {
        @JSONField(name = "FNumber")
        private String fNumber;
    }

    @NoArgsConstructor
    @Data
    public static class FReceiveStockStatus {
        @JSONField(name = "FNumber")
        private String fNumber;
    }

    @NoArgsConstructor
    @Data
    public static class FEntryPruCost {
        @JSONField(name = "FCostId")
        private FCostId fCostId;
        @JSONField(name = "FCostName")
        private String fCostName;
        @JSONField(name = "FCostAmount")
        private String fCostAmount;
        @JSONField(name = "FCostNOTE")
        private String fCostNOTE;

        @NoArgsConstructor
        @Data
        public static class FCostId {
            @JSONField(name = "FNumber")
            private String fNumber;
        }
    }
}
