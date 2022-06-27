package net.bncloud.oem.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.bncloud.base.BaseEntity;
import net.bncloud.common.util.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

 
/**
 * @author ddh
 * @since 2022/4/24
 * @description
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "t_oem_purchase_order_receiving")
public class PurchaseOrderReceiving extends BaseEntity {


    private static final long serialVersionUID = 5252015487704912321L;
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


}