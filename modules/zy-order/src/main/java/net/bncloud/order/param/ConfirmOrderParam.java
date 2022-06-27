package net.bncloud.order.param;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *
 * 类名称:    ConfirmOrderParam
 * 类描述:    TODO
 * 创建人:    lvxiangyi
 * 创建时间:  2021/4/9 11:05 上午
 */
@Data
public class ConfirmOrderParam implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 主键
	 */
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
	 * 交货日期开始
	 */
	private String deliveryTimeStart;
	
	/**
	 * 交货日期结束
	 */
	private String deliveryTimeEnd;
	
	
	/**
	 * 采购数量
	 */
	private BigDecimal purchaseNum;
	
	/**
	 * 已发数量
	 */
	private BigDecimal sendNum;
	
	/**
	 * 收货地址
	 */
	private String receivingAddress;
	
	/**
	 * 客户编码
	 */
	private String purchaseCode;
	
	/**
	 * 供应商编码
	 */
	private String supplierCode;
	
	
	/**
	 * 备注
	 */
	private String orderRemarks;
	
	/**
	 * 选中的产品
	 */
	private List<Long> selectCommunicateIdList;
	
	
	
	
}
