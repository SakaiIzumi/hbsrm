package net.bncloud.order.service.impl;

import net.bncloud.order.entity.OrderCommunicateLog;
import net.bncloud.order.mapper.OrderCommunicateLogMapper;
import net.bncloud.order.service.IOrderCommunicateLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import net.bncloud.base.BaseServiceImpl;

/**
 * <p>
 * 订单答交日志表 服务实现类
 * </p>
 *
 * @author Auto-generator
 * @since 2021-03-12
 */
@Service
public class OrderCommunicateLogServiceImpl extends BaseServiceImpl<OrderCommunicateLogMapper, OrderCommunicateLog> implements IOrderCommunicateLogService {
	
	@Override
	public Integer getMaxBatch(Long productDetailsId, String purchaseOrderCode) {
		Integer maxBatch = baseMapper.getMaxBatch(productDetailsId, purchaseOrderCode);
		if(maxBatch == null){
			return  0;
		}else {
			return maxBatch;
		}
	}
}
