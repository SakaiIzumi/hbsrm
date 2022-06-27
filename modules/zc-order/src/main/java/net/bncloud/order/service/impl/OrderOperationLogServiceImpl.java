package net.bncloud.order.service.impl;

import net.bncloud.order.entity.OrderOperationLog;
import net.bncloud.order.mapper.OrderOperationLogMapper;
import net.bncloud.order.service.IOrderOperationLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import net.bncloud.base.BaseServiceImpl;

/**
 * <p>
 * 订单操作记录表 服务实现类
 * </p>
 *
 * @author lv
 * @since 2021-03-12
 */
@Service
public class OrderOperationLogServiceImpl extends BaseServiceImpl<OrderOperationLogMapper, OrderOperationLog> implements IOrderOperationLogService {

}
