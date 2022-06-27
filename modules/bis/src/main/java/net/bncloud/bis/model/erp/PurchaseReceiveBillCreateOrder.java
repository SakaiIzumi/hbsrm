package net.bncloud.bis.model.erp;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.bncloud.msk3cloud.core.fieldparse.FieldKey;
import net.bncloud.msk3cloud.kingdee.entity.common.Number;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * desc: 采购收料通知单创建订单 Gson很坑，先弄其他
 *
 * @author Rao
 * @Date 2022/01/21
 **/
@NoArgsConstructor
@Data
public class PurchaseReceiveBillCreateOrder implements Serializable {

    private static final long serialVersionUID = -89545879280488611L;

    @SerializedName("FID")
    @FieldKey("FID")
    private Long fId;

    /**
     * 单据编号
     */
    @FieldKey("FBillNo")
    @SerializedName("FBillNo")
    private String fBillNo;


    /**
     * 单据类型
     */
    @SerializedName("FBillTypeID")
    @FieldKey("FBillTypeID")
    private Number fBillTypeId;
    /**
     * 收料日期
     */
    @SerializedName("FDate")
    @FieldKey("FDate")
    private String fDate;
    /**
     * 收料组织
     */
    @SerializedName( "FStockOrgId")
    @FieldKey("FStockOrgId")
    private Number fStockOrgId;
    /**
     * 需求组织
     */
    @SerializedName( "FDemandOrgId")
    @FieldKey("FDemandOrgId")
    private Number fDemandOrgId;
    /**
     * 采购组织
     */
    @SerializedName("FPurOrgId")
    @FieldKey("FPurOrgId")
    private Number fPurOrgId;
    /**
     * 供应商 (必填)
     */
    @SerializedName("FSupplierId")
    @FieldKey("FSupplierId")
    private Number fSupplierId;
//    /**
//     * 供货方
//     */
//    @SerializedName( "FSupplyId")
//    private Number fSupplyId;
//    /**
//     * 结算方
//     */
//    @SerializedName( "FSettleId")
//    private Number fSettleId;
//    /**
//     * 收款方
//     */
//    @SerializedName( "FChargeId")
//    private Number fChargeId;
//    /**
//     * 是否对接
//     */
//    @SerializedName( "F_PAEZ_sfdj")
//    private String fPaezSfdj;
//    /**
//     * 货主类型
//     */
//    @SerializedName( "FOwnerTypeIdHead")
//    private String fOwnerTypeIdHead;
    /**
     * 货主 (必填)
     */
    @SerializedName("FOwnerIdHead")
    @FieldKey("FOwnerIdHead")
    private Number fOwnerIdHead;
    /**
     * 物料分类 （必填）
     */
    @SerializedName("F_MS_WLFL")
    @FieldKey("F_MS_WLFL")
    private Number fMsWlfl;
//    /**
//     * 外部单据
//     */
//    @SerializedName( "FIsInsideBill")
//    private Boolean fIsInsideBill;
//    /**
//     * 来自移动
//     */
//    @SerializedName( "FIsMobile")
//    private Boolean fIsMobile;
//    /**
//     * 变更数量操作
//     */
//    @SerializedName( "FIsChangeQty")
//    private Boolean fIsChangeQty;
//    /**
//     * 验收方式
//     */
//    @SerializedName( "FACCTYPE")
//    private String facctype;
//    /**
//     * 财务信息
//     */
//    @SerializedName( "FinanceEntity")
//    private FinanceEntity financeEntity;

    /**
     * 业务类型：FBusinessType
     * 委外：WW
     * 采购：CG
     * 枚举值，不用Number
     */
    @FieldKey("FBusinessType")
    @SerializedName("FBusinessType")
    private String fBusinessType;

    /**
     * 明细信息
     */
    @SerializedName("FDetailEntity")
    private List<FDetailEntity> fDetailEntity;

//    @NoArgsConstructor
//    @Data
//    public static class FinanceEntity {
//        /**
//         * 结算组织
//         */
//        @SerializedName( "FSettleOrgId")
//        private Number fSettleOrgId;
//        /**
//         * 结算币别
//         */
//        @SerializedName( "FSettleCurrId")
//        private Number fSettleCurrId;
//        /**
//         * 含税
//         */
//        @SerializedName( "FIsIncludedTax")
//        private Boolean fIsIncludedTax;
//        /**
//         * 定价时点
//         */
//        @SerializedName( "FPricePoint")
//        private String fPricePoint;
//        /**
//         * 本位币
//         */
//        @SerializedName( "FLocalCurrId")
//        private Number fLocalCurrId;
//        /**
//         * 汇率类型
//         */
//        @SerializedName( "FExchangeTypeId")
//        private Number fExchangeTypeId;
//        /**
//         * 汇率
//         */
//        @SerializedName( "FExchangeRate")
//        private Integer fExchangeRate;
//        /**
//         * 价外税
//         */
//        @SerializedName( "FISPRICEEXCLUDETAX")
//        private Boolean fispriceexcludetax;
//
//    }

    @NoArgsConstructor
    @Data
    @FieldKey("FDetailEntity")
    public static class FDetailEntity {

        /**
         * 主键ID
         */
        @FieldKey("FEntryID")
        @SerializedName("FEntryID")
        private Long fEntryId;

        /**
         * 物料编码 （必填）
         */
        @SerializedName("FMaterialId")
        @FieldKey("FMaterialId")
        private Number fMaterialId;
//        /**
//         * 物料说明
//         */
//        @SerializedName( "FMaterialDesc")
//        private String fMaterialDesc;
        /**
         * 收料单位 （必填）
         */
        @SerializedName("FUnitId")
        @FieldKey("FUnitId")
        private Number fUnitId;
        /**
         * 交货数量 （必填）
         */
        @SerializedName("FActReceiveQty")
        @FieldKey("FActReceiveQty")
        private String fActReceiveQty;
        /**
         * 预计到货日期 (日期无法处理)
         */
        @SerializedName("FPreDeliveryDate")
//        @FieldKey( "FPreDeliveryDate")
        private String fPreDeliveryDate;
        /**
         * 供应商交货数量
         */
        @SerializedName("FSUPDELQTY")
        @FieldKey("FSUPDELQTY")
        private String fsupdelqty;
        /**
         * 计价单位
         */
        @SerializedName("FPriceUnitId")
        @FieldKey("FPriceUnitId")
        private Number fPriceUnitId;
        /**
         * 仓库 （必填）
         */
        @SerializedName("FStockID")
        @FieldKey("FStockID")
        private Number fStockId;

        // ---- 业务类型相关  委派订单

        /**
         * 采购单号
         */
        @SerializedName("FSrcBillNo")
        private String fSrcBillNo;

        /**
         * 源单单号
         */
        @SerializedName("FOrderBillNo")
        private String fOrderBillNo;

        /**
         * 源单单据类型
         */
        @SerializedName("FSrcFormId")
        private String fSrcFormId = "PUR_PurchaseOrder";

        /**
         * 源单ID
         */
        @SerializedName("FSrcId")
        private Long fSrcId;

        /**
         * 源单分录ID
         */
        @SerializedName("FSrcEntryId")
        private Long fSrcEntryId;

        /**
         * 源单分录ID
         */
        @SerializedName("FPOORDERENTRYID")
        private Long FPOORDERENTRYID;

        // ------------

//        /**
//         * 库存状态
//         */
//        @SerializedName( "FStockStatusId")
//        private Number fStockStatusId;
//        /**
//         * 是否赠品
//         */
//        @SerializedName( "FGiveAway")
//        private Boolean fGiveAway;
//        /**
//         * 控制入库数量
//         */
//        @SerializedName( "FCtrlStockInPercent")
//        private Boolean fCtrlStockInPercent;
//        /**
//         * 来料检验
//         */
//        @SerializedName( "FCheckInComing")
//        private Boolean fCheckInComing;
//        /**
//         * 收料更新库存(废弃)
//         */
//        @SerializedName( "FIsReceiveUpdateStock")
//        private Boolean fIsReceiveUpdateStock;
//        /**
//         * 入库上限
//         */
//        @SerializedName( "FStockInMaxQty")
//        private Integer fStockInMaxQty;
//        /**
//         * 入库下限
//         */
//        @SerializedName( "FStockInMinQty")
//        private Integer fStockInMinQty;
//        /**
//         * 税率%
//         */
//        @SerializedName( "FEntryTaxRate")
//        private Integer fEntryTaxRate;
        /**
         * 计价基本数量
         */
        @SerializedName("FPriceBaseQty")
        @FieldKey("FPriceBaseQty")
        private String fPriceBaseQty;
        /**
         * 库存单位
         */
        @SerializedName("FStockUnitID")
        @FieldKey("FStockUnitID")
        private Number fStockUnitId;
        /**
         * 库存单位数量
         */
        @SerializedName("FStockQty")
        @FieldKey("FStockQty")
        private String fStockQty;
//        /**
//         * 库存基本数量
//         */
//        @SerializedName( "FStockBaseQty")
//        private Integer fStockBaseQty;

        /**
         * 实到数量
         */
        @SerializedName("FActlandQty")
        @FieldKey("FActlandQty")
        private String fActlandQty;

        /**
         * 单价
         */
        @SerializedName("FPrice")
        private BigDecimal fPrice;

        /**
         * 含税单价
         */
        @SerializedName("FTaxPrice")
        private BigDecimal fTaxPrice;

        // /**
        //  * 商家编码（条码）
        //  */
        // @SerializedName("F_MS_SJBM")
        // private String fMssjbm;

        /**
         * 	 批号： FLot
         */
        @SerializedName("FLot")
        private Number fLot;


        /**
         * 关联关系信息
         */
        @SerializedName("FDetailEntity_Link")
        @FieldKey("FDetailEntity_Link")
        private List<FDetailEntityLink> fDetailEntityLinkList;

        /**
         * 仓位编码
         * 在美尚测试环境中如果送货单中的采购方为800（美尚（广州）化妆品制造有限公司 ），则需固定传入仓位编码值4000-0001
         */
        @SerializedName("FStockLocId")
        private FStockLocId stockLocId;

    }

    @NoArgsConstructor
    @Data
    public static class FStockLocId {

        @SerializedName("FSTOCKLOCID__FF100002")
        private Number fstocklocidFf100002;
        @SerializedName("FSTOCKLOCID__FF100005")
        private Number fstocklocidFf100005;
        @SerializedName("FSTOCKLOCID__FF100006")
        private Number fstocklocidFf100006;
        @SerializedName("FSTOCKLOCID__FF100007")
        private Number fstocklocidFf100007;
        @SerializedName("FSTOCKLOCID__FF100008")
        private Number fstocklocidFf100008;
        @SerializedName("FSTOCKLOCID__FF100010")
        private Number fstocklocidFf100010;
        @SerializedName("FSTOCKLOCID__FF100012")
        private Number fstocklocidFf100012;
        @SerializedName("FSTOCKLOCID__FF100013")
        private Number fstocklocidFf100013;
        @SerializedName("FSTOCKLOCID__FF100014")
        private Number fstocklocidFf100014;
        @SerializedName("FSTOCKLOCID__FF100015")
        private Number fstocklocidFf100015;

    }

    /**
     * ruleid：PUR_PurchaseOrder-PUR_ReceiveBill
     * tablename：t_PUR_POOrderEntry
     * 上查单据信息关联
     */
    @NoArgsConstructor
    @Data
    @FieldKey("FDetailEntity_Link")
    public static class FDetailEntityLink {

        /**
         * 转换规则
         */
        @FieldKey("FDetailEntity_Link_FRuleId")
        @SerializedName("FDetailEntity_Link_FRuleId")
        private String FDetailEntity_Link_FRuleId = "PUR_PurchaseOrder-PUR_ReceiveBill";

        /**
         * 源单表 :
         */
        @SerializedName("FDetailEntity_Link_FSTableName")
        @FieldKey("FDetailEntity_Link_FSTableName")
        private String FDetailEntity_Link_FSTableName = "t_PUR_POOrderEntry";

        /**
         * 源单内码 -> 采购订单的单据编码
         */
        @SerializedName("FDetailEntity_Link_FSBillId")
        @FieldKey("FDetailEntity_Link_FSBillId")
        private Long FDetailEntity_Link_FSBillId;

        /**
         * 源单分录内码 -> 采购订单的明细ID
         */
        @SerializedName("FDetailEntity_Link_FSId")
        @FieldKey("FDetailEntity_Link_FSId")
        private Long FDetailEntity_Link_FSId;

        /**
         * 原始携带量  --> 交货数量
         */
        @SerializedName("FDetailEntity_Link_FBaseUnitQtyOld")
        @FieldKey("FDetailEntity_Link_FBaseUnitQtyOld")
        private String FDetailEntity_Link_FBaseUnitQtyOld;

        /**
         * 修改携带量 --> 交货数量修改
         */
        @SerializedName("FDetailEntity_Link_FBaseUnitQty")
        @FieldKey("FDetailEntity_Link_FBaseUnitQty")
        private String FDetailEntity_Link_FBaseUnitQty;

    }


}


