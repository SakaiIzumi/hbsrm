package net.bncloud.quotation.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import net.bncloud.base.BaseEntity;


/**
 *
 * 发送应标供应商信息
 *
 *
 * @author Auto-generator
 * @since 2022-02-18
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_rfq_bid_response")
@Builder
public class BidResponse extends BaseEntity {

    private static final long serialVersionUID = 1L;


    /**
     * 询价单号
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "询价单号")
    private String quotationNo;

    /**
     * 联系人手机号
     */
    @ApiModelProperty(value = "联系人手机号")
    private String mobile;

    /**
     * 供应商ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "供应商ID")
    private Long supplierId;


    /**
     * 供应商编码
     */
    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;

    /**
     * 供应商名称
     */
    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    /**
     * 供应商类型，formal正式，potential潜在
     */
    @ApiModelProperty(value = "供应商类型，formal正式，potential潜在")
    private String supplierType;











}
