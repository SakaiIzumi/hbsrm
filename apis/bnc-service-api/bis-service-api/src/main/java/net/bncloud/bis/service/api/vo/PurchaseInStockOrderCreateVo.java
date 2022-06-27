package net.bncloud.bis.service.api.vo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * desc:
 *
 * @author Rao
 * @Date 2022/04/25
 **/
@Accessors(chain = true)
@NoArgsConstructor
@Data
public class PurchaseInStockOrderCreateVo implements Serializable {
    private static final long serialVersionUID = -374674862054221184L;
    
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
     *  入库日期
     */
    @NotNull(message = "入库日期参数缺失！")
    @JSONField(name = "FDate")
    private String fDate;

    /**
     * 收料组织
     */
    @NotNull(message = "收料组织参数缺失！")
    @JSONField(name = "FStockOrgId")
    private Number fStockOrgId;

    /**
     *  采购组织
     */
    @NotNull(message = "采购组织参数缺失！")
    @JSONField(name = "FPurchaseOrgId")
    private Number fPurchaseOrgId;

    /**
     *  物料分类
     */
    @NotNull(message = "物料分类参数缺失！")
    @JSONField(name = "F_MS_WLFL")
    private Number fMsWlfl;

    /**
     * 物料明细列表
     */
    @JSONField(name = "FInStockEntry")
    private List<FInStockEntry> fInStockEntryList;


    /**
     * FInStockEntry
     */
    @NoArgsConstructor
    @Data
    @Accessors(chain = true)
    public static class FInStockEntry {
        /**
         * fEntryID
         */
        @JSONField(name = "FEntryID")
        private String fEntryId;

        /**
         * 物料编码
         */
        @NotNull(message = "物料编码参数缺失！")
        @JSONField(name = "FMaterialId")
        private Number fMaterialId;

        /**
         * 库存单位
         */
        @NotNull(message = "库存单位参数缺失！")
        @JSONField(name = "FUnitID")
        private Number fUnitId;

        /**
         *  计价单位
         */
        @NotNull(message = "计价单位参数缺失！")
        @JSONField(name = "FPriceUnitID")
        private Number fPriceUnitId;

        /**
         *  采购单位
         */
        @NotNull(message = "采购单位参数缺失！")
        @JSONField(name = "FRemainInStockUnitId")
        private Number fRemainInStockUnitId;

        /**
         * 货主类型
         */
        @NotNull(message = "货主类型参数缺失！")
        @JSONField(name = "FOWNERTYPEID")
        private String fownertypeid;

        /**
         *  货主
         */
        @NotNull(message = "货主参数缺失！")
        @JSONField(name = "FOWNERID")
        private Number fownerid;

        /**
         * 供应商订单号
         */
        @NotNull(message = "供应商订单号参数缺失！")
        @JSONField(name = "F_QH_SUPPLYNO")
        private String fQhSupplyno;

        /**
         *  实收数量
         */
        @NotNull(message = "实收数量参数缺失！")
        @JSONField(name = "FRealQty")
        private String fRealQty;

        /**
         *  批号
         */
        @NotNull(message = "批号参数缺失！")
        @JSONField(name = "FLot")
        private Number fLot;

        /**
         *  入库仓
         */
        @NotNull(message = "入库仓！")
        @JSONField(name = "FStockID")//    FStockID   F_SRM_CK
        private Number fStockID;

        /**
         * 供应商 (必填)
         */
        @NotNull(message = "入库仓！")
        @JSONField(name= "FSupplierId")
        private Number fSupplierId;

        /**
         * 含税单价
         */
        @NotNull(message = "含税单价！")
        @JSONField(name= "FTaxPrice")
        private BigDecimal fTaxPrice;

    }



}
