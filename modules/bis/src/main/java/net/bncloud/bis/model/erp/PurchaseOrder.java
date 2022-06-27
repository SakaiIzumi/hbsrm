package net.bncloud.bis.model.erp;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.bncloud.msk3cloud.core.fieldparse.FieldKey;
import net.bncloud.msk3cloud.kingdee.entity.common.Number;

import java.io.Serializable;
import java.util.List;

/**
 * desc: Erp采购订单
 *
 * @author Rao
 * @Date 2022/01/18
 **/
@NoArgsConstructor
@Data
public class PurchaseOrder implements Serializable {

    private static final long serialVersionUID = -2877350031211962767L;
    /**
     * 实体主键
     */
    @FieldKey("FID")
    @JSONField(name = "FID")
    private Long fid;

    /**
     *  单据类型：FBillTypeID  (必填项)
     */
    @FieldKey("FBillTypeID")
    @JSONField(name = "FBillTypeID")
    private Number fBillTypeID;

    /**
     * 单据状态：FDocumentStatus
     */
    @FieldKey("FDocumentStatus")
    @JSONField(name = "FDocumentStatus")
    private String fDocumentStatus;

    /**
     * 单据编号
     */
    @FieldKey("FBillNo")
    @JSONField(name = "FBillNo")
    private String fBillNo;

    /**
     * 物料分类：F_MS_WLFL  (必填项)
     */
    @FieldKey("F_MS_WLFL")
    @JSONField(name = "F_MS_WLFL")
    private Number fMsWlfl;

    /**
     * 采购日期
     */
    @FieldKey("FDate")
    @JSONField(name = "FDate")
    private String fDate;

    /**
     *  供应商：FSupplierId  (必填项)
     */
    @FieldKey("FSupplierId")
    @JSONField(name = "FSupplierId")
    private Number fSupplierId;

    /**
     *  采购组织：FPurchaseOrgId  (必填项)
     */
    @FieldKey("FPurchaseOrgId")
    @JSONField(name = "FPurchaseOrgId")
    private Number fPurchaseOrgId;

    /**
     * 采购部门：FPurchaseDeptId
     */
    @FieldKey("FPurchaseDeptId")
    @JSONField(name = "FPurchaseDeptId")
    private Number fPurchaseDeptId;

    /**
     * 采购组：FPurchaserGroupId
     */
    @FieldKey("FPurchaserGroupId")
    @JSONField(name = "FPurchaserGroupId")
    private Number fPurchaserGroupId;

    /**
     * 采购员：FPurchaserId
     */
    @FieldKey("FPurchaserId")
    @JSONField(name = "FPurchaserId")
    private Number fPurchaserId;

    /**
     * 供货方：FProviderId
     */
    @FieldKey("FProviderId")
    @JSONField(name = "FProviderId")
    private Number fProviderId;

    /**
     * 供货方联系人：FProviderContactId
     */
    @FieldKey("FProviderContactId")
    @JSONField(name = "FProviderContactId")
    private FProviderContactId fProviderContactId;

    @FieldKey("FProviderAddress")
    @JSONField(name = "FProviderAddress")
    private String fProviderAddress;

    @FieldKey("FSettleId")
    @JSONField(name = "FSettleId")
    private Number fSettleId;

    @FieldKey("FChargeId")
    @JSONField(name = "FChargeId")
    private Number fChargeId;
    @JSONField(name = "FConfirmerId")
    private FConfirmerId fConfirmerId;
    @JSONField(name = "FConfirmDate")
    private String fConfirmDate;
    @JSONField(name = "FCorrespondOrgId")
    private Number fCorrespondOrgId;
    @JSONField(name = "FProviderContact")
    private String fProviderContact;
    @JSONField(name = "FIsModificationOperator")
    private String fIsModificationOperator;
    @JSONField(name = "FChangeStatus")
    private String fChangeStatus;

    @FieldKey("F_MS_DDBH")
    @JSONField(name = "F_MS_DDBH")
    private String fMsDdbh;

    @FieldKey("FACCTYPE")
    @JSONField(name = "FACCTYPE")
    private String facctype;

    @JSONField(name = "F_abc_Remarks")
    private String fAbcRemarks;

    /**
     * 运输方式
     */
    @FieldKey("F_abc_Combo")
    @JSONField(name = "F_abc_Combo")
    private String fAbcCombo;

    @JSONField(name = "F_abc_Text")
    private String fAbcText;
    @JSONField(name = "F_MS_MSCGY")
    private String fMsMscgy;

    @FieldKey("F_abc_JHDD")
    @JSONField(name = "F_abc_JHDD")
    private String fAbcJhdd;

    @JSONField(name = "F_abc_CheckBox")
    private String fAbcCheckbox;

    /**
     * 财务信息
     */
    @JSONField(name = "FPOOrderFinance")
    private FPOOrderFinance fPOOrderFinance;

    /**
     * 明细
     */
    @JSONField(name = "FPOOrderEntry")
    private List<FPOOrderEntry> fPOOrderEntry;


    /**
     * 付款方式：F_abc_BaseProperty1
     */
    @FieldKey("F_abc_BaseProperty1")
    @JSONField(name = "F_abc_BaseProperty1")
    private String fAbcbaseProperty1;

    /**
     * 账期(天)：F_abc_BaseProperty
     */
    @FieldKey("F_abc_BaseProperty")
    @JSONField(name = "F_abc_BaseProperty")
    private String fAbcbaseProperty;

    /**
     *  变更原因：FChangeReason
     */
    @FieldKey("FChangeReason")
    @JSONField(name = "FChangeReason")
    private String fChangeReason;

    @FieldKey("FChangeDate")
    @JSONField(name = "FChangeDate")
    private String fChangeDate;


    @NoArgsConstructor
    @Data
    public static class FProviderContactId {
        @JSONField(name = "FCONTACTNUMBER")
        private String fcontactnumber;
    }

    @NoArgsConstructor
    @Data
    public static class FConfirmerId {
        @JSONField(name = "FUserID")
        private String fUserID;
    }




    @NoArgsConstructor
    @Data
    public static class FPOOrderFinance {

        @FieldKey("FEntryID")
        @JSONField(name = "FEntryId")
        private Long fEntryId;

        /**
         * 结算币别
         */
        @FieldKey("FSettleModeId")
        @JSONField(name = "FSettleModeId")
        private Number fSettleModeId;

        /**
         * 付款条件
         */
        @FieldKey("FPayConditionId ")
        @JSONField(name = "FPayConditionId")
        private Number fPayConditionId;

        @JSONField(name = "FSettleCurrId")
        private Number fSettleCurrId;
        @JSONField(name = "FExchangeTypeId")
        private Number fExchangeTypeId;
        @JSONField(name = "FExchangeRate")
        private Integer fExchangeRate;
        @JSONField(name = "FPriceListId")
        private Number fPriceListId;
        @JSONField(name = "FDiscountListId")
        private Number fDiscountListId;
        @JSONField(name = "FPriceTimePoint")
        private String fPriceTimePoint;
        @JSONField(name = "FFOCUSSETTLEORGID")
        private Number ffocussettleorgid;
        @JSONField(name = "FIsIncludedTax")
        private String fIsIncludedTax;
        @JSONField(name = "FISPRICEEXCLUDETAX")
        private String fispriceexcludetax;
        @JSONField(name = "FLocalCurrId")
        private Number fLocalCurrId;
        @JSONField(name = "FPAYADVANCEAMOUNT")
        private Integer fpayadvanceamount;
        @JSONField(name = "FSupToOderExchangeBusRate")
        private Integer fSupToOderExchangeBusRate;
        @JSONField(name = "FSEPSETTLE")
        private String fsepsettle;
        @JSONField(name = "FDepositRatio")
        private Integer fDepositRatio;

    }

    @NoArgsConstructor
    @Data
    public static class FPOOrderEntry {

        @JSONField(name = "FEntryID")
        private Long fEntryID;

        @JSONField(name = "FProductType")
        private String fProductType;

        @FieldKey("FMaterialId")
        @JSONField(name = "FMaterialId")
        private Number fMaterialId;

        @JSONField(name = "FBomId")
        private Number fBomId;
        @JSONField(name = "FMaterialDesc")
        private String fMaterialDesc;
        @JSONField(name = "FAuxPropId")
        private Number fAuxPropId;

        @FieldKey("FUnitId")
        @JSONField(name = "FUnitId")
        private Number fUnitId;

        /**
         * 采购数量
         */
        @FieldKey("FQty")
        @JSONField(name = "FQty")
        private Integer fQty;

        @FieldKey("FPriceUnitId")
        @JSONField(name = "FPriceUnitId")
        private Number fPriceUnitId;

        @FieldKey("FPriceUnitQty")
        @JSONField(name = "FPriceUnitQty")
        private Integer fPriceUnitQty;


        @JSONField(name = "FPriceBaseQty")
        private Integer fPriceBaseQty;

        /**
         * 交货日期
         */
        @FieldKey("FDeliveryDate")
        @JSONField(name = "FDeliveryDate")
        private String fDeliveryDate;


        @JSONField(name = "FLocation")
        private String fLocation;
        @JSONField(name = "FLocationAddress")
        private String fLocationAddress;

        /**
         * 单价
         */
        @FieldKey("FPrice")
        @JSONField(name = "FPrice")
        private Integer fPrice;

        /**
         * 含税单价
         */
        @FieldKey("FTaxPrice")
        @JSONField(name = "FTaxPrice")
        private Integer fTaxPrice;

        @FieldKey("FEntryAmount")
        @JSONField(name = "FEntryAmount")
        private Integer fEntryAmount;

        @JSONField(name = "FEntryDiscountRate")
        private Integer fEntryDiscountRate;
        @JSONField(name = "FTaxCombination")
        private Number fTaxCombination;

        @FieldKey("FEntryTaxRate")
        @JSONField(name = "FEntryTaxRate")
        private Integer fEntryTaxRate;

        @JSONField(name = "FRequireOrgId")
        private Number fRequireOrgId;
        @JSONField(name = "FRequireDeptId")
        private Number fRequireDeptId;
        @JSONField(name = "FRequireStaffId")
        private Number fRequireStaffId;
        @JSONField(name = "FReceiveOrgId")
        private Number fReceiveOrgId;
        @JSONField(name = "FReceiveDeptId")
        private FReceiveDeptId fReceiveDeptId;
        @JSONField(name = "FEntrySettleOrgId")
        private Number fEntrySettleOrgId;
        @JSONField(name = "FGiveAway")
        private String fGiveAway;
        @JSONField(name = "FEntryNote")
        private String fEntryNote;
        @JSONField(name = "FSupMatId")
        private String fSupMatId;
        @JSONField(name = "FSupMatName")
        private String fSupMatName;
        @JSONField(name = "FStockUnitID")
        private Number fStockUnitID;
        @JSONField(name = "FStockQty")
        private Integer fStockQty;
        @JSONField(name = "FStockBaseQty")
        private Integer fStockBaseQty;
        @JSONField(name = "FLot")
        private Number fLot;
        @JSONField(name = "FSupplierLot")
        private String fSupplierLot;
        @JSONField(name = "FProcesser")
        private Number fProcesser;
        @JSONField(name = "FDeliveryControl")
        private String fDeliveryControl;
        @JSONField(name = "FTimeControl")
        private String fTimeControl;
        @JSONField(name = "FDeliveryMaxQty")
        private Integer fDeliveryMaxQty;
        @JSONField(name = "FDeliveryMinQty")
        private Integer fDeliveryMinQty;
        @JSONField(name = "FDeliveryBeforeDays")
        private Integer fDeliveryBeforeDays;
        @JSONField(name = "FDeliveryDelayDays")
        private Integer fDeliveryDelayDays;

        @FieldKey("FMaterialName")
        @JSONField(name = "FMaterialName")
        private String fMaterialName;

        /**
         * 缺失字段
         * 	 控制交货时间：FTimeControl
         * 	 物料名称：FMaterialName
         * 	 规格型号：FModel
         * 	 物料类别：FMaterialType
         */
        @FieldKey("FModel")
        @JSONField(name = "FModel")
        private String fModel;

        @FieldKey("FMaterialType")
        @JSONField(name = "FMaterialType")
        private String fMaterialType;

        @JSONField(name = "FDeliveryEarlyDate")
        private String fDeliveryEarlyDate;
        @JSONField(name = "FDeliveryLastDate")
        private String fDeliveryLastDate;
        @JSONField(name = "FPriceCoefficient")
        private Integer fPriceCoefficient;
        @JSONField(name = "FEntrySettleModeId")
        private Number fEntrySettleModeId;
        @JSONField(name = "FConsumeSumQty")
        private Integer fConsumeSumQty;
        @JSONField(name = "FContractNo")
        private String fContractNo;
        @JSONField(name = "FReqTraceNo")
        private String fReqTraceNo;
        @JSONField(name = "FMtoNo")
        private String fMtoNo;
        @JSONField(name = "FDEMANDTYPE")
        private String fdemandtype;
        @JSONField(name = "FDEMANDBILLNO")
        private String fdemandbillno;
        @JSONField(name = "FDEMANDBILLENTRYSEQ")
        private Integer fdemandbillentryseq;
        @JSONField(name = "FDEMANDBILLENTRYID")
        private Integer fdemandbillentryid;
        @JSONField(name = "FLocationId")
        private Number fLocationId;
        @JSONField(name = "FPlanConfirm")
        private String fPlanConfirm;
        @JSONField(name = "FSalUnitID")
        private Number fSalUnitID;
        @JSONField(name = "FSalQty")
        private Integer fSalQty;
        @JSONField(name = "FSalJoinQty")
        private Integer fSalJoinQty;
        @JSONField(name = "FBaseSalJoinQty")
        private Integer fBaseSalJoinQty;
        @JSONField(name = "FSetPriceUnitID")
        private Number fSetPriceUnitID;
        @JSONField(name = "FInventoryQty")
        private Integer fInventoryQty;
        @JSONField(name = "FChargeProjectID")
        private Number fChargeProjectID;
        @JSONField(name = "FCentSettleOrgId")
        private Number fCentSettleOrgId;
        @JSONField(name = "FDispSettleOrgId")
        private Number fDispSettleOrgId;
        @JSONField(name = "FGroup")
        private Integer fGroup;
        @JSONField(name = "FDeliveryStockStatus")
        private Number fDeliveryStockStatus;
        @JSONField(name = "FMaxPrice")
        private Integer fMaxPrice;
        @JSONField(name = "FMinPrice")
        private Integer fMinPrice;
        @JSONField(name = "FIsStock")
        private String fIsStock;
        @JSONField(name = "FBaseConsumeSumQty")
        private Integer fBaseConsumeSumQty;
        @JSONField(name = "FSalBaseQty")
        private Integer fSalBaseQty;
        @JSONField(name = "FSubOrgId")
        private Number fSubOrgId;
        @JSONField(name = "FEntryPayOrgId")
        private Number fEntryPayOrgId;
        @JSONField(name = "FPriceDiscount")
        private Integer fPriceDiscount;
        @JSONField(name = "FEntryDeliveryPlan")
        private List<FEntryDeliveryPlan> fEntryDeliveryPlan;
        @JSONField(name = "FTaxDetailSubEntity")
        private List<FTaxDetailSubEntity> fTaxDetailSubEntity;

        /**
         *  税额：FEntryTaxAmount
         */
        @FieldKey("FEntryTaxAmount")
        @JSONField(name = "FEntryTaxAmount")
        private Integer fEntryTaxAmount;

        /**
         * 价税合计：FAllAmount
         */
        @FieldKey("FAllAmount")
        @JSONField(name = "FAllAmount")
        private Integer fAllAmount;

        @JSONField(name = "FChangeFlag")
        private String fChangeFlag;

        @NoArgsConstructor
        @Data
        public static class FReceiveDeptId {
            @JSONField(name = "FNUMBER")
            private String fnumber;
        }

        @NoArgsConstructor
        @Data
        public static class FEntryDeliveryPlan {
            @JSONField(name = "FDetailId")
            private Long fDetailId;
            @JSONField(name = "FDeliveryDate_Plan")
            private String fdeliverydatePlan;
            @JSONField(name = "FPlanQty")
            private Integer fPlanQty;
            @JSONField(name = "FELocation")
            private String fELocation;
            @JSONField(name = "FELocationAddress")
            private String fELocationAddress;
            @JSONField(name = "FSUPPLIERDELIVERYDATE")
            private String fsupplierdeliverydate;
            @JSONField(name = "FPREARRIVALDATE")
            private String fprearrivaldate;
            @JSONField(name = "FTRLT")
            private Integer ftrlt;
            @JSONField(name = "FConfirmDeliQty")
            private Integer fConfirmDeliQty;
            @JSONField(name = "FConfirmDeliDate")
            private String fConfirmDeliDate;
            @JSONField(name = "FConfirmInfo")
            private String fConfirmInfo;
            @JSONField(name = "FELocationId")
            private Number fELocationId;
        }

        @NoArgsConstructor
        @Data
        public static class FTaxDetailSubEntity {
            @JSONField(name = "FDetailID")
            private Long fDetailID;
            @JSONField(name = "FTaxRateId")
            private Number fTaxRateId;
            @JSONField(name = "FTaxRate")
            private Integer fTaxRate;

        }
    }

}
