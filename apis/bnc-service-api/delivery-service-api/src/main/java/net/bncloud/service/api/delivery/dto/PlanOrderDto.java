package net.bncloud.service.api.delivery.dto;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.experimental.Accessors;
import net.bncloud.common.exception.Asserts;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * desc:
 *
 * @author Rao
 * @Date 2022/05/30
 **/
@Accessors(chain = true)
@Data
public class PlanOrderDto implements Serializable {

    private static final long serialVersionUID = -3538887423858822888L;


    /**
     * erp ID
     */
    private Long fid;

    /**
     * 单据编号
     */
    private String fBillNo;

    /**
     * 单据状态
     * Z 暂存
     * A 创建
     * B 审核中
     * C 已审核
     * D 重新审核
     */
    private String fDocumentStatus;

    /**
     *  投放单据类型
     * CGSQD01_SYS    标准采购申请
     * CGSQD02_SYS    直运采购申请
     * CGSQD03_SYS    资产采购申请单
     * CGSQD04_SYS    费用采购申请
     */
    private String fReleaseBillType;

    /**
     * 单据类型
     */
    private String fBillTypeId;

    /**
     * 采购方编码
     */
    private String fSupplyOrgId;

    /**
     * 入库组织
     */
    private String fInStockOrgId;

    /**
     * 供应商编码
     */
    private String fAbcTest;

    /**
     * 物料编码
     */
    private String fMaterialId;

    /**
     * 物料名称
     */
    private String fMaterialName;

    /**
     * 规格型号
     */
    private String fSpecification;

    /**
     * 单位
     */
    private String fUnitId;

    /**
     * 建议到货/完工日期
     */
    private LocalDateTime fPlanFinishDate;

    /**
     * 建议到货秒数用于排序
     */
    private Long planFinishDateEpochMilli = 0L;

    /**
     * 计划订单量
     */
    private BigDecimal fOrderQty;

    /**
     * 采购方名称
     */
    private String purchaseName;

    /**
     * 供应商名称
     */
    private String supplierName;

    /**
     * 运算编号
     */
    private String  fComputerNo;

    /**
     * 审核日期
     */
    private LocalDateTime fApproveDate;

    /**
     * 到货日期的 毫秒数
     * @return
     */
    public PlanOrderDto updatePlanFinishDateSecond(){
        if (this.fPlanFinishDate != null) {
            this.planFinishDateEpochMilli = this.fPlanFinishDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        }
        return this;
    }

    /**
     * build 分组key
     *    供应商code+采购方code
     * @return
     */
    public String buildGroupKey( ){
        return this.fAbcTest + "_" + this.fSupplyOrgId;
    }

    /**
     * 解析 groupKey
     */
    public static PlanOrderDto parseGroupKey(String groupKey) {
        Asserts.isTrue(StrUtil.isNotBlank( groupKey),"滚!");
        String[] arr = groupKey.split("_");
        return new PlanOrderDto().setFAbcTest( arr[0] ).setFSupplyOrgId( arr[1]);
    }

    /**
     * 转换成日期
     * @return
     */
    public LocalDate planFinishDateToLocalDate(){
        return this.fPlanFinishDate.toLocalDate();
    }


}
