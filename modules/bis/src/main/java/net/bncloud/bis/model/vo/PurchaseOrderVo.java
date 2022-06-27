package net.bncloud.bis.model.vo;

import lombok.Data;
import net.bncloud.msk3cloud.core.fieldparse.FieldKey;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * desc: 采购订单信息
 *  聚合
 *  需要留意的是，改变字段的同时需要修改表结构
 *
 * @author Rao
 * @Date 2022/01/18
 **/
@Data
public class PurchaseOrderVo implements Serializable {

    private static final long serialVersionUID = -5161105228138516572L;

    /**
     * 实体主键  计划id
     */
    @FieldKey("FID")
    private Long fid;

    /**
     * 单据状态：FDocumentStatus
     */
    @FieldKey("FDocumentStatus")
    private String fDocumentStatus;

    /**
     * 关闭状态：FCloseStatus
     */
    @FieldKey("FCloseStatus")
    private String fCloseStatus;

    /**
     * 关闭日期：FCloseDate
     */
    @FieldKey("FCloseDate")
    private LocalDateTime FCloseDate;

    /**
     *  供应商：FSupplierId  (必填项)
     */
    @FieldKey("FSupplierId.fnumber")
    private String fSupplierId;

    /**
     *  采购组织：FPurchaseOrgId  (必填项)
     */
    @FieldKey("FPurchaseOrgId.fnumber")
    private String fPurchaseOrgId;

    /**
     * 单据编号
     */
    @FieldKey("FBillNo")
    private String fBillNo;

    /**
     * 采购日期
     */
    @FieldKey("FDate")
    private String fDate;

    @FieldKey("F_MS_MSCGY")
    private String fMsMscgy;

    /**
     * 采购部门：FPurchaseDeptId
     */
    @FieldKey("FPurchaseDeptId.fnumber")
    private String fPurchaseDeptId;


    @FieldKey("F_abc_JHDD")
    private String fAbcJhdd;

    /**
     * 备注
     */
    @FieldKey("F_abc_Remarks")
    private String fAbcRemarks;

    /**
     * 单据类型：FBillTypeID  (必填项)
     */
    @FieldKey("FBillTypeID.fnumber")
    private String fBillTypeID;

    /**
     * 订单编号 PO
     */
    @FieldKey("F_MS_DDBH")
    private String fMsDdbh;

    /**
     * 付款方式：F_abc_BaseProperty1
     */
    @FieldKey("F_abc_BaseProperty1")
    private String fAbcbaseProperty1;

    /**
     * 账期(天)：F_abc_BaseProperty
     */
    @FieldKey("F_abc_BaseProperty")
    private String fAbcbaseProperty;


    /**
     * 物料分类：F_MS_WLFL  (必填项)
     */
    @FieldKey("F_MS_WLFL.fnumber")
    private String fMsWlfl;

    /**
     * 运输方式
     */
    @FieldKey("F_abc_Combo")
    private String fAbcCombo;

    /**
     *  变更原因：FChangeReason
     */
    @FieldKey("FChangeReason")
    private String fChangeReason;

    /**
     * 变更日期：FChangeDate
     */
    @FieldKey("FChangeDate")
    private String fChangeDate;

    /**
     * 最后更新日期
     */
    @FieldKey("FModifyDate")
    private String fModifyDate;

    /**
     * 	验收方式：FACCTYPE
     */
    @FieldKey("FACCTYPE")
    private String fAccType;

    /**
     * 采购员编码
     */
    @FieldKey("FPurchaserId.fnumber")
    private String fPurchaserIdFnumber;

    /**
     * 采购员名称
     */
    @FieldKey("FPurchaserId.fname")
    private String fPurchaserIdName;

    // -------------- FPOOrderFinance 财务信息 ------------------

    /**
     * 结算币别
     */
    @FieldKey("FSettleCurrId.fnumber")
    private String fSettleCurrId;

    /**
     * 付款条件
     */
    @FieldKey("FPayConditionId.fnumber")
    private String fPayConditionId;

    /**
     * 价税合计
     */
    @FieldKey("FBillAllAmount")
    private String fBillAllAmount;

    // --------------------- FPOOrderEntry -------------------

    /**
     * 计划明细的sourceId
     */
    @FieldKey("FPOOrderEntry_FEntryID")
    private Long fEntryID;

    /**
     * 物料编码
     */
    @FieldKey("FMaterialId.fnumber")
    private String fMaterialId;

    @FieldKey("FModel")
    private String fModel;

    /**
     * 交货日期
     */
    @FieldKey("FDeliveryDate")
    private String fDeliveryDate;

    /**
     * 采购数量
     */
    @FieldKey("FQty")
    private Integer fQty;

    /**
     * 采购单位
     */
    @FieldKey("FUnitId.fnumber")
    private String fUnitId;

    /**
     * 计价数量
     */
    @FieldKey("FPriceUnitQty")
    private Integer fPriceUnitQty;

    /**
     * 计价单位
     */
    @FieldKey("FPriceUnitId.fnumber")
    private String fPriceUnitId;

    /**
     * 单价
     */
    @FieldKey("FPrice")
    private BigDecimal fPrice;

    /**
     * 产品总价
     */
    @FieldKey("FEntryAmount")
    private BigDecimal fEntryAmount;

    /**
     * 含税单价
     */
    @FieldKey("FTaxPrice")
    private BigDecimal fTaxPrice;

    /**
     * 税率
     */
    @FieldKey("FEntryTaxRate")
    private BigDecimal fEntryTaxRate;

    /**
     *  税额：FEntryTaxAmount
     */
    @FieldKey("FEntryTaxAmount")
    private BigDecimal fEntryTaxAmount;

    /**
     * 价税合计：FAllAmount
     */
    @FieldKey("FAllAmount")
    private BigDecimal fAllAmount;

    /**
     * 名称
     */
    @FieldKey("FMaterialName")
    private String fMaterialName;

    /**
     * 交货地址
     */
    @FieldKey("FLocationAddress")
    private String fLocationAddress;

    /**
     * FChangeFlag
     */
    @FieldKey("FChangeFlag")
    private String fChangeFlag;

    /**
     * 商家编码（条码）
     */
    @FieldKey("F_MS_SJBM")
    private String fMssjbm;


    // ---------------------- 交货明细 FEntryDeliveryPlan

    /**
     * 来源系统主键ID
     */
    @FieldKey("FEntryDeliveryPlan_FDetailId")
    private Long fDetailId;

    /**
     * (交货安排)交货日期
     */
    @FieldKey("FDeliveryDate_Plan")
    private String fDeliveryDatePlan ;

    /**
     * 计划数量
     */
    @FieldKey("FPlanQty")
    private Integer fPlanQty;

    /**
     * 交货地址
     */
    @FieldKey("FELocationAddress")
    private String fELocationAddress;

    /**
     * 计划单位
     */
    @FieldKey("FPlanUnitID.fnumber")
    private String fPlanUnitId;

    // -------------------- t_PUR_POENTRYDELIPLAN

    /**
     * 入库仓编码
     */
    @FieldKey("F_SRM_CK.fnumber")
    private String fSrmCkNumber;

    /**
     * 货主类型  BD_OwnerOrg、BD_Supplier、BD_Customer
     */
    //@FieldKey("FOWNERTYPEID")
    //private String ownerTypeid;

    /**
     * oem供应商地址
     */
    @FieldKey("F_ABC_REMARKS1")
    private String faBCrEMARKS1;


}
