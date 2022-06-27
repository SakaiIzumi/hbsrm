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

/**
 * @author ddh
 * @description 被退回的视图
 * @since 2022/4/27
 */
@Data
@Accessors(chain = true)
public class ReturnedReceiptsVo implements Serializable {
    private static final long serialVersionUID = -1L;

    /**
     * 采购订单id
     */
    private Long purchaseOrderId;
    /**
     * 物料id
     */
    private Long materialId;
    /**
     * 收货id
     */
    private Long receivingId;
    /**
     * 采购方编码
     */
    private String purchaseCode;
    /**
     * 采购方名称
     */
    private String purchaseName;
    /**
     * 采购订单
     */
    private String purchaseOrderCode;
    /**
     * 物料编码
     */
    private String materialCode;
    /**
     * 物料名称
     */
    private String materialName;
    /**
     * 送货单号
     */
    private String deliveryNoteNo;
    /**
     * 生产批次号
     */
    private String manufactureBatchNo;
    /**
     * 收货批次号
     */
    private String receiveBatchNo;
    /**
     * 批次收货数量
     */
    private String receiveQuantity;
    /**
     * 收货日期
     */
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATE)
    @JsonFormat(pattern = DateUtil.PATTERN_DATE)
    private LocalDate receiveDate;
    /**
     * 收货确认状态
     */
    private String status;
    /**
     * oem供应商备注
     */
    private String oemSupplierRemark;

    /**
     * 采购方备注
     */
    private String brandRemark;

    /**
     * 剩余数量
     */
    private Long remainingQuantity;
    /**
     * 附件列表
     */
    private List<FileInfo> attachmentList;
}
