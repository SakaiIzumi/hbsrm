package net.bncloud.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.base.BaseService;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.order.entity.OrderProductDetails;
import net.bncloud.order.param.OrderAsPlanParam;
import net.bncloud.order.param.OrderParam;
import net.bncloud.order.param.OrderProductDetailsParam;
import net.bncloud.order.param.StockParam;
import net.bncloud.order.vo.OrderAsPlanVo;
import net.bncloud.order.vo.OrderProductDetailsHistoryVo;
import net.bncloud.order.vo.OrderProductDetailsVo;
import org.springframework.data.domain.PageImpl;

import java.util.List;

/**
 * <p>
 * 产品明细表 产品明细表 服务类
 * </p>
 *
 * @author lv
 * @since 2021-03-12
 */
public interface IOrderProductDetailsService extends BaseService<OrderProductDetails> {
	
//	/**
//	 * 组装产品明细表业务参数
//	 */
//	IPage<OrderProductDetailsVo> assembleOrderProductDetail(IPage<OrderProductDetailsVo> page);

	/**
	 * 组装产品明细表业务参数
	 */
	IPage<OrderProductDetailsVo> assembleOrderProductDetailHistory(IPage<OrderProductDetailsVo> page);
	
	
	/**
	 * 添加产品库存
	 */
	void addProductStock(List<StockParam> stockParamList);
	
	/**
	 * 减去产品库存
	 */
	void subtractionProductStock(List<StockParam> stockParamList);

	/**
	 * 自定义分页
	 */
	IPage<OrderProductDetails> selectPage(IPage<OrderProductDetails> page, QueryParam<OrderProductDetailsParam> queryParam);

    PageImpl<OrderAsPlanVo> orderAsPlan(IPage<OrderAsPlanVo> toPage, QueryParam<OrderAsPlanParam> queryParam);
}
