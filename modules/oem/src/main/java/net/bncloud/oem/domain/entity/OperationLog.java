package net.bncloud.oem.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;
import net.bncloud.base.BaseEntity;
import net.bncloud.common.util.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;


/**
 * 送货通知操作记录信息表
 * @author ddh
 * @since 2022/4/24
 * @description
 */
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Data
@TableName(value = "t_oem_operation_log")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OperationLog extends BaseEntity {


    private static final long serialVersionUID = 1L;
    /**
     * 单据id
     */
    @ApiModelProperty(value = "单据id")
    private Long billId;

    /**
     * 操作人工号
     */
    @ApiModelProperty(value = "操作人工号")
    private String operatorNo;

    /**
     * 操作人姓名
     */
    @ApiModelProperty(value = "操作人姓名")
    private String operatorName;

    /**
     * 操作内容
     */
    @ApiModelProperty(value = "操作内容")
    private String operatorContent;

    /**
     * 说明
     */
    @ApiModelProperty(value = "说明")
    private String remark;

    /**
     * 送货地址编码
     */
    @ApiModelProperty(value="送货地址编码")
    private String addressCode;

    /**
     * 送货地址
     */
    @ApiModelProperty(value="送货地址")
    private String address;

    /**
     * 地址的修改内容
     */
    @ApiModelProperty(value="送货地址")
    private String content;

    /**
     * 采购单号
     */
    @ApiModelProperty(value="采购单号（美尚）")
    private String purchaseOrderCode;
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

}