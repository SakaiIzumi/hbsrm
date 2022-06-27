package net.bncloud.oem.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import net.bncloud.common.util.DateUtil;
import net.bncloud.oem.domain.entity.FileInfo;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * @author ddh
 * @description 收货记录明细
 * @since 2022/4/29
 */
@Data
@Accessors(chain = true)
public class ReceivingRecordsVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 收货id
     */
    private Long id;

    /**
     * 物料id
     */
    private Long purchaseOrderMaterialId;
    /**
     * 物料编码
     */
    private String materialCode;

    /**
     * 物料名称
     */
    private String materialName;

    /**
     * 条码
     */
    private String barCode;

    /**
     * 送货单号
     */
    private String deliveryNoteNo;

    /**
     * 生产批次号
     */
    private String manufactureBatchNo;

    /**
     * 收货批次
     */
    private String receiveBatchNo;

    /**
     * 本批次收货数量
     */
    private Long receiveQuantity;

    /**
     * 收货日期
     */
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATE)
    @JsonFormat(pattern = DateUtil.PATTERN_DATE)
    private LocalDate receiveDate;

    /**
     * oem供应商备注
     */
    private String oemSupplierRemark;

    /**
     * 品牌方备注
     */
    private String brandRemark;

    /**
     * 收货状态：0待确认，1已确认，2已退回
     */
    private String status;


    /**
     * 剩余数量(冗余字段)
     */
    private Long remainingQuantity;

    /**
     * 采购单号(冗余字段)
     */
    private String purchaseOrderCode;

    /**
     * 附件列表
     */
    private List<FileInfo> attachmentList;

    /**
     * 操作按钮：
     * 编辑
     * 删除
     */
    private Map<String,Boolean> operationButton;
}
