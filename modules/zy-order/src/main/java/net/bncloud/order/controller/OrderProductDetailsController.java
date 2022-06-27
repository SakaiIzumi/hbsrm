package net.bncloud.order.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.pageable.PageUtils;
import net.bncloud.order.entity.OrderProductDetails;
import net.bncloud.order.param.ConfirmOrderParam;
import net.bncloud.order.param.OrderProductDetailsParam;
import net.bncloud.order.service.IOrderProductDetailsService;
import net.bncloud.order.vo.OrderProductDetailsVo;
import net.bncloud.order.wrapper.OrderProductDetailsWrapper;
import net.bncloud.support.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;


/**
 * <p>
 * 产品明细表 产品明细表 前端控制器
 * </p>
 *
 * @author lv
 * @since 2021-03-12
 */
@RestController
@RequestMapping("/zy/order/product-details")
public class OrderProductDetailsController {
	
	
	@Autowired
	private IOrderProductDetailsService iOrderProductDetailsService;
	
	
	/**
	 * 通过id查询
	 */
	@GetMapping("/getById")
	@ApiOperation(value = "根据ID查询", notes = "传入OrderProductDetails")
	public R<OrderProductDetails> getById(@PathVariable(value = "id") Long id) {
		return R.data(iOrderProductDetailsService.getById(id));
	}
	
	/**
	 * 新增
	 */
	@PostMapping("/save")
	@ApiOperation(value = "新增", notes = "传入OrderProductDetails")
	public R save(@RequestBody OrderProductDetails orderProductDetails) {
		iOrderProductDetailsService.save(orderProductDetails);
		return R.success();
	}
	
	/**
	 * 通过id删除
	 */
	@DeleteMapping("/deleteById/{id}")
	@ApiOperation(value = "通过id删除", notes = "传入ids")
	public R delete(@PathVariable(value = "id") String ids) {
		String[] idsStrs = ids.split(",");
		for (String id : idsStrs) {
			iOrderProductDetailsService.removeById(Long.parseLong(id));
		}
		return R.success();
	}
	
	/**
	 * 修改
	 */
	@PutMapping("/update")
	@ApiOperation(value = "修改", notes = "传入orderProductDetails")
	public R updateById(@RequestBody OrderProductDetails orderProductDetails) {
		iOrderProductDetailsService.updateById(orderProductDetails);
		return R.success();
	}
	
	
	/**
	 * 查询列表
	 */
	@PostMapping("/list")
	@ApiOperation(value = "查询列表", notes = "传入orderProductDetails")
	public R list(@RequestBody OrderProductDetails orderProductDetails) {
		List<OrderProductDetails> list = iOrderProductDetailsService.list(Condition.getQueryWrapper(orderProductDetails));
		
		return R.data(list);
	}
	
	/**
	 * 分页查询
	 */
	@PostMapping("/getProductList")
	@ApiOperation(value = "查询列表", notes = "传入OrderProductDetailsParam")
	public R page(Pageable pageable, @RequestBody QueryParam<OrderProductDetailsParam> queryParam) {

		IPage<OrderProductDetails> page = iOrderProductDetailsService.selectPage(PageUtils.toPage(pageable), queryParam);
		IPage<OrderProductDetailsVo> orderProductDetailsVoIPage = OrderProductDetailsWrapper.build().pageVO(page);
		//进行业务数据组装
//		IPage<OrderProductDetailsVo> orderProductDetailsVoIPageRes = iOrderProductDetailsService.assembleOrderProductDetail(orderProductDetailsVoIPage);
		//进行业务数据组装
		IPage<OrderProductDetailsVo> orderProductDetailsVoIPageRes = iOrderProductDetailsService.assembleOrderProductDetailHistory(orderProductDetailsVoIPage);
		return R.data(PageUtils.result(orderProductDetailsVoIPageRes));
	}
	
	
	/**
	 * 分页查询已经答交完成的订单产品数据
	 */
	@PostMapping("/confirmOrderList")
	public R confirmOrderList(Pageable pageable, @RequestBody QueryParam<ConfirmOrderParam> queryParam) {
		return R.data(iOrderProductDetailsService.confirmOrderList(pageable, queryParam));
	}
	
	
	/**
	 * 通过产品编码和采购编号来校验库存
	 * @return
	 */
	@GetMapping("/checkProductStock")
	public R checkProductStock(@RequestParam(value = "communicateId")Long communicateId,@RequestParam(value = "sendNum") BigDecimal sendNum){
		iOrderProductDetailsService.checkProductStock(communicateId,sendNum);
		return R.success();
	}
	
	
	
}
