package net.bncloud.order.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.pageable.PageUtils;
import net.bncloud.order.entity.ExportOrderModel;
import net.bncloud.order.entity.Order;
import net.bncloud.order.param.OrderParam;
import net.bncloud.order.param.SendOrderParam;
import net.bncloud.order.service.IOrderService;
import net.bncloud.order.vo.MsgCountVo;
import net.bncloud.order.vo.OrderVo;
import net.bncloud.order.wrapper.OrderWrapper;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

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
 * 订单：采购工作台
 */
@RestController
@RequestMapping("/zc/order")
@AllArgsConstructor
@Slf4j
public class OrderController {

    private final IOrderService iOrderService;


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
     *
     * @param purchaseOrderCode 采购单号
     * @return
     */
    @GetMapping("/getOrderDetails")
    @ApiOperation(value = "详情", notes = "传入order")
    public R<OrderVo> getOrderDetails(@RequestParam(value = "purchaseOrderCode") String purchaseOrderCode) {
        OrderVo resOrder = iOrderService.getOrderDetails(purchaseOrderCode);

        String shippingType = resOrder.getShippingType();
        if (shippingType.equals("1")) {
            resOrder.setShippingType("车辆运输");
        } else if (shippingType.equals("2")) {
            resOrder.setShippingType("空运");
        } else if (shippingType.equals("3")) {
            resOrder.setShippingType("船运");
        } else {
            resOrder.setShippingType("");
        }

        return R.data(resOrder);
    }


    /**
     * 发送订单
     */
    @PostMapping("/sendOrder")
    @ApiOperation(value = "发起答交订单协同", notes = "传入Order")
    public R sendOrder(@RequestBody SendOrderParam sendOrderParam) {


        iOrderService.sendOrder(sendOrderParam);
        return R.success("发起成功");
    }

    /**
     * 确认订单
     */
    @GetMapping("/confirmOrder")
    @ApiOperation(value = "确认订单", notes = "传入确认订单")
    public R confirmOrder(@RequestParam(value = "purchaseOrderCode") String purchaseOrderCode) {
        iOrderService.confirmOrder(purchaseOrderCode);
        return R.success("操作成功");
    }


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
     *
     * @return
     */
    @PostMapping("/getOrderList")
    public R<PageImpl<OrderVo>> selectListPage(Pageable pageable, @RequestBody QueryParam<OrderParam> queryParam) {
        final IPage<OrderVo> orderVoIPage = iOrderService.selectListPage(PageUtils.toPage(pageable), queryParam);
        return R.data(PageUtils.result(orderVoIPage));
    }

    /**
     * 导出
     *
     * @return
     */
    @PostMapping("/exportOrder")
    public void selectListPageExcelExport(HttpServletResponse response, @RequestBody QueryParam<OrderParam> queryParam) {
        List<ExportOrderModel> models = iOrderService.getOrderListByCondition(queryParam);
        try {
            String fileName = "订单"+System.currentTimeMillis()+ ExcelTypeEnum.XLSX.getValue();
            response.setContentType("application/vnd.ms-excel");
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

    /**
     * 查询待办消息数
     *
     * @return
     */
    @PostMapping("/getMsgCount")
    @ApiOperation(value = "查询待办消息数", notes = "查询待办消息数")
    public R getMsgCount() {
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



}
