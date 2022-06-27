package net.bncloud.serivce.api.order.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;


@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OrderForDeliveryInfoDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 订单id
	 */
	private Long id;
	
	/**
	 * 订单金额
	 */
	@ApiModelProperty(value = "订单金额")
	private String orderPrice;
	/**
	 * 订单确认金额
	 */
	@ApiModelProperty(value = "订单确认金额")
	private String orderConfirmPrice;
	/**
	 * 订单确认人ID
	 */
	@ApiModelProperty(value = "订单确认人ID")
	private Long confirmUserId;
	/**
	 * 订单确认人名称
	 */
	@ApiModelProperty(value = "订单确认人名称")
	private String confirmUserName;
	/**
	 * 交易条件
	 */
	@ApiModelProperty(value = "交易条件")
	private String tradeTerm;
	/**
	 * 币别
	 */
	@ApiModelProperty(value = "币别")
	private String currency;
	/**
	 * 税别
	 */
	@ApiModelProperty(value = "税别")
	private String taxCategory;
	/**
	 * 付款条件
	 */
	@ApiModelProperty(value = "付款条件")
	private String paymentTerms;
	/**
	 * 确认时间
	 */
//	@ApiModelProperty(value = "确认时间")
//	private Date confirmTime;
	
	/**
	 * 确认方 1智采 2智易
	 */
	@ApiModelProperty(value = "确认方 1智采 2智易")
	private Integer confirmSource;
	
	/**
	 * 采购日期
	 */
//	@ApiModelProperty(value = "采购日期")
//	private Date purchaseTime;
	/**
	 * 采购员ID
	 */
	@ApiModelProperty(value = "采购员ID")
	private Long purchaseUserId;

	/**
	 * 采购员编码
	 */
	@ApiModelProperty(value = "采购员编码")
	private String purchaseUserCode;
	/**
	 * 采购员名称
	 */
	@ApiModelProperty(value = "采购员名称")
	private String purchaseUserName;
	/**
	 * 采购部门
	 */
	@ApiModelProperty(value = "采购部门")
	private String purchaseDepartment;
	/**
	 * 采购单号  ->（ERP订单号）
	 */
	@ApiModelProperty(value = "采购单号")
	private String purchaseOrderCode;
	/**
	 * 采购方编码
	 */
	@ApiModelProperty(value = "采购方编码")
	private String purchaseCode;
	/**
	 * 采购方名称
	 */
	@ApiModelProperty(value = "采购方名称")
	private String purchaseName;
	/**
	 * 供应商名称
	 */
	@ApiModelProperty(value = "供应商名称")
	private String supplierName;
	/**
	 * 供应商编码
	 */
	@ApiModelProperty(value = "供应商编码")
	private String supplierCode;
	/**
	 * 供应商附件
	 */
	@ApiModelProperty(value = "供应商附件")
	private String supplierFiles;
	/**
	 * 补充条款
	 */
	@ApiModelProperty(value = "补充条款")
	private String sideLetter;
	/**
	 * 物流方式
	 */
	@ApiModelProperty(value = "物流方式")
	private String logisticsMode;
	/**
	 * 收货地址
	 */
	@ApiModelProperty(value = "收货地址")
	private String receivingAddress;
	/**
	 * 收货人
	 */
	@ApiModelProperty(value = "收货人")
	private String consignee;
	/**
	 * 订单备注
	 */
	@ApiModelProperty(value = "订单备注")
	private String orderRemarks;
	/**
	 * 消息状态0未读1已读
	 */
	@ApiModelProperty(value = "消息状态0未读1已读")
	private Integer msgType;

//	@ApiModelProperty
//	private LocalDateTime msgReadTime;
	/**
	 * 变更状态 1）无变更
	 2）已确认并更
	 3）未确认变更
	 4）待确认变更
	 */
	@ApiModelProperty(value = "变更状态")
	private Integer changeStatus;
	/**
	 * 订单状态 1）草稿
	 2）待答交
	 3）已留置
	 4）答交差异
	 5）退回
	 6）变更中
	 7）已确认
	 8）已完成
	 */
	@ApiModelProperty(value = "订单状态")
	private Integer orderStatus;
	
	/**
	 * 上一个订单状态
	 */
	@ApiModelProperty(value = "订单状态")
	private Integer beforeOrderStatus;
	
	/**
	 * 订单类型 ERP获取类型
	 */
	@ApiModelProperty(value = "订单类型")
	private String orderType;
	/**
	 * 收货状态 1）待收货
	 2）部分收货
	 3）收货完成
	 4）已结案
	 */
	@ApiModelProperty(value = "收货状态")
	private String takeOverStatus;
	/**
	 * 差异详情 1）单价差异
	 2）数量差异
	 3）交期差异
	 4）无差异
	 */
	@ApiModelProperty(value = "差异详情")
	private String differenceDetails;
	
	
	/**
	 * 签约状态
	 * 1）待签约
	 * 2）异常
	 * 3）已签约
	 */
	@ApiModelProperty(value = "签约状态")
	private Integer signContractStatus;
	
	
	/**
	 * 计算状态1 未计算 2已计算用于计算订单金额
	 */
	@ApiModelProperty(value = "计算状态")
	private Integer sumStatus;


	/**
	 * 美尚  -> (订单编号)
	 */
	@ApiModelProperty(value = "订单编号")
	private String orderNo;
	/**
	 * 美尚
	 */
	@ApiModelProperty(value = "验收方式")
	private String acceptanceMethod;
	/**
	 * 美尚
	 */
	@ApiModelProperty(value = "付款方式")
	private String paymentMethod;
	/**
	 * 美尚
	 */
	@ApiModelProperty(value = "账期（天）")
	private Integer accountingPeriod;
	/**
	 * 美尚
	 */
	@ApiModelProperty(value = "物料分类")
	private String itemClass;
	/**
	 * 美尚
	 */
	@ApiModelProperty(value = "变更原因")
	private String changeReason;

	/**
	 * 美尚
	 */
	@ApiModelProperty(value = "变更订单")
	private String changeOrderStatus;

	/**
	 * 运输方式
	 */
	private String shippingType;

	/**
	 * 关闭状态
	 */
	@ApiModelProperty(value = "关闭状态")
	private String offState;
	/**
	 * 关闭状态的关闭时间
	 */
//	@ApiModelProperty(value = "关闭状态的关闭时间")
//	private LocalDateTime closedTime;

	/**
	 * 来源系统的id(美尚erpId)
	 */
	@ApiModelProperty(value = "来源系统的id(美尚erpId)")
	private String sourceId;

	

}
