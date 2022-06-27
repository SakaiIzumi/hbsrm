package net.bncloud.order.vo;/**
 * 创建人:    lv
 */

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * 类名称:    ConfirmOrderVo
 * 类描述:    收发货查询产品列表视图对象
 * 创建人:    lvxiangyi
 * 创建时间:  2021/4/6 3:05 下午
 */
@Data
public class ConfirmOrderVo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 主键
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long communicateId;
	
	
	/**
	 * 产品编码
	 */
	private String productCode;
	
	/**
	 * 产品名称
	 */
	private String productName;
	
	/**
	 * 产品规格
	 */
	private String productSpecs;
	
	/**
	 * 采购单位
	 */
	private String purchaseUnit;
	
	/**
	 * 采购说明
	 */
	private String purchaseRemarks;
	
	/**
	 * 项次
	 */
	private String itemCode;
	
	/**
	 * 采购编码
	 */
	private String purchaseOrderCode;
	
	/**
	 * 交货日期
	 */
	private Date deliveryTime;
	
	
	/**
	 * 采购数量
	 */
	private BigDecimal purchaseNum;

	/**
	 * 单价
	 */
	private BigDecimal unitPrice;
	
	/**
	 * 已发数量
	 */
	private BigDecimal sendNum;
	
	/**
	 * 收货地址
	 */
	private String receivingAddress;
	
	/**
	 * 备注
	 */
	private String orderRemarks;
	
}
