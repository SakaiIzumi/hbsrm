package net.bncloud.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.base.BaseService;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.order.entity.OrderProductDetails;
import net.bncloud.order.param.ConfirmOrderParam;
import net.bncloud.order.param.OrderProductDetailsParam;
import net.bncloud.order.vo.ConfirmOrderVo;
import net.bncloud.order.vo.OrderProductDetailsVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

/**
 * <p>
 * 产品明细表 产品明细表 服务类
 * </p>
 *
 * @author lv
 * @since 2021-03-12
 */
public interface IOrderProductDetailsService extends BaseService<OrderProductDetails> {
	
	/**
	 * 组装产品明细表业务参数
	 */
	IPage<OrderProductDetailsVo> assembleOrderProductDetail(IPage<OrderProductDetailsVo> page);
	/**
	 * 组装产品明细表业务参数
	 */
	IPage<OrderProductDetailsVo> assembleOrderProductDetailHistory(IPage<OrderProductDetailsVo> page);
	
	/**
	 * 收发货产品列表查询
	 */
	Page<ConfirmOrderVo> confirmOrderList(Pageable pageable, QueryParam<ConfirmOrderParam> queryParam);
	
	
	/**
	 * 校验产品库存
	 */
	void checkProductStock(Long communicateId, BigDecimal sendNum);

	/**
	 * 自定义分页查询
	 */
    IPage<OrderProductDetails> selectPage(IPage<OrderProductDetails> toPage, QueryParam<OrderProductDetailsParam> queryParam);
}
