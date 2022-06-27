package net.bncloud.order.mapper;

import net.bncloud.order.entity.OrderCommunicateLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * <p>
 * 订单答交日志表 Mapper 接口
 * </p>
 *
 * @author Auto-generator
 * @since 2021-03-12
 */
public interface OrderCommunicateLogMapper extends BaseMapper<OrderCommunicateLog> {
	
	
	/**
	 * 新增库存
	 * @param communicateId
	 * @param sendNum
	 * @return
	 */
	int addProductStock(Long communicateId, BigDecimal sendNum);
	
	/**
	 * 减去库存
	 * @param communicateId
	 * @param sendNum
	 * @return
	 */
	int subtractionProductStock(Long communicateId, BigDecimal sendNum);
	
	/**
	 * 查询最大批次号
	 * @return
	 */
	Integer getMaxBatch(@Param("productDetailsId") Long productDetailsId,@Param("purchaseOrderCode") String purchaseOrderCode);
	
}
