package net.bncloud.delivery.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.bncloud.base.BaseEntity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * desc: 计划订单
 *
 * @author Rao
 * @Date 2022/05/07
 **/
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("t_pln_plan_order")
public class PlnPlanOrder extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -7772671227322111457L;

    /**
     * erp ID
     */
    private Long sourceId;

    /**
     * 单据编号
     */
    private String billNo;

    /**
     * 单据状态
     * Z 暂存
     * A 创建
     * B 审核中
     * C 已审核
     * D 重新审核
     */
    private String documentStatus;

    /**
     * 单据类型
     * CGSQD01_SYS    标准采购申请
     * CGSQD02_SYS    直运采购申请
     * CGSQD03_SYS    资产采购申请单
     * CGSQD04_SYS    费用采购申请
     */
    private String billTypeId;

    /**
     * 采购方编码
     */
    private String purchaserCode;

    /**
     * 入库组织
     */
    private String purchaserCodeOrg;

    /**
     * 供应商
     */
    private String supplierCode;

    /**
     * 物料编码
     */
    private String materialIdChild;

    /**
     * 物料名称
     */
    private String materialName;

    /**
     * 规格型号
     */
    private String specification;

    /**
     * 单位
     */
    private String unitId;

    /**
     * 建议到货/完工日期
     */
    private LocalDateTime planFinishDate;

    /**
     * 计划订单量
     */
    private BigDecimal orderQty;

    /**
     * 运算编号
     */
    private String computerNo;

    /**
     * 审核日期
     */
    private LocalDateTime approveDate;

}
