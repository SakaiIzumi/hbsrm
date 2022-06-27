package net.bncloud.order.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.util.CollectionUtil;
import net.bncloud.order.entity.ExportOrderModel;
import net.bncloud.order.entity.Order;
import net.bncloud.order.param.OrderParam;
import net.bncloud.order.service.IOrderService;
import net.bncloud.order.vo.MsgCountVo;
import net.bncloud.order.vo.OrderVo;
import net.bncloud.order.wrapper.OrderWrapper;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;


/**
 * <p>
 * 订单表 订单表 前端控制器
 * </p>
 *
 * @author lv
 * @since 2021-03-09
 */

/**
 * 订单：销售工作台
 */
@RestController
@RequestMapping("/zy/order")
@Slf4j
public class OrderController {
	
	
	@Autowired
	private IOrderService iOrderService;
	
	
	/**
	 * 通过id查询
	 */
	@GetMapping("/getById")
	@ApiOperation(value = "通过id查询", notes = "传入notice")
	public R<OrderVo> getById(@RequestParam("id") Long id) {
		Order order = iOrderService.getById(id);
		OrderVo orderVo = OrderWrapper.build().entityVO(order);
		return R.data(orderVo);
	}
	
	
	/**
	 * 获取订单详情
 	 * @param purchaseOrderCode 采购单号
	 * @return
	 */
	@GetMapping("/getOrderDetails")
	@ApiOperation(value = "详情", notes = "传入order")
	public R<Order> getOrderDetails(@RequestParam(value = "purchaseOrderCode") String purchaseOrderCode) {
		OrderVo resOrder = iOrderService.getOrderDetails(purchaseOrderCode);
		return R.data(resOrder);
	}
	
	
	/**
	 * 新增
	 */
	@PostMapping("/save")
	@ApiOperation(value = "新增", notes = "传入Order")
	public R save(@RequestBody Order order) {
		iOrderService.save(order);
		return R.success("保存成功");
	}
	
	
//	/**
//	 * 发起答交订单协同
//	 */
//	@PostMapping("/sendCommunicateOrder")
//	@ApiOperation(value = "发起答交订单协同", notes = "传入Order")
//	public R sendCommunicateOrder(@RequestBody List<OrderCommunicateLogParam>  sendOrderParam) {
//		iOrderService.sendOrder(sendOrderParam);
//		return R.success("发起成功");
//	}
//
//
//	/**
//	 * 发起变更答交订单协同
//	 */
//	@PostMapping("/sendChangeOrder")
//	@ApiOperation(value = "发起变更答交订单协同", notes = "传入Order")
//	public R sendChangeOrder(@RequestBody List<OrderCommunicateLogParam>  sendOrderParam) {
//		iOrderService.sendOrderChange(sendOrderParam);
//		return R.success("发起成功");
//	}
//
//
//
//	/**
//	 * 提醒
//	 */
//	@GetMapping("/sendRemindMsg")
//	@ApiOperation(value = "提醒", notes = "传入提醒参数")
//	public R sendRemindMsg(@RequestParam(value = "purchaseOrderCode") String purchaseOrderCode) {
//
//		iOrderService.sendRemindMsg(purchaseOrderCode);
//
//		return R.success("操作成功");
//	}
//
//	/**
//	 * 退回
//	 * @param purchaseOrderCode
//	 * @return
//	 */
//	@GetMapping("/goBackOrder")
//	@ApiOperation(value = "退回", notes = "退回")
//	public R goBackOrder(@RequestParam(value = "purchaseOrderCode") String purchaseOrderCode){
//
//		iOrderService.goBackOrder(purchaseOrderCode);
//		return R.success();
//	}
	
	
	/**
	 * 确认订单
	 */
	@GetMapping("/confirmOrder")
	@ApiOperation(value = "确认订单", notes = "传入确认订单")
	public R confirmOrder(@RequestParam(value = "purchaseOrderCode") String purchaseOrderCode) {
		iOrderService.confirmOrder(purchaseOrderCode);
		return R.success("操作成功");
	}
	
//
//	/**
//	 * 重推签约
//	 */
//	@GetMapping("/contractSign")
//	@ApiOperation(value = "重推签约", notes = "重推签约")
//	public R contractSign(@RequestParam(value = "purchaseOrderCode") String purchaseOrderCode) {
//		return R.success("操作成功");
//	}
//
//	/**
//	 * 保存协同
//	 */
//	@PostMapping("/saveCommunicateOrder")
//	@ApiOperation(value = "发起答交订单协同", notes = "传入Order")
//	public R saveCommunicateOrder(@RequestBody List<OrderCommunicateLogParam> sendOrderParam) {
//
//		iOrderService.saveCommunicateOrder(sendOrderParam);
//		return R.success("发起成功");
//	}
//
//
//	/**
//	 * 挂起订单
//	 */
//	@GetMapping("/stopOrder")
//	@ApiOperation(value = "挂起订单", notes = "传入采购订单")
//	public R stopOrder(@RequestParam(value = "purchaseOrderCode") String purchaseOrderCode) {
//		iOrderService.stopOrder(purchaseOrderCode);
//		return R.success("操作成功");
//	}
//
//
//	/**
//	 * 启动订单
//	 */
//	@GetMapping("/startOrder")
//	@ApiOperation(value = "启动订单", notes = "传入采购订单")
//	public R startOrder(@RequestParam(value = "purchaseOrderCode") String purchaseOrderCode) {
//		iOrderService.startOrder(purchaseOrderCode);
//		return R.success("操作成功");
//	}
//
//
	/**
	 * 确定变更
	 */
	@GetMapping("/confirmOrderChange")
	@ApiOperation(value = "确定变更", notes = "传入采购订单")
	public R confirmChangeOrder(@RequestParam(value = "purchaseOrderCode") String purchaseOrderCode) {
		iOrderService.confirmChangeOrder(purchaseOrderCode);
		return R.success("操作成功");
	}
//
//
//	/**
//	 * 确定差异
//	 */
//	@GetMapping("/confirmDifferenceOrder")
//	@ApiOperation(value = "确定变更", notes = "传入采购订单")
//	public R confirmDifferenceOrder(@RequestParam(value = "purchaseOrderCode") String purchaseOrderCode) {
//		iOrderService.confirmDifferenceOrder(purchaseOrderCode);
//		return R.success("操作成功");
//	}
//
//
	
	/**
	 * 通过id删除
	 */
	@DeleteMapping("/deleteById/{id}")
	@ApiOperation(value = "通过id删除", notes = "ids")
	public R delete(@PathVariable(value = "id") String ids) {
		String[] idsStrs = ids.split(",");
		for (String id : idsStrs) {
			iOrderService.removeById(Long.parseLong(id));
		}
		return R.success();
	}
	
	/**
	 * 修改
	 */
	@PostMapping("/update")
	@ApiOperation(value = "修改", notes = "传入order")
	public R updateById(@RequestBody Order order) {
		iOrderService.updateById(order);
		return R.success();
	}
	
	
	/**
	 * 查询列表
	 */
	@PostMapping("/getOrderList")
	public R<Page<OrderVo>> selectListPage(@RequestBody QueryParam<OrderParam> queryParam, Pageable pageable) {
		return R.data(iOrderService.selectListPage(queryParam, pageable));
	}
	
//	/**
////	 * 分页查询已经答交完成的订单
////	 */
////	@PostMapping("/confirmOrderList")
////	public R confirmOrderList(@RequestBody  PageParam<OrderParam> pageParam) {
////		OrderParam param = pageParam.getParam();
////		//对条件进行初始值设置
////		param.setOrderStatus(OrderTypeCode.ORDER_CONFIRM.getCode());
////		return R.data(iOrderService.selectListPage(pageParam));
////	}
	
	
	
	/**
	 * 查询待办消息数
	 * @return
	 */
	@PostMapping("/getMsgCount")
	@ApiOperation(value = "查询待办消息数", notes = "查询待办消息数")
	public R getMsgCount(){
		MsgCountVo msgCount = iOrderService.getMsgCount();
		return R.data(msgCount);
	}
	
	/**
	 * 查询总待办消息数
	 *
	 * @return
	 */
	@PostMapping("/getMsgTotalCount")
	@ApiOperation(value = "查询待办消息数", notes = "查询待办消息数")
	public R getMsgTotalCount() {
		MsgCountVo msgCount = iOrderService.getMsgCount();
		int total = msgCount.getDifferenceMsgCount() + msgCount.getNotFinishedCount() +
				msgCount.getReturnCount() + msgCount.getStopCount() + msgCount.getUnconfirmedChangeCount() + msgCount.getWaitForUnconfirmedChangeCount() +
				msgCount.getWaitingForAnswersCount();
		return R.data(total);
	}

	/**
	 * 导出
	 * @param response
	 * @param queryParam
	 */
	@PostMapping("/exportOrder")
	@ApiOperation(value = "导出")
	public void exportOrder(HttpServletResponse response,@RequestBody QueryParam<OrderParam> queryParam){
		List<ExportOrderModel> models = iOrderService.getOrderListByCondition(queryParam);
		try {
			String fileName = "订单"+System.currentTimeMillis()+ ExcelTypeEnum.XLSX.getValue();
			response.setContentType("application/vnd.ms-excel");
			// response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			response.setCharacterEncoding("UTF-8");
			// 这里URLEncoder.encode可以防止中文乱码
			fileName= URLEncoder.encode(fileName, "UTF-8");
			response.setHeader("Content-disposition", "attachment;filename=" + fileName);
			response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");

			//内容样式策略
			WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
			//垂直居中,水平居中
			contentWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
			contentWriteCellStyle.setBorderLeft(BorderStyle.THIN);
			contentWriteCellStyle.setBorderTop(BorderStyle.THIN);
			contentWriteCellStyle.setBorderRight(BorderStyle.THIN);
			contentWriteCellStyle.setBorderBottom(BorderStyle.THIN);
			//设置 自动换行
			contentWriteCellStyle.setWrapped(true);
			// 字体策略
			WriteFont contentWriteFont = new WriteFont();
			// 字体大小
			contentWriteFont.setFontHeightInPoints((short) 10);
			contentWriteCellStyle.setWriteFont(contentWriteFont);
			//头策略使用默认
			WriteCellStyle headWriteCellStyle = new WriteCellStyle();

			EasyExcel.write(response.getOutputStream(), ExportOrderModel.class)
					.head(ExportOrderModel.class)
					.registerWriteHandler(new HorizontalCellStyleStrategy(headWriteCellStyle,contentWriteCellStyle))
					.sheet("订单")
					//设置默认样式及写入头信息开始的行数
					.useDefaultStyle(true).relativeHeadRowIndex(0)
					.doWrite(models);
		} catch (IOException e) {
			log.error("导出订单数据失败，错误信息：{}", JSON.toJSONString(e.getMessage()));
			e.printStackTrace();
		}
	}


	
	
}
