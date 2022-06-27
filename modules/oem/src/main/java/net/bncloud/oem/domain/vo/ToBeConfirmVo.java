package net.bncloud.oem.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.bncloud.common.util.DateUtil;
import net.bncloud.oem.domain.entity.FileInfo;
import net.bncloud.oem.domain.entity.PurchaseOrder;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


/**
 * @author liyh
 * @description
 * @since 2022/4/24
 */

@Data
public class ToBeConfirmVo extends PurchaseOrder implements Serializable {


    private static final long serialVersionUID = 1140465048876944383L;
    /**
     * 采购订单id
     */
    @ApiModelProperty(value = "采购订单id")
    private Long purchaseOrderId;

    /**
     * 物料编码
     */
    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    /**
     * 物料名称
     */
    @ApiModelProperty(value = "物料名称")
    private String materialName;

    /**
     * 交货日期
     */
    @ApiModelProperty(value = "交货日期")
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
    @JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
    private LocalDateTime deliveryDate;

    /**
     * 交货方式
     */
    @ApiModelProperty(value = "交货方式")
    private String deliveryType;

    /**
     * 答交状态
     */
    @ApiModelProperty(value = "答交状态")
    private Integer answerStatus;

    /**
     * 采购数量(订单数量)
     */
    @ApiModelProperty(value = "订单数量")
    private Long purchaseQuantity;
    /**
     * 剩余数量
     */
    private Long remainingQuantity;

    /**
     * 收货次数
     */
    private Long receivingTimes;

    /**
     * 已收货数量
     */
    private Long receivedQuantity;


    /**
     * 收货状态:
     * 1待收货：已收货数量=0
     * 2部分收货：0 < 已收货数量 < 订单数量
     * 3已收货：已收货数量 ≥ 订单数量
     */
    @ApiModelProperty(value = "收货状态:1待收货，2部分收货，3已收货")
    private String takeOverStatus;


    /**
     * ERPID
     */
    private String sourceErpId;

    /**
     * 物料id
     */
    private Long purchaseOrderMaterialId;
    /**
     * 送货单号
     */
    @ApiModelProperty(value="送货单号")
    private String deliveryNoteNo;

    /**
     * 生产批次号
     */
    @ApiModelProperty(value="生产批次号")
    private String manufactureBatchNo;

    /**
     * 收货批次
     */
    @ApiModelProperty(value="收货批次")
    private String receiveBatchNo;

    /**
     * 本批次收货数量
     */
    @ApiModelProperty(value="本批次收货数量")
    private Long receiveQuantity;

    /**
     * 收货日期
     */
    @ApiModelProperty(value="收货日期")
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATE)
    @JsonFormat(pattern = DateUtil.PATTERN_DATE)
    private LocalDate receiveDate;

    /**
     * oem供应商备注
     */
    @ApiModelProperty(value="oem供应商备注")
    private String oemSupplierRemark;

    /**
     * 品牌方备注
     */
    @ApiModelProperty(value="品牌方备注")
    private String brandRemark;

    /**
     * 状态：0待确认，1已确认，2已退回
     */
    @ApiModelProperty(value="状态：0待确认，1已确认，2已退回")
    private String status;

    /**
     * 回写erp后的fEntryId
     */
    @ApiModelProperty(value="回写erp后的fEntryId")
    private String fEntryId;

    /**
     * 权限按钮
     */
    @ApiModelProperty(value="回写erp后的fEntryId")
    private Map<String,Boolean> permissionButton;

    /**
     * 地址
     */
    @ApiModelProperty(value="地址")
    private String address;

    /**
     * 第三层id
     */
    @ApiModelProperty(value="第三层id")
    private String rid;


    /**
     * 附件列表
     */
    private List<FileInfo> attachmentList;





}