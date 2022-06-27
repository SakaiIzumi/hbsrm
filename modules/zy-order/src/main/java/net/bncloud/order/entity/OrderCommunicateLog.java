package net.bncloud.order.entity;


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

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Date;

/**
 * <p>
 * 订单答交日志表
 * </p>
 *
 * @author Auto-generator
 * @since 2021-03-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_order_communicate_log")

public class OrderCommunicateLog extends BaseEntity {
	
	private static final long serialVersionUID = 1L;
	
	
	/**
	 * 关联order_product_details表
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "关联order_product_details表")
	private Long orderProductDetailsId;
	
	/**
	 * 采购单号
	 */
	@ApiModelProperty(value = "采购单号")
	private String purchaseOrderCode;
	
	/**
	 * 项次
	 */
	@ApiModelProperty(value = "项次")
	private String itemCode;
	
	/**
	 * 项次
	 */
	@ApiModelProperty(value = "项次")
	private String item;
	
	/**
	 * 交货日期
	 */
	@JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
	@ApiModelProperty(value = "交货日期")
	private Date deliveryTime;
	
	/**
	 * 交货日期修改类型0未修改1修改
	 */
	@ApiModelProperty(value = "交货日期修改类型0未修改1修改")
	private Integer deliveryTimeType;
	
	/**
	 * 采购数量
	 */
	@ApiModelProperty(value = "采购数量")
	private BigDecimal purchaseNum;
	
	/**
	 * 采购数量修改类型0未修改1修改
	 */
	@ApiModelProperty(value = "采购数量修改类型0未修改1修改")
	private Integer purchaseNumType;
	
	/**
	 * 单价
	 */
	@ApiModelProperty(value = "单价")
	private BigDecimal unitPrice;
	
	/**
	 * 单价修改类型0未修改1修改
	 */
	@ApiModelProperty(value = "单价修改类型0未修改1修改")
	private Integer unitPriceType;
	
	/**
	 * 备注
	 */
	@ApiModelProperty(value = "备注")
	private String brandRemarks;
	
	/**
	 * 附件
	 */
	@ApiModelProperty(value = "附件")
	private String files;
	
	/**
	 * 类型1变更信息2答交信息3答交差异
	 */
	@ApiModelProperty(value = "类型1变更信息2答交信息3答交差异")
	private Integer type;
	
	/**
	 * 批次-辅助字段
	 */
	@ApiModelProperty(value = "批次-辅助字段")
	private Integer batch;
	
	/**
	 * 处理状态
	 */
	@ApiModelProperty(value = "0未生效1已处理2作废")
	private Integer status;
	
}
