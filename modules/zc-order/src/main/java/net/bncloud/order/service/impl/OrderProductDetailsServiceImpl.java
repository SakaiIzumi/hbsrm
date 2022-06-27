package net.bncloud.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.exception.ApiException;
import net.bncloud.common.exception.Asserts;
import net.bncloud.common.exception.BizException;
import net.bncloud.common.pageable.PageUtils;
import net.bncloud.common.security.SecurityUtils;
import net.bncloud.common.security.Supplier;
import net.bncloud.common.util.CollectionUtil;
import net.bncloud.common.util.SumUtils;
import net.bncloud.order.constants.*;
import net.bncloud.order.entity.OrderCommunicateLog;
import net.bncloud.order.entity.OrderCommunicateLogSave;
import net.bncloud.order.entity.OrderProductDetails;
import net.bncloud.order.entity.OrderProductDetailsHistory;
import net.bncloud.order.mapper.OrderProductDetailsMapper;
import net.bncloud.order.param.OrderAsPlanParam;
import net.bncloud.order.param.OrderParam;
import net.bncloud.order.param.OrderProductDetailsParam;
import net.bncloud.order.param.StockParam;
import net.bncloud.order.service.IOrderCommunicateLogSaveService;
import net.bncloud.order.service.IOrderCommunicateLogService;
import net.bncloud.order.service.IOrderProductDetailHistoryService;
import net.bncloud.order.service.IOrderProductDetailsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.bncloud.order.vo.*;
import net.bncloud.order.wrapper.OrderCommunicateLogWrapper;
import net.bncloud.order.wrapper.OrderProductDetailsHistoryWrapper;
import net.bncloud.order.wrapper.OrderProductDetailsWrapper;
import net.bncloud.order.wrapper.OrderWrapper;
import net.bncloud.service.api.delivery.feign.OrderDeliverySupplierFeignClient;
import net.bncloud.support.Condition;
import net.bncloud.utils.AuthUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import net.bncloud.base.BaseServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 产品明细表 产品明细表 服务实现类
 * </p>
 *
 * @author lv
 * @since 2021-03-12
 */
@Service
public class OrderProductDetailsServiceImpl extends BaseServiceImpl<OrderProductDetailsMapper, OrderProductDetails> implements IOrderProductDetailsService {
	
	private static final Logger logger = LoggerFactory.getLogger(OrderProductDetailsServiceImpl.class);

	@Resource
	private OrderDeliverySupplierFeignClient orderDeliverySupplierFeignClient;
	
	/**
	 * 答交日志业务
	 */
	@Autowired
	private IOrderCommunicateLogService iOrderCommunicateLogService;

	@Autowired
	private IOrderProductDetailHistoryService iOrderProductDetailHistoryService;

//	@Autowired
//	private IOrderCommunicateLogSaveService iOrderCommunicateLogSaveService;

	@Resource
	private OrderProductDetailsMapper productDetailsMapper;



	@Override
	public IPage<OrderProductDetailsVo> assembleOrderProductDetailHistory(IPage<OrderProductDetailsVo> page) {
		//取出业务数据
		List<OrderProductDetailsVo> records = page.getRecords();
		records.forEach(record->{
			//获取历史变更记录
			//获取答交日志
			OrderProductDetailsHistory orderProductDetailsHistoryQuery = new OrderProductDetailsHistory();
			orderProductDetailsHistoryQuery.setPdId(record.getId());
			List<OrderProductDetailsHistory> list = iOrderProductDetailHistoryService.list(Condition.getQueryWrapper(orderProductDetailsHistoryQuery).
					lambda().orderByDesc(OrderProductDetailsHistory::getCreatedDate));
			//变更颜色处理
			List<OrderProductDetailsHistoryVo> orderVoPageResult = OrderProductDetailsHistoryWrapper.build().listVO(list);
			orderVoPageResult.forEach(item->{
				if (StringUtils.isNotBlank(item.getBeforeChange())) {
					ColorChange orderCommunicateLogSaves = JSON.parseObject(item.getBeforeChange(), ColorChange.class);
					item.setColorChange(orderCommunicateLogSaves);
					item.setDeliveryTimeStatus(!orderCommunicateLogSaves.getDeliveryTime()?ColorCode.CHANGE_ORDER.getColor():ColorCode.NONE.getColor());
					item.setTaxRateStatus(!orderCommunicateLogSaves.getTaxRate()?ColorCode.CHANGE_ORDER.getColor():ColorCode.NONE.getColor());
					item.setTaxPriceStatus(!orderCommunicateLogSaves.getTaxPrice()?ColorCode.CHANGE_ORDER.getColor():ColorCode.NONE.getColor());
					item.setPurchaseNumStatus(!orderCommunicateLogSaves.getPurchaseNum()?ColorCode.CHANGE_ORDER.getColor():ColorCode.NONE.getColor());
				}
			});
			record.setOrderProductDetailsHistoryList(orderVoPageResult);


		});


		return page;
	}


//	/**
//	 * 智采逻辑
//	 * @param page
//	 * @return
//	 */
//	@Override
//	public IPage<OrderProductDetailsVo> assembleOrderProductDetail(IPage<OrderProductDetailsVo> page) {
//		//取出业务数据
//		List<OrderProductDetailsVo> records = page.getRecords();
//		//循环进行业务组装
//		for (int i = 0; i < records.size(); i++) {
//			OrderProductDetailsVo productDetails = records.get(i);
//			//获取答交日志
//			OrderCommunicateLog orderCommunicateLogQuery = new OrderCommunicateLog();
//			orderCommunicateLogQuery.setPurchaseOrderCode(productDetails.getPurchaseOrderCode());
//			orderCommunicateLogQuery.setOrderProductDetailsId(productDetails.getId());
//			List<OrderCommunicateLog> orderCommunicateLogs = iOrderCommunicateLogService.list(Condition.getQueryWrapper(orderCommunicateLogQuery).lambda().orderByDesc(OrderCommunicateLog::getBatch));
//			//转换视图对象
//			List<OrderCommunicateLogVo> orderCommunicateLogVos = OrderCommunicateLogWrapper.build().listVO(orderCommunicateLogs);
//			//填充答交日志
//			productDetails.setCommunicateLogList(orderCommunicateLogVos);
//			//查询暂存日志
//			OrderCommunicateLogSave querySaveList = new OrderCommunicateLogSave();
//			querySaveList.setPurchaseOrderCode(productDetails.getPurchaseOrderCode());
//			querySaveList.setOrderProductDetailsId(productDetails.getId());
//			querySaveList.setCreatedBy(AuthUtil.getUser().getUserId());
//			querySaveList.setSysType(ConfirmSourceCode.ZC.getCode());
//			OrderCommunicateLogSave orderCommunicateLogSave = iOrderCommunicateLogSaveService.getOne(Condition.getQueryWrapper(querySaveList));
//			if (orderCommunicateLogSave != null) {
//				String entityJson = orderCommunicateLogSave.getEntityJson();
//				List<OrderCommunicateLog> orderCommunicateLogSaves = JSON.parseArray(entityJson, OrderCommunicateLog.class);
//				productDetails.setCommunicateLogSaveList(orderCommunicateLogSaves);
//			} else {
//				productDetails.setCommunicateLogSaveList(new ArrayList<>());
//			}
//		}
//		return page;
//	}
//
	
	@Override
	@Transactional
	public void addProductStock(List<StockParam> stockParamList) {
		
		logger.info("释放库存参数入参{}", stockParamList);
		
		for (int i = 0; i < stockParamList.size(); i++) {
			StockParam stockParam = stockParamList.get(i);
			Long communicateId = stockParam.getCommunicateId();
			BigDecimal sendNum = stockParam.getSendNum();
			if (sendNum.compareTo(BigDecimal.ZERO) == 0) {
				throw new ApiException(ZcResultCode.NOT_ZERO_ERROR_SUB.getCode(), ZcResultCode.NOT_ZERO_ERROR_SUB.getMessage());
			}
			//校验订单状态
			
			//1.通过communicateId查询出产品ID和采购单号
			OrderCommunicateLog orderCommunicateLog = iOrderCommunicateLogService.getById(communicateId);
			//2。通过查出的产品ID查询出产品
			OrderProductDetails orderProductDetails = getById(orderCommunicateLog.getOrderProductDetailsId());
			//校验是否超出有效库存
			//BigDecimal sendNum1 = orderCommunicateLog.getPurchaseNum()==null?new BigDecimal("0.00"):orderCommunicateLog.getPurchaseNum();
			BigDecimal subtract = SumUtils.subtract(orderCommunicateLog.getSendNum(), sendNum);
			if (subtract.compareTo(BigDecimal.ZERO) < 0) {
				throw new BizException(ZcResultCode.NOT_ENOUGH_ERROR);
			}
			
			if (subtract.compareTo(BigDecimal.ZERO) == 0) {
				//把状态改成部分发货
				orderProductDetails.setTakeOverStatus(TakeOverCode.TAKE_OVER_PART.getCode());
			} else {
				//把状态改成部分发货
				orderProductDetails.setTakeOverStatus(TakeOverCode.TAKE_OVER_WAIT.getCode());
			}
			orderCommunicateLog.setSendNum(subtract);
			iOrderCommunicateLogService.updateById(orderCommunicateLog);
			//进行库存释放
			baseMapper.addProductStock(communicateId, subtract);
			//communicate库存新增成功就修改产品库存释放
			orderProductDetails.setSendNum(subtract);
			
			updateById(orderProductDetails);
			
		}
	}
	
	@Override
	@Transactional
	public void subtractionProductStock(List<StockParam> stockParamList) {
		logger.info("发货参数入参{}", stockParamList);
		for (int i = 0; i < stockParamList.size(); i++) {
			StockParam stockParam = stockParamList.get(i);
			Long communicateId = stockParam.getCommunicateId();
			BigDecimal sendNum = stockParam.getSendNum();
			
			if (sendNum.compareTo(BigDecimal.ZERO) == 0) {
				throw new ApiException(ZcResultCode.NOT_ZERO_ERROR.getCode(), ZcResultCode.NOT_ZERO_ERROR.getMessage());
			}
			
			//校验订单状态
			int i1 = baseMapper.checkProductStatus(communicateId);
			if (i1 > 0) {
				//1.通过communicateId查询出产品ID和采购单号
				OrderCommunicateLog orderCommunicateLog = iOrderCommunicateLogService.getById(communicateId);
				//2。通过查出的产品ID查询出产品
				OrderProductDetails orderProductDetails = getById(orderCommunicateLog.getOrderProductDetailsId());
				//校验是否超出有效库存
				BigDecimal purchaseNum = orderCommunicateLog.getPurchaseNum();
				BigDecimal sendNum1 = orderCommunicateLog.getSendNum() == null ? new BigDecimal("0.00") : orderCommunicateLog.getSendNum();
				BigDecimal add = SumUtils.add(sendNum, sendNum1);
				if (purchaseNum.compareTo(add) < 0) {
					throw new BizException(ZcResultCode.OUT_ERROR);
				}
				orderCommunicateLog.setSendNum(add);
				baseMapper.subtractionProductStock(communicateId, add);
				
				//修改对应的状态
				//如果采购数量=已经发货数量 那么把产品改成发货完成否则为发货中
				if (purchaseNum.compareTo(add) == 0) {
					orderCommunicateLog.setTakeOverStatus(TakeOverCode.TAKE_OVER_FINISH.getCode());
				} else {
					orderCommunicateLog.setTakeOverStatus(TakeOverCode.TAKE_OVER_PART.getCode());
				}
				iOrderCommunicateLogService.updateById(orderCommunicateLog);
				//判断产品的状态
				//首先查询该产品下面所有的交期是否全部发货完成
				LambdaQueryWrapper<OrderCommunicateLog> queryCountCommunicate = Wrappers.<OrderCommunicateLog>query().lambda()
						.eq(OrderCommunicateLog::getStatus, CommunicateStatusCode.UPDATED.getCode())
						.ne(OrderCommunicateLog::getTakeOverStatus, TakeOverCode.TAKE_OVER_FINISH.getCode());
				int count = iOrderCommunicateLogService.count(queryCountCommunicate);
				if (count == 0) {
					orderProductDetails.setTakeOverStatus(TakeOverCode.TAKE_OVER_FINISH.getCode());
				} else {
					orderProductDetails.setTakeOverStatus(TakeOverCode.TAKE_OVER_PART.getCode());
				}
				//communicate库存新增成功就修改产品库存
				orderProductDetails.setSendNum(SumUtils.add(orderProductDetails.getSendNum(), add));
				updateById(orderProductDetails);
			} else {
				throw new BizException(ZcResultCode.NOT_STATUS_ERROR);
			}
		}
		
		
	}

	/**
	 * 自定义分页
	 *
	 */
	@Override
	public IPage<OrderProductDetails> selectPage(IPage<OrderProductDetails> page, QueryParam<OrderProductDetailsParam> queryParam) {
		return page.setRecords(productDetailsMapper.selectListPage(page, queryParam));
	}


	@Override
	public PageImpl<OrderAsPlanVo> orderAsPlan(IPage<OrderAsPlanVo> toPage, QueryParam<OrderAsPlanParam> queryParam) {
		if (queryParam == null || queryParam.getParam() == null || queryParam.getParam().getPurchaseCode() == null) {
			throw new RuntimeException("请先选择客户再选择按订单送货");
		}

		//通过登录工具获取当前供应商的id和编码
		Supplier supplier = SecurityUtils.getCurrentSupplier().get();
		String supplierCode = supplier.getSupplierCode();
		Long supplierId = supplier.getSupplierId();

		//过滤当前登录的供应商可以查询的订单计划
		queryParam.getParam().setSupplierCode(supplierCode);

		//排程供应商只能查看自己的物料的订单计划
		R<List<String>> materialCodeListForR = orderDeliverySupplierFeignClient.selectOrderDeliverySupplierProductCode(supplierId);
		Asserts.isTrue(materialCodeListForR.isSuccess(),"远程获取排程供应商所属物料失败");

		List<String> productCodeList = materialCodeListForR.getData();
		//为空 排程供应商没有关联的物料 返回空就可以了
		if(CollectionUtil.isEmpty(productCodeList)){
			return PageUtils.result( new Page<>(toPage.getCurrent(),toPage.getSize(),0));
		}

		IPage<OrderAsPlanVo> orderAsPlanVoIPage = productDetailsMapper.orderAsPlan(toPage, queryParam, productCodeList);

		return PageUtils.result(orderAsPlanVoIPage);
	}
}
