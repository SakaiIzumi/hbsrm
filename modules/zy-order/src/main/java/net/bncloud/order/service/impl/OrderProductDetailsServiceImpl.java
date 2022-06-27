package net.bncloud.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.base.BaseServiceImpl;
import net.bncloud.common.api.ResultCode;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.exception.BizException;
import net.bncloud.common.pageable.PageUtils;

import net.bncloud.order.constants.ColorCode;
import net.bncloud.order.constants.ConfirmSourceCode;
import net.bncloud.order.entity.OrderCommunicateLog;
import net.bncloud.order.entity.OrderCommunicateLogSave;
import net.bncloud.order.entity.OrderProductDetails;
import net.bncloud.order.entity.OrderProductDetailsHistory;
import net.bncloud.order.mapper.OrderProductDetailsMapper;
import net.bncloud.order.param.ConfirmOrderParam;
import net.bncloud.order.param.OrderProductDetailsParam;
import net.bncloud.order.service.IOrderCommunicateLogSaveService;
import net.bncloud.order.service.IOrderCommunicateLogService;
import net.bncloud.order.service.IOrderProductDetailHistoryService;
import net.bncloud.order.service.IOrderProductDetailsService;
import net.bncloud.order.vo.*;
import net.bncloud.order.wrapper.OrderCommunicateLogWrapper;
import net.bncloud.order.wrapper.OrderProductDetailsHistoryWrapper;
import net.bncloud.support.Condition;
import net.bncloud.utils.AuthUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
	
	/**
	 * 答交日志业务
	 */
	@Autowired
	private IOrderCommunicateLogService iOrderCommunicateLogService;

	@Autowired
	private IOrderProductDetailHistoryService iOrderProductDetailHistoryService;
	
	@Autowired
	private IOrderCommunicateLogSaveService iOrderCommunicateLogSaveService;
	
	@Override
	public IPage<OrderProductDetailsVo> assembleOrderProductDetail(IPage<OrderProductDetailsVo> page) {
		//取出业务数据
		List<OrderProductDetailsVo> records = page.getRecords();
		//循环进行业务组装
		for(int i =0;i<records.size();i++){
			OrderProductDetailsVo productDetails = records.get(i);
			//获取答交日志
			OrderCommunicateLog orderCommunicateLogQuery = new OrderCommunicateLog();
			orderCommunicateLogQuery.setPurchaseOrderCode(productDetails.getPurchaseOrderCode());
			orderCommunicateLogQuery.setOrderProductDetailsId(productDetails.getId());
			List<OrderCommunicateLog> orderCommunicateLogs = iOrderCommunicateLogService.list(Condition.getQueryWrapper(orderCommunicateLogQuery).lambda().orderByDesc(OrderCommunicateLog::getCreatedDate));
			//转换视图对象
			List<OrderCommunicateLogVo> orderCommunicateLogVos = OrderCommunicateLogWrapper.build().listVO(orderCommunicateLogs);
			//填充答交日志
//			productDetails.setCommunicateLogList(orderCommunicateLogVos);
			//查询暂存日志
			OrderCommunicateLogSave querySaveList = new OrderCommunicateLogSave();
			querySaveList.setPurchaseOrderCode(productDetails.getPurchaseOrderCode());
			querySaveList.setOrderProductDetailsId(productDetails.getId());
			querySaveList.setCreatedBy(AuthUtil.getUser().getUserId());
			querySaveList.setSysType(ConfirmSourceCode.ZY.getCode());
			OrderCommunicateLogSave orderCommunicateLogSave = iOrderCommunicateLogSaveService.getOne(Condition.getQueryWrapper(querySaveList));
			if(orderCommunicateLogSave != null){
				String entityJson = orderCommunicateLogSave.getEntityJson();
				List<OrderCommunicateLog> orderCommunicateLogSaves = JSON.parseArray(entityJson, OrderCommunicateLog.class);
//				productDetails.setCommunicateLogSaveList(orderCommunicateLogSaves);
			}else {
//				productDetails.setCommunicateLogSaveList(new ArrayList<>());
			}
		}
		return page;
	}

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





	@Override
	public Page<ConfirmOrderVo> confirmOrderList(Pageable pageable, QueryParam<ConfirmOrderParam> queryParam) {

		IPage<ConfirmOrderVo> page = PageUtils.toPage(pageable);
		IPage<ConfirmOrderVo> orderVoIPage = page.setRecords(baseMapper.confirmOrderList(page, queryParam));
		return PageUtils.result(orderVoIPage);
	}
	
	
	@Override
	public void checkProductStock(Long communicateId, BigDecimal sendNum) {
		BigDecimal bigDecimal = baseMapper.checkProductStock(communicateId,sendNum);
		//如果小于0那么就抛出异常
		if(bigDecimal.compareTo(BigDecimal.ZERO)<0){
			throw new BizException(ResultCode.OUT_OF_STOCK);
		}
	}

	/**
	 * 自定义分页查询
	 */
	@Override
	public IPage<OrderProductDetails> selectPage(IPage<OrderProductDetails> page, QueryParam<OrderProductDetailsParam> queryParam) {
		return page.setRecords(baseMapper.selectListPage(page, queryParam));
	}
}
