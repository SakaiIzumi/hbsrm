package net.bncloud.delivery.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import net.bncloud.base.BaseEntity;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import net.bncloud.common.util.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.OffsetDateTime;
import java.util.Date;

/**
 * <p>
 * 报关资料信息表
 * </p>
 *
 * @author huangtao
 * @since 2021-03-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_delivery_customs_information")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryCustomsInformation extends BaseEntity {

    private static final long serialVersionUID = 1L;


    /**
     * 送货单ID t_delivery_note.delivery_id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "送货单ID t_delivery_note.delivery_id")
    private Long deliveryId;

    /**
     * 送货单号
     */
    @ApiModelProperty(value = "送货单号")
    private String deliveryNo;

    /**
     * 内部单号
     */
    @ApiModelProperty(value = "内部单号")
    private String innerOrderNo;

    /**
     * 提单号码
     */
    @ApiModelProperty(value = "提单号码")
    private String trackingNumber;

    /**
     * 报关类型
     */
    @ApiModelProperty(value = "报关类型")
    private String customsType;

    /**
     * 目的港
     */
    @ApiModelProperty(value = "目的港")
    private String destinationHarbor;

    /**
     * 装运港
     */
    @ApiModelProperty(value = "装运港")
    private String shipmentHarbor;

    /**
     * 原产国
     */
    @ApiModelProperty(value = "原产国")
    private String originCountry;

    /**
     * 发票号码
     */
    @ApiModelProperty(value = "发票号码")
    private String invoiceNumber;

    /**
     * 发票日期
     */
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
    @JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
    @ApiModelProperty(value = "发票日期")
    private Date invoiceDate;

}
