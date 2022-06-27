package net.bncloud.order.param;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 *
 * 类名称:    SendOrderParam
 * 类描述:    发起订单协同 请求对象
 * 创建人:    lvxiangyi
 * 创建时间:  2021/3/12 4:58 下午
 */
@Data
public class SendOrderParam implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 采购单号
	 */
	private String purchaseOrderCode;
	
	
	/**
	 * 关联产品表
	 */
	private Long orderProductDetailsId;
	
	
	/**
	 * 发送类型 1 差异 2 变更
	 */
	private Integer type;
	
	
	/**
	 * 操作状态 0保存 1已操作
	 */
	private Integer status;
	
	

	
}
