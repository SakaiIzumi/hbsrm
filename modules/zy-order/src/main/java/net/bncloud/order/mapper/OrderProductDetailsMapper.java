package net.bncloud.order.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.order.entity.OrderProductDetails;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.bncloud.order.param.ConfirmOrderParam;
import net.bncloud.order.param.OrderProductDetailsParam;
import net.bncloud.order.vo.ConfirmOrderVo;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 产品明细表 产品明细表 Mapper 接口
 * </p>
 *
 * @author lv
 * @since 2021-03-12
 */
public interface OrderProductDetailsMapper extends BaseMapper<OrderProductDetails> {
	
	
	/**
	 * 收发货查询产品
	 */
	List<ConfirmOrderVo> confirmOrderList(IPage page, QueryParam<ConfirmOrderParam> queryParam);
	
	
	/**
	 * 通过产品编码和采购编号来校验库存
	 */
	BigDecimal checkProductStock(@Param("communicateId") Long communicateId,@Param("sendNum") BigDecimal sendNum);

	/**
	 * 分页列表查询
	 */
	List<OrderProductDetails> selectListPage(IPage page, QueryParam<OrderProductDetailsParam> pageParam);
	
}
