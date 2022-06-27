package net.bncloud.order.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.order.entity.Order;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.bncloud.order.param.OrderParam;

import java.util.List;

/**
 * <p>
 * 订单表 订单表 Mapper 接口
 * </p>
 *
 * @author lv
 * @since 2021-03-09
 */
public interface OrderMapper extends BaseMapper<Order> {
	
	/**
	 * 通过xml自定义sql
	 * @return
	 */
	List<Order> selectListPage(IPage page, QueryParam<OrderParam> queryParam);
	

}
