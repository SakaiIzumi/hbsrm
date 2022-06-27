package net.bncloud.order.service.impl;

import net.bncloud.order.entity.OrderChangeLog;
import net.bncloud.order.mapper.OrderChangeLogMapper;
import net.bncloud.order.service.IOrderChangeLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import net.bncloud.base.BaseServiceImpl;

/**
 * <p>
 * 修改日志表 服务实现类
 * </p>
 *
 * @author 吕享1义
 * @since 2021-03-12
 */
@Service
public class OrderChangeLogServiceImpl extends BaseServiceImpl<OrderChangeLogMapper, OrderChangeLog> implements IOrderChangeLogService {

}
