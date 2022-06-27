package net.bncloud.service.api.delivery.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import net.bncloud.common.util.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 * 送货计划明细批次表
 * </p>
 *
 * @author Auto-generator
 * @since 2022-01-17
 */
@Data
@Accessors(chain = true)
public class DeliveryPlanDetailItemEntity {

	private static final long serialVersionUID = 1L;



    /**
     * 送货日期 (交货日期)
     */
//    @DateTimeFormat(pattern = DateUtil.PATTERN_DATE)
//    @JsonFormat(pattern = DateUtil.PATTERN_DATE)
    @ApiModelProperty(value = "送货日期")
    private LocalDateTime deliveryDate;

    /**
     * 送货数量
     */
    @ApiModelProperty(value = "送货数量")
    private String deliveryQuantity;

    /**
     * 确认时间
     */
//    @DateTimeFormat(pattern = DateUtil.PATTERN_DATE)
//    @JsonFormat(pattern = DateUtil.PATTERN_DATE)
    @ApiModelProperty(value = "确认时间")
    private LocalDateTime confirmDate;

    /**
     * 确认数量
     */
    @ApiModelProperty(value = "确认数量")
    private String confirmQuantity;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private Long createdBy;


    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createdDate;

    /**
     * 更新人
     */
    @ApiModelProperty(value = "更新人")
    private Long lastModifiedBy;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime lastModifiedDate;


    /**
     * 状态[0:未删除,1:删除]
     */
    @ApiModelProperty(value = "是否已删除")
    private Integer isDeleted;



    /**
     * 来源系统主键ID
     */
    private String sourceId;


    /**
     * 入货仓
     */
    @ApiModelProperty(value = "入货仓")
    private String warehousing;

    /**
     * 送货地址
     */
    @ApiModelProperty(value = "送货地址")
    private String deliveryAddress;

    /**
     * 剩余数量
     */
    @ApiModelProperty(value = "剩余数量")
    private String remainingQuantity;

}
