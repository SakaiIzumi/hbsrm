package net.bncloud.order.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.bncloud.base.BaseEntity;

/**
 *
 * 类名称:    OrderCommunicateLogSave
 * 类描述:    TODO
 * 创建人:    lvxiangyi
 * 创建时间:  2021/4/25 3:27 下午
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_order_communicate_log_save")
public class OrderCommunicateLogSave extends BaseEntity {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 产品ID
	 */
	private Long orderProductDetailsId;
	
	/**
	 * 采购单号
	 */
	private String purchaseOrderCode;
	
	/**
	 * 答交json
	 */
	private String entityJson;
	
	/**
	 * 当前保存来源
	 */
	private Integer sysType;
	
}
