package net.bncloud.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.order.entity.OrderProductDetails;
import net.bncloud.order.param.OrderAsPlanParam;
import net.bncloud.order.param.OrderParam;
import net.bncloud.order.param.OrderProductDetailsParam;
import net.bncloud.order.vo.OrderAsPlanVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

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
	 * 校验库存
	 * @param communicateId
	 * @return
	 */
	BigDecimal checkProductStock(Long communicateId);
	
	
	/**
	 * 校验订单状态
	 * @param communicateId
	 * @return
	 */
	int checkProductStatus(Long communicateId);

	/**
	 * 分页列表查询
	 */
	List<OrderProductDetails> selectListPage(IPage page, QueryParam<OrderProductDetailsParam> pageParam);

	/**
	 *
	 * @param orderProductDetailSourceId
	 * @param receiptQuantity
	 */
	@Update("update t_order_product_details set inventory_quantity = IFNULL(inventory_quantity,0) + #{receiptQuantity} where source_id = #{orderProductDetailSourceId}")
    int addInventoryQuantity(@Param("orderProductDetailSourceId") String orderProductDetailSourceId, @Param("receiptQuantity") BigDecimal receiptQuantity);

	IPage<OrderAsPlanVo> orderAsPlan(IPage toPage, @Param("queryParam")QueryParam<OrderAsPlanParam> queryParam, @Param("productCodeList")List<String> productCodeList);
}
