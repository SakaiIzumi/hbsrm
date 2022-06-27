package net.bncloud.bis.model.vo;

import lombok.Data;
import net.bncloud.msk3cloud.core.fieldparse.FieldKey;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * desc: 计划订单
 *
 * @author Rao
 * @Date 2022/05/27
 **/
@Data
public class PlnPlanOrderVo implements Serializable {
    private static final long serialVersionUID = 8366139994860456050L;

    // 主体信息

    /**
     * erp ID
     */
    @FieldKey("FID")
    private Long fid;

    /**
     * 单据编号
     */
    @FieldKey("FBillNo")
    private String fBillNo;

    /**
     * 单据状态
     * Z 暂存
     * A 创建
     * B 审核中
     * C 已审核
     * D 重新审核
     */
    @FieldKey("FDocumentStatus")
    private String fDocumentStatus;

    /**
     *  投放单据类型
     * CGSQD01_SYS    标准采购申请
     * CGSQD02_SYS    直运采购申请
     * CGSQD03_SYS    资产采购申请单
     * CGSQD04_SYS    费用采购申请
     */
    @FieldKey("FReleaseBillType.fnumber")
    private String fReleaseBillType;

    /**
     * 单据类型
     */
    @FieldKey("FBillTypeID.fnumber")
    private String fBillTypeId;

    /**
     * 采购方编码
     */
    @FieldKey("FSupplyOrgId.fnumber")
    private String fSupplyOrgId;

    /**
     * 入库组织
     */
    @FieldKey("FInStockOrgId.fnumber")
    private String fInStockOrgId;

    /**
     * 供应商
     */
    @FieldKey("F_ABC_TEST.fnumber")
    private String fAbcTest;

    /**
     * 物料编码
     */
    @FieldKey("FMaterialId.fnumber")
    private String fMaterialId;

    /**
     * 物料名称
     */
    @FieldKey("FMaterialName")
    private String fMaterialName;

    /**
     * 规格型号
     */
    @FieldKey("FSpecification")
    private String fSpecification;

    /**
     * 单位
     */
    @FieldKey("FUnitId.fnumber")
    private String fUnitId;

    /**
     * 建议到货/完工日期
     */
    @FieldKey("FPlanFinishDate")
    private LocalDateTime fPlanFinishDate;

    /**
     * 计划订单量
     */
    @FieldKey("FOrderQty")
    private BigDecimal fOrderQty;

    /**
     * 运算编号
     */
    @FieldKey("FComputerNo")
    private String fComputerNo;

    /**
     * 审核日期
     */
    @FieldKey("FApproveDate")
    private LocalDateTime fApproveDate;

}
