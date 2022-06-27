package net.bncloud.order.service;

import net.bncloud.order.entity.OrderCommunicateLog;
import net.bncloud.base.BaseService;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 订单答交日志表 服务类
 * </p>
 *
 * @author Auto-generator
 * @since 2021-03-12
 */
public interface IOrderCommunicateLogService extends BaseService<OrderCommunicateLog> {
	/**
	 * 查询最大批次号
	 * @param productDetailsId
	 * @param purchaseOrderCode
	 * @return
	 */
	public Integer getMaxBatch(Long productDetailsId,String purchaseOrderCode);
}
