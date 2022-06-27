package net.bncloud.bis.srm.financial.model.erp;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.bncloud.msk3cloud.core.fieldparse.FieldKey;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author: liulu
 * @Date: 2022-02-14 10:32
 */
@ApiModel("结算池对象")
@TableName(value = "settlement_pool_sync")
@Data
public class SettlementPoolSync implements Serializable {

    private static final long serialVersionUID = -2877350031211962767L;

    /**
     * id值
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 抬头实体主键
     */
    @FieldKey("FID")
    private Long fid;

    /**
     * 明细行id
     */
    @FieldKey("FEntityDetail_FEntryID")
    private Long fDetailId;

    /**
     * srm对账单状态
     */
    @FieldKey("F_srm_dzzt")
    private String fSrmDzzt;

    @ApiModelProperty("采购方编码")
    @FieldKey("FPURCHASEORGID.fnumber")
    private String fPurchaseOrgCode;

    @ApiModelProperty("采购方名称")
    @FieldKey("FPURCHASEORGID.fname")
    private String fPurchaseOrgName;

    @ApiModelProperty("供应商编码")
    @FieldKey("FSUPPLIERID.fnumber")
    private String fSupplierCode;

    @ApiModelProperty("供应商名称")
    @FieldKey("FSUPPLIERID.fname")
    private String fSupplierName;

    @ApiModelProperty("最后更新日期")
    @FieldKey("FModifyDate")
    private Date fModifyDate;

    @ApiModelProperty("费用单号")
    @FieldKey("FBillNo")
    private String fBillNo;

    @ApiModelProperty("单据类型")
    @FieldKey("FBillTypeID")
    private String fBillTypeID;

    @ApiModelProperty("单据类型编码")
    @FieldKey("FBillTypeID.fnumber")
    private String fBillTypeNumber;

    @ApiModelProperty("单据类型名称")
    @FieldKey("FBillTypeID.fname")
    private String fBillTypeName;

    @ApiModelProperty("单据状态")
    @FieldKey("FDOCUMENTSTATUS")
    private String fDocumentStatus;

    @ApiModelProperty("发布时间")
    @FieldKey("FCreateDate")
    private Date fCreateDate;

    @ApiModelProperty("发布人")
    @FieldKey("FCreatorId.fname")
    private String fCreatorName;

    @ApiModelProperty("确认时间")
    @FieldKey("FAPPROVEDATE")
    private Date fApproveDate;

    @ApiModelProperty("签收/确认时间")
    @FieldKey("FDATE")
    private Date fDate;

    @ApiModelProperty("确认人")
    @FieldKey("FAPPROVERID.fname")
    private String fApproverName;

    @ApiModelProperty("币别")
    @FieldKey("FCURRENCYID.fnumber")
    private String fCurrencyCode;

    @ApiModelProperty("币别")
    @FieldKey("FCURRENCYID.fname")
    private String fCurrencyName;

    @ApiModelProperty("价税合计")
    @FieldKey("FALLAMOUNTFOR")
    private BigDecimal fAllAmountFor;

    @ApiModelProperty("价税合计")
    @FieldKey("FALLAMOUNTFOR_D")
    private BigDecimal fAllAmountFor_d;

    @ApiModelProperty("税率(%)")
    @FieldKey("FEntryTaxRate")
    private BigDecimal fEntryTaxRate;

    @ApiModelProperty("税额汇总")
    @FieldKey("FTaxAmountFor")
    private BigDecimal fTaxAmountFor;

    @ApiModelProperty("税额明细")
    @FieldKey("FTAXAMOUNTFOR_D")
    private BigDecimal fTaxAmountFor_d;


    @ApiModelProperty("费用名称")
    @FieldKey("FCostName")
    private String fCostName;

    @ApiModelProperty("费用原因")
    @FieldKey("FComment")
    private String fComment;

    @ApiModelProperty("入库单号")
    @FieldKey("FINSTOCKID")
    private String fInstockId;

    @ApiModelProperty("费用编码")
    @FieldKey("FCOSTID.fnumber")
    private String fCostId;

    @ApiModelProperty("含税金额")
    @FieldKey("FTaxPrice")
    private BigDecimal fTaxPrice;

    @ApiModelProperty("不含税金额")
    @FieldKey("FNoTaxAmountFor_D")
    private BigDecimal fNoTaxAmountFor_d;

    @ApiModelProperty("送货数量")
    @FieldKey("FPriceQty")
    private Integer fPriceQty;

    @ApiModelProperty("备注")
    @FieldKey("FAP_Remark")
    private String fRemark;

    @ApiModelProperty("收料通知单号")
    @FieldKey("F_abc_Text")
    private String fAbcText;
}
