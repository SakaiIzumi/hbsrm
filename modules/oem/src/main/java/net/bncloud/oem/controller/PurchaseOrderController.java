package net.bncloud.oem.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.pageable.PageUtils;
import net.bncloud.oem.domain.entity.AddressModule;
import net.bncloud.oem.domain.entity.FileInfo;
import net.bncloud.oem.domain.param.BatchParam;
import net.bncloud.oem.domain.param.PurchaseOrderParam;
import net.bncloud.oem.domain.param.ReceivingAddressParam;
import net.bncloud.oem.domain.param.ReturnReceivingParam;
import net.bncloud.oem.domain.vo.PurchaseOrderVo;
import net.bncloud.oem.domain.vo.RemarkVo;
import net.bncloud.oem.domain.vo.ToBeConfirmVo;
import net.bncloud.oem.mapper.ReceivingAddressMapper;
import net.bncloud.oem.service.PurchaseOrderMaterialService;
import net.bncloud.oem.service.PurchaseOrderReceivingService;
import net.bncloud.oem.service.PurchaseOrderService;
import org.apache.commons.compress.utils.Lists;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 采购订单 控制器 liyh2
 *
 * @author Auto-generator
 * @since 2022-04-24
 */
@Slf4j
@RestController
@RequestMapping("/zc/purchase/order")
public class PurchaseOrderController {
    @Autowired
    private PurchaseOrderService purchaseOrderService;
    @Autowired
    private PurchaseOrderMaterialService purchaseOrderMaterialService;
    @Autowired
    private PurchaseOrderReceivingService purchaseOrderReceivingService;
    @Autowired
    private ReceivingAddressMapper receivingAddressMapper;


    /**
     * 分页查询
     */
    @PostMapping("/page")
    @ApiOperation(value = "分页查询", notes = "")
    public R<PageImpl<PurchaseOrderVo>> page(Pageable pageable, @RequestBody QueryParam<PurchaseOrderParam> param) {
        PageImpl<PurchaseOrderVo> purchaseOrderVos = purchaseOrderService.selectList(PageUtils.toPage(pageable), param);
        return R.data(purchaseOrderVos);
    }

    /**
     * 分页待确认查询
     */
    @PostMapping("/page/toBeConfirm")
    @ApiOperation(value = "分页查询", notes = "")
    public R<PageImpl<ToBeConfirmVo>> pageToBeConfirm(Pageable pageable, @RequestBody QueryParam<PurchaseOrderParam> param) {
        PageImpl<ToBeConfirmVo> toBeConfirmVos = purchaseOrderService.selectToBeConfirmList(PageUtils.toPage(pageable), param);
        return R.data(toBeConfirmVos);
    }

    /**
     * 查看备注接口
     */
    @PostMapping("/remark/{id}")
    @ApiOperation(value = "查看备注接口")
    public R<RemarkVo> remark(@PathVariable("id")Long id) {
        RemarkVo remarkVo=purchaseOrderReceivingService.queryRemark(id);
        return R.data(remarkVo);
    }

    /**
     * 确认收货
     */
    @PutMapping("/confirm/{id}")
    @ApiOperation(value = "确认收货", notes = "id为物料收货OrderReceiving的id")
    public R confirmReceive(@PathVariable("id") Long id) {
        purchaseOrderReceivingService.confirmStatus(id);
        return R.success();
    }

    /**
     * 批量确认收货
     */
    @PutMapping("/batchConfirm")
    @ApiOperation(value = "批量确认收货")
    public R<String> batchConfirmReceive(@RequestBody BatchParam batchParam) {
        purchaseOrderReceivingService.batchConfirmReceiving(batchParam.getIds(),null);
        return R.success("操作成功");
    }

    /**
     * 待确认的批量确认收货按钮
     */
    @PutMapping("/toBebatchConfirm")
    @ApiOperation(value = "批量确认收货")
    public R<String> toBeConfirmBatchConfirmReceiving(@RequestBody BatchParam batchParam) {
        purchaseOrderReceivingService.toBeConfirmBatchConfirmReceiving(batchParam.getIds());
        return R.success("操作成功");
    }

    /**
     * 批量退回收货
     */
    @PutMapping("/batchReject")
    @ApiOperation(value = "批量确认收货")
    public R<String> batchRejectReceive(@RequestBody BatchParam batchParam) {
        purchaseOrderReceivingService.batchRejectReceiving(batchParam.getIds());
        return R.success("操作成功");
    }

    /**
     * 退回收货
     */
    @PutMapping("/reject")
    @ApiOperation(value = "退回收货")
    public R<String> reject(@RequestBody ReturnReceivingParam param) {
        try {
            purchaseOrderReceivingService.returnReceiving(param);
        } catch (Exception e) {
            log.info(e.getMessage());
            return R.fail(e.getMessage());
        }
        return R.success("操作成功");
    }

    /**
     * 统计待确认数量
     */
    @PutMapping("/staticsToBeConfirm")
    @ApiOperation(value = "统计待确认数量")
    public R<Integer> staticsToBeConfirm() {
        return R.data(purchaseOrderReceivingService.statisticsToBeConfirmQuantity());
    }

    /**
     * 调用公共接口【下载附件】
     *
     * @return
     */
    @GetMapping("/downloadModuleById")
    public R<Map<String, Long>> downloadModuleById() {
        return R.data(purchaseOrderService.getReceivingAddressExcelAttachmentId());
    }

    /**
     * 导入收货地址
     *
     * @param file
     * @return
     */
    @PostMapping("/importAddress")
    public R<FileInfo> importAddress(@RequestParam("file") MultipartFile file){
        FileInfo fileInfo=new FileInfo();
        try {
            fileInfo = purchaseOrderService.importReceivingAddress(file);
        } catch (IOException e) {
           log.error(e.getMessage());
        }
        return R.data(fileInfo);

    }

    /**
     * 导出收货地址
     *
     * @param response
     */
    @PostMapping("/exportAddress")
    public void exportReceivingAddress(HttpServletResponse response, @RequestBody QueryParam<ReceivingAddressParam> param) {
        //获取收货地址的集合
        ArrayList<AddressModule> addressModules = Lists.newArrayList();
        Optional.ofNullable(receivingAddressMapper.selectListPage(null, param)).ifPresent(receivingAddressVos -> {
            receivingAddressVos.forEach(receivingAddressVo -> {
                AddressModule addressModule = new AddressModule(receivingAddressVo.getCode()
                        , receivingAddressVo.getAddress()
                        , receivingAddressVo.getSupplierCode()
                        , receivingAddressVo.getSupplierName());
                addressModules.add(addressModule);
            });
        });

        try {
            String fileName = "收货地址" + System.currentTimeMillis() + ExcelTypeEnum.XLSX.getValue();
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("UTF-8");
            fileName = URLEncoder.encode(fileName, "UTF-8");
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName);
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
            EasyExcel.write(response.getOutputStream())
                    .head(AddressModule.class)
                    .registerWriteHandler(new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle))
                    .sheet("收货地址")
                    .useDefaultStyle(true)
                    .doWrite(addressModules);
        } catch (IOException ex) {
            log.error("downloadModel error!", ex);
        }
    }

}
