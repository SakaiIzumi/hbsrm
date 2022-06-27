package net.bncloud.bis.model.erp;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.bncloud.msk3cloud.kingdee.entity.common.Number;

import java.io.Serializable;
import java.util.List;

/**
 * desc: 采购收料通知单
 * @author Rao
 * @Date 2022/01/21
 **/
@NoArgsConstructor
@Data
public class PurchaseReceiveBillSourceOrder implements Serializable {

    private static final long serialVersionUID = 70990644348340884L;

    /**
     * 单据类型：FBillTypeID  (必填项)
     */
    @JSONField(name = "FBillTypeID")
    private FBillTypeID fBillTypeId;
    /**
     * 单据编号
     */
    @JSONField(name = "FBillNo")
    private String fBillNo;
    /**
     * 收料日期：FDate  (必填项)
     */
    @JSONField(name = "FDate")
    private String fDate;
    /**
     * FStockOrgId  (必填项)
     */
    @JSONField(name = "FStockOrgId")
    private Number fStockOrgId;
    @JSONField(name = "FReceiveDeptId")
    private Number fReceiveDeptId;
    @JSONField(name = "FStockGroupId")
    private Number fStockGroupId;
    @JSONField(name = "FReceiverId")
    private Number fReceiverId;
    @JSONField(name = "FDemandOrgId")
    private Number fDemandOrgId;
    @JSONField(name = "FCorrespondOrgId")
    private Number fCorrespondOrgId;
    /**
     * 采购组织：FPurOrgId  (必填项)
     */
    @JSONField(name = "FPurOrgId")
    private Number fPurOrgId;
    @JSONField(name = "FPurDeptId")
    private Number fPurDeptId;
    @JSONField(name = "FPurGroupId")
    private Number fPurGroupId;
    @JSONField(name = "FPurchaserId")
    private Number fPurchaserId;
    /**
     * 供应商：FSupplierId  (必填项)
     */
    @JSONField(name = "FSupplierId")
    private Number fSupplierId;

    @JSONField(name = "FNote")
    private String fNote;
    @JSONField(name = "FSupplyId")
    private Number fSupplyId;
    @JSONField(name = "FSupplyAddress")
    private String fSupplyAddress;
    @JSONField(name = "FSettleId")
    private Number fSettleId;
    @JSONField(name = "FChargeId")
    private Number fChargeId;
    @JSONField(name = "F_PAEZ_sfdj")
    private String fPaezSfdj;
    @JSONField(name = "F_PAEZ_sfytb")
    private String fPaezSfytb;
    /**
     * 货主类型：FOwnerTypeIdHead  (必填项)
     */
    @JSONField(name = "FOwnerTypeIdHead")
    private String fOwnerTypeIdHead;
    /**
     * 货主：FOwnerIdHead  (必填项)
     */
    @JSONField(name = "FOwnerIdHead")
    private Number fOwnerIdHead;
    /**
     * 物料分类：F_MS_WLFL  (必填项)
     */
    @JSONField(name = "F_MS_WLFL")
    private Number fMsWlfl;

    @JSONField(name = "FConfirmerId")
    private FConfirmerId fConfirmerId;
    @JSONField(name = "FConfirmDate")
    private String fConfirmDate;
    @JSONField(name = "FIsInsideBill")
    private String fIsInsideBill;
    @JSONField(name = "FIsMobile")
    private String fIsMobile;
    @JSONField(name = "FScanBox")
    private String fScanBox;
    /**
     * 供货方联系人
     */
    @JSONField(name = "FProviderContactId")
    private FProviderContactId fProviderContactId;

    @JSONField(name = "FIsChangeQty")
    private String fIsChangeQty;
    @JSONField(name = "F_PAEZ_DSFDH")
    private String fPaezDsfdh;

    /**
     * 验收方式
     */
    @JSONField(name = "FACCTYPE")
    private String facctype;

    /**
     * 财务信息
     */
    @JSONField(name = "FinanceEntity")
    private FinanceEntity financeEntity;
    /**
     * 明细信息
     */
    @JSONField(name = "FDetailEntity")
    private List<FDetailEntity> fDetailEntity;
    /**
     * 物流跟踪
     */
    @JSONField(name = "FReceiveTrace")
    private List<FReceiveTrace> fReceiveTrace;

    @NoArgsConstructor
    @Data
    public static class FBillTypeID {
        @JSONField(name = "FNUMBER")
        private String fnumber;
    }

    @NoArgsConstructor
    @Data
    public static class FConfirmerId {
        @JSONField(name = "FUserID")
        private String fUserID;
    }

    @NoArgsConstructor
    @Data
    public static class FProviderContactId {
        @JSONField(name = "FCONTACTNUMBER")
        private String fcontactnumber;
    }

    /**
     * 财务信息
     */
    @NoArgsConstructor
    @Data
    public static class FinanceEntity {
        /**
         * 实体主键
         */
        @JSONField(name = "FEntryId")
        private Integer fEntryId;
        /**
         * 结算组织
         */
        @JSONField(name = "FSettleOrgId")
        private Number fSettleOrgId;
        /**
         * 结算方式
         */
        @JSONField(name = "FSettleModeId")
        private Number fSettleModeId;
        /**
         * 结算币别
         */
        @JSONField(name = "FSettleCurrId")
        private Number fSettleCurrId;
        @JSONField(name = "FPayConditionId")
        private Number fPayConditionId;
        @JSONField(name = "FIsIncludedTax")
        private String fIsIncludedTax;
        @JSONField(name = "FPricePoint")
        private String fPricePoint;
        @JSONField(name = "FPriceListId")
        private Number fPriceListId;
        @JSONField(name = "FDiscountListId")
        private Number fDiscountListId;
        @JSONField(name = "FLocalCurrId")
        private Number fLocalCurrId;
        @JSONField(name = "FExchangeTypeId")
        private Number fExchangeTypeId;
        @JSONField(name = "FExchangeRate")
        private Integer fExchangeRate;
        @JSONField(name = "FISPRICEEXCLUDETAX")
        private String fispriceexcludetax;

    }

    @NoArgsConstructor
    @Data
    public static class FDetailEntity {
        @JSONField(name = "FEntryID")
        private Integer fEntryID;
        @JSONField(name = "FMaterialId")
        private Number fMaterialId;
        @JSONField(name = "FMaterialDesc")
        private String fMaterialDesc;
        @JSONField(name = "FUnitId")
        private Number fUnitId;
        @JSONField(name = "FActReceiveQty")
        private Integer fActReceiveQty;
        @JSONField(name = "FPreDeliveryDate")
        private String fPreDeliveryDate;
        @JSONField(name = "FSUPDELQTY")
        private Integer fsupdelqty;

        /**
         * 辅助属性
         */
        @JSONField(name = "FAuxPropId")
        private FAuxPropId fAuxPropId;

        @JSONField(name = "FPriceUnitId")
        private Number fPriceUnitId;
        @JSONField(name = "FStockID")
        private Number fStockID;
        @JSONField(name = "FStockLocId")
        private FStockLocId fStockLocId;
        @JSONField(name = "FStockStatusId")
        private Number fStockStatusId;
        @JSONField(name = "FLot")
        private Number fLot;
        @JSONField(name = "FRejectReason")
        private String fRejectReason;
        @JSONField(name = "FProduceDate")
        private String fProduceDate;
        @JSONField(name = "FDemandDeptId")
        private Number fDemandDeptId;
        @JSONField(name = "FDemanderId")
        private FDemanderId fDemanderId;
        @JSONField(name = "FGiveAway")
        private String fGiveAway;
        @JSONField(name = "FChargeProjectID")
        private Number fChargeProjectID;
        @JSONField(name = "FCtrlStockInPercent")
        private String fCtrlStockInPercent;
        @JSONField(name = "FMtoNo")
        private String fMtoNo;
        @JSONField(name = "FExtAuxUnitId")
        private Number fExtAuxUnitId;
        @JSONField(name = "FExtAuxUnitQty")
        private Integer fExtAuxUnitQty;
        @JSONField(name = "FProjectNo")
        private String fProjectNo;
        @JSONField(name = "FCheckJoinBaseQty")
        private Integer fCheckJoinBaseQty;
        @JSONField(name = "FReceiveBaseQty")
        private Integer fReceiveBaseQty;
        @JSONField(name = "FRefuseBaseQty")
        private Integer fRefuseBaseQty;
        @JSONField(name = "FSampleDamageBaseQty")
        private Integer fSampleDamageBaseQty;
        @JSONField(name = "FCheckJoinQty")
        private Integer fCheckJoinQty;
        @JSONField(name = "FReceiveQty")
        private Integer fReceiveQty;
        @JSONField(name = "FRefuseQty")
        private Integer fRefuseQty;
        @JSONField(name = "FSampleDamageQty")
        private Integer fSampleDamageQty;
        @JSONField(name = "FCheckInComing")
        private String fCheckInComing;
        @JSONField(name = "FDeliverySite")
        private String fDeliverySite;
        @JSONField(name = "FCardJoinQty")
        private Integer fCardJoinQty;
        @JSONField(name = "FCardQty")
        private Integer fCardQty;
        @JSONField(name = "FCardBaseQty")
        private Integer fCardBaseQty;
        @JSONField(name = "FCardJoinBaseQty")
        private Integer fCardJoinBaseQty;
        @JSONField(name = "FCheckBaseQty")
        private Integer fCheckBaseQty;
        @JSONField(name = "FCheckQty")
        private Integer fCheckQty;
        @JSONField(name = "FTransferBaseQty")
        private Integer fTransferBaseQty;
        @JSONField(name = "FIsReceiveUpdateStock")
        private String fIsReceiveUpdateStock;
        @JSONField(name = "FTransferQty")
        private Integer fTransferQty;
        @JSONField(name = "FBomId")
        private Number fBomId;
        @JSONField(name = "FSupplierLot")
        private String fSupplierLot;
        @JSONField(name = "FExpiryDate")
        private String fExpiryDate;
        @JSONField(name = "FAuxUnitQty")
        private Integer fAuxUnitQty;
        @JSONField(name = "FDescription")
        private String fDescription;
        @JSONField(name = "FStockInMaxQty")
        private Integer fStockInMaxQty;
        @JSONField(name = "FStockInMinQty")
        private Integer fStockInMinQty;
        @JSONField(name = "FEntryTaxRate")
        private Integer fEntryTaxRate;
        @JSONField(name = "FTaxCombination")
        private Number fTaxCombination;
        @JSONField(name = "FDiscountRate")
        private Integer fDiscountRate;
        @JSONField(name = "FPrice")
        private Integer fPrice;
        @JSONField(name = "FTaxPrice")
        private Integer fTaxPrice;
        @JSONField(name = "FProcScrapBaseQty")
        private Integer fProcScrapBaseQty;
        @JSONField(name = "FProcScrapQty")
        private Integer fProcScrapQty;
        @JSONField(name = "FMtrlScrapBaseQty")
        private Integer fMtrlScrapBaseQty;
        @JSONField(name = "FMtrlScrapQty")
        private Integer fMtrlScrapQty;
        @JSONField(name = "FCsnReceiveBaseQty")
        private Integer fCsnReceiveBaseQty;
        @JSONField(name = "FCsnReceiveQty")
        private Integer fCsnReceiveQty;
        @JSONField(name = "FRefuseJoinBaseQty")
        private Integer fRefuseJoinBaseQty;
        @JSONField(name = "FRefuseJoinQty")
        private Integer fRefuseJoinQty;
        @JSONField(name = "FCsnReceiveJoinBaseQty")
        private Integer fCsnReceiveJoinBaseQty;
        @JSONField(name = "FCsnReceiveJoinQty")
        private Integer fCsnReceiveJoinQty;
        @JSONField(name = "FPriceBaseQty")
        private Integer fPriceBaseQty;
        @JSONField(name = "FSetPriceUnitID")
        private Number fSetPriceUnitID;
        @JSONField(name = "FStockUnitID")
        private Number fStockUnitID;
        @JSONField(name = "FStockQty")
        private Integer fStockQty;
        @JSONField(name = "FStockBaseQty")
        private Integer fStockBaseQty;
        @JSONField(name = "FActlandQty")
        private Integer fActlandQty;
        @JSONField(name = "FConfirmDeliQty")
        private Integer fConfirmDeliQty;
        @JSONField(name = "FConfirmDeliDate")
        private String fConfirmDeliDate;
        @JSONField(name = "FConfirmInfo")
        private String fConfirmInfo;
        @JSONField(name = "FPriceDiscount")
        private Integer fPriceDiscount;
        @JSONField(name = "FACCRATE")
        private Integer faccrate;
        @JSONField(name = "FTaxDetailSubEntity")
        private List<FTaxDetailSubEntity> fTaxDetailSubEntity;
        @JSONField(name = "FSerialSubEntity")
        private List<FSerialSubEntity> fSerialSubEntity;

        @NoArgsConstructor
        @Data
        public static class FAuxPropId {
        }

        @NoArgsConstructor
        @Data
        public static class FStockLocId {
        }

        @NoArgsConstructor
        @Data
        public static class FDemanderId {
        }

        @NoArgsConstructor
        @Data
        public static class FTaxDetailSubEntity {
            @JSONField(name = "FDetailID")
            private Integer fDetailID;
            @JSONField(name = "FTaxRate")
            private Integer fTaxRate;
        }

        @NoArgsConstructor
        @Data
        public static class FSerialSubEntity {
            @JSONField(name = "FDetailID")
            private Integer fDetailID;
            @JSONField(name = "FSerialNo")
            private String fSerialNo;
            @JSONField(name = "FSerialNote")
            private String fSerialNote;
        }
    }

    @NoArgsConstructor
    @Data
    public static class FReceiveTrace {
        @JSONField(name = "FEntryID")
        private Integer fEntryID;
        @JSONField(name = "FLogComId")
        private FLogComId fLogComId;
        @JSONField(name = "FCarryBillNo")
        private String fCarryBillNo;
        @JSONField(name = "FTraceDetail")
        private List<FTraceDetail> fTraceDetail;

        @NoArgsConstructor
        @Data
        public static class FLogComId {
            @JSONField(name = "FCODE")
            private String fcode;
        }

        @NoArgsConstructor
        @Data
        public static class FTraceDetail {
            @JSONField(name = "FDetailID")
            private Integer fDetailID;
        }
    }
}
