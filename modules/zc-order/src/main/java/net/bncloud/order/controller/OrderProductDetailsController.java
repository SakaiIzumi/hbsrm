package net.bncloud.order.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.pageable.PageUtils;
import net.bncloud.order.entity.OrderProductDetails;
import net.bncloud.order.param.OrderAsPlanParam;
import net.bncloud.order.param.OrderParam;
import net.bncloud.order.param.OrderProductDetailsParam;
import net.bncloud.order.param.StockParam;
import net.bncloud.order.service.IOrderProductDetailsService;
import net.bncloud.order.vo.OrderAsPlanVo;
import net.bncloud.order.vo.OrderProductDetailsVo;
import net.bncloud.order.wrapper.OrderProductDetailsWrapper;
import net.bncloud.support.Condition;
import net.bncloud.utils.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
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
@RequestMapping("/zc/order/product-details")
public class OrderProductDetailsController {

    
    @Autowired
    private IOrderProductDetailsService iOrderProductDetailsService;

    
    /**
    * 通过id查询
    */
    @GetMapping("/getById")
    @ApiOperation(value = "根据ID查询", notes = "传入OrderProductDetails")
    public R<OrderProductDetails> getById(@PathVariable(value = "id") Long id){
        return R.data(iOrderProductDetailsService.getById(id));
    }

    /**
    * 新增
    */
    @PostMapping("/save")
    @ApiOperation(value = "新增", notes = "传入OrderProductDetails")
    public R save(@RequestBody OrderProductDetails orderProductDetails){
        iOrderProductDetailsService.save(orderProductDetails);
        return R.success();
    }

    /**
    * 通过id删除
    */
    @DeleteMapping("/deleteById/{id}")
    @ApiOperation(value = "通过id删除", notes = "传入ids")
    public R delete(@PathVariable(value = "id") String ids){
        String[] idsStrs = ids.split(",");
        for (String id:idsStrs){
            iOrderProductDetailsService.removeById(Long.parseLong(id));
        }
        return R.success();
    }

    /**
    * 修改
    */
    @PutMapping("/update")
    @ApiOperation(value = "修改", notes = "传入orderProductDetails")
    public R updateById(@RequestBody OrderProductDetails orderProductDetails){
        iOrderProductDetailsService.updateById(orderProductDetails);
        return R.success();
    }


    /**
    * 查询列表
    */
    @PostMapping("/list")
    @ApiOperation(value = "查询列表", notes = "传入orderProductDetails")
    public R list(@RequestBody OrderProductDetails orderProductDetails ){
        List<OrderProductDetails> list = iOrderProductDetailsService.list(Condition.getQueryWrapper(orderProductDetails));

        return R.data(list);
    }

    /**
    * 分页查询
    */
    @PostMapping("/getProductList")
    @ApiOperation(value = "查询列表", notes = "传入OrderProductDetailsParam")
    public R page(Pageable pageable, @RequestBody QueryParam<OrderProductDetailsParam> queryParam){

        IPage<OrderProductDetails> page = iOrderProductDetailsService.selectPage(PageUtils.toPage(pageable), queryParam);
        IPage<OrderProductDetailsVo> orderProductDetailsVoIPage = OrderProductDetailsWrapper.build().pageVO(page);
        //进行业务数据组装
        //IPage<OrderProductDetailsVo> orderProductDetailsVoIPageRes = iOrderProductDetailsService.assembleOrderProductDetail(orderProductDetailsVoIPage);
        //进行业务数据组装
        IPage<OrderProductDetailsVo> orderProductDetailsVoIPageRes = iOrderProductDetailsService.assembleOrderProductDetailHistory(orderProductDetailsVoIPage);
		return R.data(PageUtils.result(orderProductDetailsVoIPageRes));
    }
    
    
    
    /**
     * 通过产品编码和采购编号来释放库存
     * @return
     */
    @PostMapping("/releaseStock")
    public R addProductStock(@RequestBody List<StockParam> stockParamList){
        iOrderProductDetailsService.addProductStock(stockParamList);
        return R.success();
    }
    
    
    
    /**
     * 发货
     * @return
     */
    @PostMapping("/deliverGoodsStock")
    public R subtractionProductStock(@RequestBody List<StockParam> stockParamList){
        iOrderProductDetailsService.subtractionProductStock(stockParamList);
        return R.success();
    }

    /**
     * 按订单送货
     *
     */
    @PostMapping("/orderAsPlan")
    public R<PageImpl<OrderAsPlanVo>> orderAsPlan(Pageable pageable, @RequestBody QueryParam<OrderAsPlanParam> queryParam){
        @Valid OrderAsPlanParam param = queryParam.getParam();
        param.setSupplierCode(AuthUtil.getUser().getCurrentSupplier() == null ? "" : AuthUtil.getUser().getCurrentSupplier().getSupplierCode());
        if (param.getDeliveryTimeEnd() != null) {
            String deliveryTimeEnd = param.getDeliveryTimeEnd();
            String replace = deliveryTimeEnd.replace("00:00:00","23:59:59");
            param.setDeliveryTimeEnd(replace);
        }

        PageImpl<OrderAsPlanVo> orderAsPlanVos = iOrderProductDetailsService.orderAsPlan(PageUtils.toPage(pageable), queryParam);
        return R.data(orderAsPlanVos);
    }

}
