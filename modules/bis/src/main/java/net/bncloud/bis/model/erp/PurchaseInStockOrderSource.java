package net.bncloud.bis.model.erp;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.bncloud.msk3cloud.kingdee.entity.common.Number;

import java.io.Serializable;
import java.util.List;

/**
 * desc:
 *
 * @author Rao
 * @Date 2022/04/25
 **/
@NoArgsConstructor
@Data
public class PurchaseInStockOrderSource implements Serializable {

    private static final long serialVersionUID = 8503347261381992042L;


    /**
     * fid
     */
    @JSONField(name = "FID")
    private String fid;
    /**
     * 单据编号
     */
    @JSONField(name = "FBillTypeID")
    private Number fBillTypeId;
    /**
     * fBillNo
     */
    @JSONField(name = "FBillNo")
    private String fBillNo;
    /**
     * fDate
     */
    @JSONField(name = "FDate")
    private String fDate;
    /**
     * 收料组织
     */
    @JSONField(name = "FStockOrgId")
    private Number fStockOrgId;
    /**
     * fStockDeptId
     */
    @JSONField(name = "FStockDeptId")
    private Number fStockDeptId;
    /**
     * fStockerGroupId
     */
    @JSONField(name = "FStockerGroupId")
    private Number fStockerGroupId;
    /**
     * fStockerId
     */
    @JSONField(name = "FStockerId")
    private Number fStockerId;
    /**
     * fDemandOrgId
     */
    @JSONField(name = "FDemandOrgId")
    private Number fDemandOrgId;
    /**
     * fCorrespondOrgId
     */
    @JSONField(name = "FCorrespondOrgId")
    private Number fCorrespondOrgId;
    /**
     *  采购组织
     */
    @JSONField(name = "FPurchaseOrgId")
    private Number fPurchaseOrgId;
    /**
     * fPurchaseDeptId
     */
    @JSONField(name = "FPurchaseDeptId")
    private Number fPurchaseDeptId;
    /**
     * fPurchaserGroupId
     */
    @JSONField(name = "FPurchaserGroupId")
    private Number fPurchaserGroupId;
    /**
     * fPurchaserId
     */
    @JSONField(name = "FPurchaserId")
    private Number fPurchaserId;
    /**
     * fSupplierId
     */
    @JSONField(name = "FSupplierId")
    private Number fSupplierId;
    /**
     * fSupplyId
     */
    @JSONField(name = "FSupplyId")
    private Number fSupplyId;
    /**
     * fSupplyAddress
     */
    @JSONField(name = "FSupplyAddress")
    private String fSupplyAddress;
    /**
     * fSettleId
     */
    @JSONField(name = "FSettleId")
    private Number fSettleId;
    /**
     * fChargeId
     */
    @JSONField(name = "FChargeId")
    private Number fChargeId;
    /**
     * fOwnerTypeIdHead
     */
    @JSONField(name = "FOwnerTypeIdHead")
    private String fOwnerTypeIdHead;
    /**
     * fOwnerIdHead
     */
    @JSONField(name = "FOwnerIdHead")
    private Number fOwnerIdHead;
    /**
     *  物料分类
     */
    @JSONField(name = "F_MS_WLFL")
    private Number fMsWlfl;
    /**
     * fConfirmerId
     */
    @JSONField(name = "FConfirmerId")
    private FConfirmerId fConfirmerId;
    /**
     * fConfirmDate
     */
    @JSONField(name = "FConfirmDate")
    private String fConfirmDate;
    /**
     * fScanBox
     */
    @JSONField(name = "FScanBox")
    private String fScanBox;
    /**
     * fCDateOffsetUnit
     */
    @JSONField(name = "FCDateOffsetUnit")
    private String fCDateOffsetUnit;
    /**
     * fCDateOffsetValue
     */
    @JSONField(name = "FCDateOffsetValue")
    private Integer fCDateOffsetValue;
    /**
     * fProviderContactID
     */
    @JSONField(name = "FProviderContactID")
    private FProviderContactID fProviderContactID;
    /**
     * fSplitBillType
     */
    @JSONField(name = "FSplitBillType")
    private String fSplitBillType;
    /**
     * fQhIspricegapzg
     */
    @JSONField(name = "F_QH_ISPRICEGAPZG")
    private String fQhIspricegapzg;
    /**
     * fSupplyEMail
     */
    @JSONField(name = "FSupplyEMail")
    private String fSupplyEMail;
    /**
     * fQhIsvoucher
     */
    @JSONField(name = "F_QH_ISVOUCHER")
    private String fQhIsvoucher;
    /**
     * fQhPoandstdgap
     */
    @JSONField(name = "F_QH_POANDSTDGAP")
    private String fQhPoandstdgap;
    /**
     * fPaezDsfdh
     */
    @JSONField(name = "F_PAEZ_DSFDH")
    private String fPaezDsfdh;
    /**
     * fInStockFin
     */
    @JSONField(name = "FInStockFin")
    private FInStockFin fInStockFin;
    /**
     * fInStockEntry
     */
    @JSONField(name = "FInStockEntry")
    private List<FInStockEntry> fInStockEntry;

    /**
     * FConfirmerId
     */
    @NoArgsConstructor
    @Data
    public static class FConfirmerId {
        /**
         * fUserID
         */
        @JSONField(name = "FUserID")
        private String fUserID;
    }

    /**
     * FProviderContactID
     */
    @NoArgsConstructor
    @Data
    public static class FProviderContactID {
        /**
         * fcontactnumber
         */
        @JSONField(name = "FCONTACTNUMBER")
        private String fcontactnumber;
    }

    /**
     * FInStockFin
     */
    @NoArgsConstructor
    @Data
    public static class FInStockFin {
        /**
         * fEntryId
         */
        @JSONField(name = "FEntryId")
        private String fEntryId;
        /**
         * fSettleOrgId
         */
        @JSONField(name = "FSettleOrgId")
        private Number fSettleOrgId;
        /**
         * fSettleTypeId
         */
        @JSONField(name = "FSettleTypeId")
        private Number fSettleTypeId;
        /**
         * fPayConditionId
         */
        @JSONField(name = "FPayConditionId")
        private Number fPayConditionId;
        /**
         * fSettleCurrId
         */
        @JSONField(name = "FSettleCurrId")
        private Number fSettleCurrId;
        /**
         * fIsIncludedTax
         */
        @JSONField(name = "FIsIncludedTax")
        private String fIsIncludedTax;
        /**
         * fPriceTimePoint
         */
        @JSONField(name = "FPriceTimePoint")
        private String fPriceTimePoint;
        /**
         * fPriceListId
         */
        @JSONField(name = "FPriceListId")
        private Number fPriceListId;
        /**
         * fDiscountListId
         */
        @JSONField(name = "FDiscountListId")
        private Number fDiscountListId;
        /**
         * fLocalCurrId
         */
        @JSONField(name = "FLocalCurrId")
        private Number fLocalCurrId;
        /**
         * fExchangeTypeId
         */
        @JSONField(name = "FExchangeTypeId")
        private Number fExchangeTypeId;
        /**
         * fQhStdcostrate
         */
        @JSONField(name = "F_QH_STDCOSTRATE")
        private Integer fQhStdcostrate;
        /**
         * fExchangeRate
         */
        @JSONField(name = "FExchangeRate")
        private Integer fExchangeRate;
        /**
         * fispriceexcludetax
         */
        @JSONField(name = "FISPRICEEXCLUDETAX")
        private String fispriceexcludetax;
        /**
         * fQhCostversion
         */
        @JSONField(name = "F_QH_COSTVERSION")
        private Number fQhCostversion;
        /**
         * fAllDisCount
         */
        @JSONField(name = "FAllDisCount")
        private Integer fAllDisCount;

    }

    /**
     * FInStockEntry
     */
    @NoArgsConstructor
    @Data
    public static class FInStockEntry {
        /**
         * fEntryID
         */
        @JSONField(name = "FEntryID")
        private String fEntryId;
        /**
         * fRowType
         */
        @JSONField(name = "FRowType")
        private String fRowType;
        /**
         * fWWInType
         */
        @JSONField(name = "FWWInType")
        private String fWWInType;
        /**
         * 物料编码
         */
        @JSONField(name = "FMaterialId")
        private Number fMaterialId;

        /**
         * 库存单位
         */
        @JSONField(name = "FUnitID")
        private Number fUnitId;
        /**
         * fParentMatId
         */
        @JSONField(name = "FParentMatId")
        private Number fParentMatId;
        /**
         *  实收数量
         */
        @JSONField(name = "FRealQty")
        private String fRealQty;
        /**
         *  计价单位
         */
        @JSONField(name = "FPriceUnitID")
        private Number fPriceUnitId;
        /**
         * fPrice
         */
        @JSONField(name = "FPrice")
        private Integer fPrice;
        /**
         *  批号
         */
        @JSONField(name = "FLot")
        private Number fLot;
        /**
         * fTaxCombination
         */
        @JSONField(name = "FTaxCombination")
        private Number fTaxCombination;
        /**
         * fStockId
         */
        @JSONField(name = "FStockId")
        private Number fStockId;
        /**
         * fDisPriceQty
         */
        @JSONField(name = "FDisPriceQty")
        private String fDisPriceQty;

        /**
         * fStockStatusId
         */
        @JSONField(name = "FStockStatusId")
        private Number fStockStatusId;
        /**
         * fMtoNo
         */
        @JSONField(name = "FMtoNo")
        private String fMtoNo;
        /**
         * fGiveAway
         */
        @JSONField(name = "FGiveAway")
        private String fGiveAway;
        /**
         * fNote
         */
        @JSONField(name = "FNote")
        private String fNote;
        /**
         * fProduceDate
         */
        @JSONField(name = "FProduceDate")
        private String fProduceDate;
        /**
         * 货主类型
         */
        @JSONField(name = "FOWNERTYPEID")
        private String fownertypeid;
        /**
         * fExtAuxUnitId
         */
        @JSONField(name = "FExtAuxUnitId")
        private Number fExtAuxUnitId;
        /**
         * fExtAuxUnitQty
         */
        @JSONField(name = "FExtAuxUnitQty")
        private String fExtAuxUnitQty;
        /**
         * fCheckInComing
         */
        @JSONField(name = "FCheckInComing")
        private String fCheckInComing;
        /**
         * fProjectNo
         */
        @JSONField(name = "FProjectNo")
        private String fProjectNo;
        /**
         * fIsReceiveUpdateStock
         */
        @JSONField(name = "FIsReceiveUpdateStock")
        private String fIsReceiveUpdateStock;
        /**
         * fInvoicedJoinQty
         */
        @JSONField(name = "FInvoicedJoinQty")
        private String fInvoicedJoinQty;
        /**
         * fPriceBaseQty
         */
        @JSONField(name = "FPriceBaseQty")
        private String fPriceBaseQty;
        /**
         * fSetPriceUnitID
         */
        @JSONField(name = "FSetPriceUnitID")
        private Number fSetPriceUnitID;
        /**
         *  采购单位
         */
        @JSONField(name = "FRemainInStockUnitId")
        private Number fRemainInStockUnitId;
        /**
         * fbillingclose
         */
        @JSONField(name = "FBILLINGCLOSE")
        private String fbillingclose;
        /**
         * fRemainInStockQty
         */
        @JSONField(name = "FRemainInStockQty")
        private Integer fRemainInStockQty;
        /**
         * fAPNotJoinQty
         */
        @JSONField(name = "FAPNotJoinQty")
        private Integer fAPNotJoinQty;
        /**
         * fRemainInStockBaseQty
         */
        @JSONField(name = "FRemainInStockBaseQty")
        private Integer fRemainInStockBaseQty;
        /**
         * fTaxPrice
         */
        @JSONField(name = "FTaxPrice")
        private Integer fTaxPrice;
        /**
         * fEntryTaxRate
         */
        @JSONField(name = "FEntryTaxRate")
        private Integer fEntryTaxRate;
        /**
         * fDiscountRate
         */
        @JSONField(name = "FDiscountRate")
        private Integer fDiscountRate;
        /**
         * fBOMId
         */
        @JSONField(name = "FBOMId")
        private Number fBOMId;
        /**
         * fSupplierLot
         */
        @JSONField(name = "FSupplierLot")
        private String fSupplierLot;
        /**
         * fExpiryDate
         */
        @JSONField(name = "FExpiryDate")
        private String fExpiryDate;
        /**
         * fAuxUnitQty
         */
        @JSONField(name = "FAuxUnitQty")
        private Integer fAuxUnitQty;
        /**
         * fCostPrice
         */
        @JSONField(name = "FCostPrice")
        private Integer fCostPrice;
        /**
         *  货主
         */
        @JSONField(name = "FOWNERID")
        private Number fownerid;
        /**
         * fQhIsstdcost
         */
        @JSONField(name = "F_QH_ISSTDCOST")
        private String fQhIsstdcost;
        /**
         * fQhOripricegap
         */
        @JSONField(name = "F_QH_ORIPRICEGAP")
        private Integer fQhOripricegap;
        /**
         * fQhOriamountgap
         */
        @JSONField(name = "F_QH_ORIAmountGap")
        private Integer fQhOriamountgap;
        /**
         * fQhPricegap
         */
        @JSONField(name = "F_QH_PriceGap")
        private Integer fQhPricegap;
        /**
         * fQhAmountgap
         */
        @JSONField(name = "F_QH_AmountGap")
        private Integer fQhAmountgap;
        /**
         * fQhAmountgapPrice
         */
        @JSONField(name = "F_QH_AmountGap_Price")
        private Integer fQhAmountgapPrice;
        /**
         * fQhAmountgapRate
         */
        @JSONField(name = "F_QH_AmountGap_rate")
        private Integer fQhAmountgapRate;
        /**
         * fBeforeDisPriceQty
         */
        @JSONField(name = "FBeforeDisPriceQty")
        private Integer fBeforeDisPriceQty;
        /**
         * fQhCheckvourcherlog
         */
        @JSONField(name = "F_QH_CHECKVOURCHERLOG")
        private String fQhCheckvourcherlog;
        /**
         * fAllAmountExceptDisCount
         */
        @JSONField(name = "FAllAmountExceptDisCount")
        private Integer fAllAmountExceptDisCount;
        /**
         * fQhCheckvourchertype
         */
        @JSONField(name = "F_QH_CHECKVOURCHERTYPE")
        private String fQhCheckvourchertype;
        /**
         * fPriceDiscount
         */
        @JSONField(name = "FPriceDiscount")
        private Integer fPriceDiscount;
        /**
         * fQhPoexchangerate
         */
        @JSONField(name = "F_QH_POEXCHANGERATE")
        private Integer fQhPoexchangerate;
        /**
         * 供应商订单号
         */
        @JSONField(name = "F_QH_SUPPLYNO")
        private String fQhSupplyno;
        /**
         * fQhStdpriceMaterial
         */
        @JSONField(name = "F_QH_STDPRICE_MATERIAL")
        private Integer fQhStdpriceMaterial;
        /**
         * fConsumeSumQty
         */
        @JSONField(name = "FConsumeSumQty")
        private Integer fConsumeSumQty;
        /**
         * fQhStdpriceProcess
         */
        @JSONField(name = "F_QH_STDPRICE_PROCESS")
        private Integer fQhStdpriceProcess;
        /**
         * fBaseConsumeSumQty
         */
        @JSONField(name = "FBaseConsumeSumQty")
        private Integer fBaseConsumeSumQty;
        /**
         * fQhStdamountadjGap
         */
        @JSONField(name = "F_QH_STDAMOUNTADJ_GAP")
        private Integer fQhStdamountadjGap;
        /**
         * fQhGapcost
         */
        @JSONField(name = "F_QH_GAPCOST")
        private Integer fQhGapcost;
        /**
         * fQhActcost
         */
        @JSONField(name = "F_QH_ACTCOST")
        private Integer fQhActcost;
        /**
         * fRejectsDiscountAmount
         */
        @JSONField(name = "FRejectsDiscountAmount")
        private Integer fRejectsDiscountAmount;
        /**
         * fEntryPruCost
         */
        @JSONField(name = "FEntryPruCost")
        private List<FEntryPruCost> fEntryPruCost;
        /**
         * fTaxDetailSubEntity
         */
        @JSONField(name = "FTaxDetailSubEntity")
        private List<FTaxDetailSubEntity> fTaxDetailSubEntity;
        /**
         * fSerialSubEntity
         */
        @JSONField(name = "FSerialSubEntity")
        private List<FSerialSubEntity> fSerialSubEntity;
        /**
         * fQhInstockStd
         */
        @JSONField(name = "F_QH_INSTOCK_STD")
        private List<FQHINSTOCKSTD> fQhInstockStd;
        /**
         * fBcsSubentity
         */
        @JSONField(name = "F_BCS_SubEntity")
        private List<FBCSSubEntity> fBcsSubentity;


        /**
         * FEntryPruCost
         */
        @NoArgsConstructor
        @Data
        public static class FEntryPruCost {
            /**
             * fDetailID
             */
            @JSONField(name = "FDetailID")
            private String fDetailID;
            /**
             * fQhAmountStd
             */
            @JSONField(name = "F_QH_Amount_STD")
            private Integer fQhAmountStd;
            /**
             * fQhPriceStdori
             */
            @JSONField(name = "F_QH_Price_STDORI")
            private Integer fQhPriceStdori;
            /**
             * fQhPriceStd
             */
            @JSONField(name = "F_QH_Price_STD")
            private Integer fQhPriceStd;
            /**
             * fQhCostitem
             */
            @JSONField(name = "F_QH_COSTITEM")
            private Number fQhCostitem;
            /**
             * fQhExptype
             */
            @JSONField(name = "F_QH_EXPTYPE")
            private String fQhExptype;

        }

        /**
         * FTaxDetailSubEntity
         */
        @NoArgsConstructor
        @Data
        public static class FTaxDetailSubEntity {
            /**
             * fDetailID
             */
            @JSONField(name = "FDetailID")
            private Integer fDetailID;
            /**
             * fTaxRate
             */
            @JSONField(name = "FTaxRate")
            private Integer fTaxRate;
        }

        /**
         * FSerialSubEntity
         */
        @NoArgsConstructor
        @Data
        public static class FSerialSubEntity {
            /**
             * fDetailID
             */
            @JSONField(name = "FDetailID")
            private Integer fDetailID;
            /**
             * fSerialNo
             */
            @JSONField(name = "FSerialNo")
            private String fSerialNo;
            /**
             * fSerialNote
             */
            @JSONField(name = "FSerialNote")
            private String fSerialNote;
        }

        /**
         * FQHINSTOCKSTD
         */
        @NoArgsConstructor
        @Data
        public static class FQHINSTOCKSTD {
            /**
             * fDetailID
             */
            @JSONField(name = "FDetailID")
            private Integer fDetailID;
            /**
             * fQhExpitemid
             */
            @JSONField(name = "F_QH_EXPITEMID")
            private Number fQhExpitemid;
            /**
             * fQhCostitemid
             */
            @JSONField(name = "F_QH_COSTITEMID")
            private Number fQhCostitemid;
            /**
             * fQhExptypeid
             */
            @JSONField(name = "F_QH_EXPTYPEID")
            private String fQhExptypeid;
            /**
             * fQhStdpriceStd
             */
            @JSONField(name = "F_QH_STDPRICE_STD")
            private Integer fQhStdpriceStd;
            /**
             * fQhStdamountStd
             */
            @JSONField(name = "F_QH_STDAMOUNT_STD")
            private Integer fQhStdamountStd;
            /**
             * fQhStdpriceOri
             */
            @JSONField(name = "F_QH_STDPRICE_ORI")
            private Integer fQhStdpriceOri;
            /**
             * fQhStdamountOri
             */
            @JSONField(name = "F_QH_STDAMOUNT_ORI")
            private Integer fQhStdamountOri;
            /**
             * fQhAcctsystemid
             */
            @JSONField(name = "F_QH_ACCTSYSTEMID")
            private Number fQhAcctsystemid;
            /**
             * fQhMaterialid
             */
            @JSONField(name = "F_QH_MATERIALID")
            private Number fQhMaterialid;

        }

        /**
         * FBCSSubEntity
         */
        @NoArgsConstructor
        @Data
        public static class FBCSSubEntity {
            /**
             * fDetailID
             */
            @JSONField(name = "FDetailID")
            private Integer fDetailID;
            /**
             * fQhGapid
             */
            @JSONField(name = "F_QH_GAPID")
            private Number fQhGapid;
            /**
             * fQhGapcostD
             */
            @JSONField(name = "F_QH_GAPCOST_D")
            private Integer fQhGapcostD;
            /**
             * fQhGappriceD
             */
            @JSONField(name = "F_QH_GAPPRICE_D")
            private Integer fQhGappriceD;

        }
    }
}
