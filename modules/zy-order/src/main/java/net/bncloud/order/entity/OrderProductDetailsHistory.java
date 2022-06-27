package net.bncloud.order.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.bncloud.base.BaseEntity;
import net.bncloud.common.util.DateUtil;
import net.bncloud.common.web.jackson.AmountSerializer;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 产品明细表 产品明细表
 * </p>
 *
 * @author lv
 * @since 2021-03-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_order_product_details_history")

public class OrderProductDetailsHistory extends BaseEntity {
	
	private static final long serialVersionUID = 1L;
	
	
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
	 * 产品编码
	 */
	@ApiModelProperty(value = "产品编码")
	private String productCode;
	
	/**
	 * 产品名称
	 */
	@ApiModelProperty(value = "产品名称")
	private String productName;
	
	/**
	 * 产品规格
	 */
	@ApiModelProperty(value = "产品规格")
	private String productSpecs;
	
	/**
	 * 收货部门名称
	 */
	@ApiModelProperty(value = "收货部门名称")
	private String takeOverDepartmentName;
	
	/**
	 * 收货部门ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "收货部门ID")
	private Long takeOverDepartmentId;
	
	/**
	 * 收货仓库
	 */
	@ApiModelProperty(value = "收货仓库")
	private String takeOverWarehouse;
	
	/**
	 * 采购说明
	 */
	@ApiModelProperty(value = "采购说明")
	private String purchaseRemarks;
	
	/**
	 * 交货日期
	 */
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
	@JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
	@ApiModelProperty(value = "交货日期")
	private Date deliveryTime;
	
	/**
	 * 交货方式
	 */
	@ApiModelProperty(value = "交货方式")
	private String deliveryType;
	
	/**
	 * 答交状态
	 */
	@ApiModelProperty(value = "答交状态")
	private Integer status;
	
	/**
	 * 采购数量
	 */
	@ApiModelProperty(value = "采购数量")
	private BigDecimal purchaseNum;
	
	/**
	 * 采购单位
	 */
	@ApiModelProperty(value = "采购单位")
	private String purchaseUnit;
	
	/**
	 * 采购编码
	 */
	@ApiModelProperty(value = "采购编码")
	private String purchaseCode;
	
	/**
	 * 已发数量
	 */
	@ApiModelProperty(value = "已发数量")
	private BigDecimal sendNum;
	
	/**
	 * 收货状态 1）待收货
	 * 2）部分收货
	 * 3）收货完成
	 * 4）已结案
	 */
	@ApiModelProperty(value = "发货状态")
	private String takeOverStatus;
	
	/**
	 * 计价数量
	 */
	@ApiModelProperty(value = "计价数量")
	private BigDecimal markDownNum;

	/**
	 * 单价
	 */
	@ApiModelProperty(value = "单价")
	@JsonSerialize(using = AmountSerializer.class)
	private BigDecimal unitPrice;
	
	/**
	 * 产品总价
	 */
	@ApiModelProperty(value = "产品总价")
	@JsonSerialize(using = AmountSerializer.class)
	private BigDecimal productTotalPrice;
	
	/**
	 * 供应商备注
	 */
	@ApiModelProperty(value = "供应商备注")
	private String supplierRemarks;
	
	/**
	 * 品牌方备注
	 */
	@ApiModelProperty(value = "品牌方备注")
	private String brandRemarks;
	
	/**
	 * 附件
	 */
	@ApiModelProperty(value = "附件")
	private String files;
	
	/**
	 * 计价单位
	 */
	@ApiModelProperty(value = "计价单位")
	private String valuationUnit;



	/**
	 * 美尚
	 */
	@ApiModelProperty(value = "含税单价")
	@JsonSerialize(using = AmountSerializer.class)
	private BigDecimal taxPrice;

	/**
	 * 美尚
	 */
	@ApiModelProperty(value = "税率")
	@JsonSerialize(using = AmountSerializer.class)
	private BigDecimal taxRate;

	/**
	 * 美尚
	 */
	@ApiModelProperty(value = "税额")
	@JsonSerialize(using = AmountSerializer.class)
	private BigDecimal taxAmount;

	/**
	 * 美尚
	 */
	@ApiModelProperty(value = "税价合计")
	@JsonSerialize(using = AmountSerializer.class)
	private String allAmount;

	/**
	 * 美尚
	 */
	@ApiModelProperty(value = "变更标志")
	private String changeCode;

	/**
	 * 美尚
	 */
	@ApiModelProperty(value = "与上一条明细变更详情")
	private String beforeChange;



	@ApiModelProperty(value = "明细ID")
	private Long pdId;


	
}
