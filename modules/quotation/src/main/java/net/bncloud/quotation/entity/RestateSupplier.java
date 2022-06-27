package net.bncloud.quotation.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import net.bncloud.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import net.bncloud.common.util.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.OffsetDateTime;

/**
 * <p>
 * 询价重报供应商邀请信息
 * </p>
 *
 * @author Auto-generator
 * @since 2022-03-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_rfq_restate_supplier")

public class RestateSupplier extends BaseEntity {

	private static final long serialVersionUID = 1L;


    /**
     * 询价单主键ID
     */
@JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "询价单主键ID")
    private Long quotationBaseId;

    /**
     * 重新报价次数
     */
    @ApiModelProperty(value = "重新报价次数")
    private Integer roundNumber;

    /**
     * 供应商ID
     */
@JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "供应商ID")
    private Long supplierId;

    /**
     * 供应商账号
     */
    @ApiModelProperty(value = "供应商账号")
    private String supplierAccount;

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

}
