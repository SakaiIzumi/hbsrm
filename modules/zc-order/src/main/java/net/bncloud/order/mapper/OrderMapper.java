package net.bncloud.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.order.entity.Order;
import net.bncloud.order.param.OrderParam;
import org.apache.ibatis.annotations.Param;

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

	Order getByPurchaseOrderCode(String code);

	/**
	 * mrp按订单送货创建收料通知单回写的更新方法
	 * @return
	 */
    void updateErpId(@Param("id")Long orderId, @Param("fId")Long fId, @Param("fNumber")String fNumber, @Param("code")String code);
}
