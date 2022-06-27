package net.bncloud.delivery.controller;


import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.pageable.PageUtils;
import net.bncloud.common.util.BeanUtil;
import net.bncloud.common.util.StringUtil;
import net.bncloud.delivery.entity.DeliveryDetail;
import net.bncloud.delivery.entity.DeliveryNote;
import net.bncloud.delivery.entity.DeliveryNoteExcelModel;
import net.bncloud.delivery.param.DeliveryNoteParam;
import net.bncloud.delivery.param.DeliveryNoteSaveParam;
import net.bncloud.delivery.service.DeliveryDetailService;
import net.bncloud.delivery.service.DeliveryNoteService;
import net.bncloud.delivery.vo.DeliveryDetailVo;
import net.bncloud.delivery.vo.DeliveryNoteVo;
import net.bncloud.delivery.wrapper.DeliveryNoteWrapper;
import net.bncloud.service.api.platform.sys.feign.CfgParamResourceFeignClient;
import net.bncloud.support.Condition;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;


/**
 * 送货单（采购工作台）
 */

@RestController
@RequestMapping("/zc/delivery-note")
@Api(tags = "送货单信息控制器")
@Slf4j
@RequiredArgsConstructor
@SuppressWarnings({"all"})
public class PurchaseDeliveryNoteController {

    private final DeliveryNoteService deliveryNoteService;
    private final CfgParamResourceFeignClient cfgParamResourceFeignClient;
    private final DeliveryDetailService detailService;

    /**
     * feignTest
     */
    @GetMapping("/feignTest/{code}")
    @ApiOperation(value = "根据ID查询", notes = "传入DeliveryNote")
    public R feignTest(@PathVariable(value = "code") String code) {
        return cfgParamResourceFeignClient.getParamByCode(code);
    }

    /**
     * 根据送货单号查询
     */
    @GetMapping("/getOneByNo/{deliveryNo}")
    @ApiOperation(value = "根据送货单号查询", notes = "传入deliveryNo")
    public R<DeliveryNoteVo> getOneByNo(@PathVariable(value = "deliveryNo") String deliveryNo) {
        return R.data(deliveryNoteService.getDeliveryNoteInfoByNo(deliveryNo));
    }

    /**
     * 通过id查询
     */
    @GetMapping("/getById/{id}")
    @ApiOperation(value = "根据ID查询", notes = "传入DeliveryNote")
    public R<DeliveryNoteVo> getById(@PathVariable(value = "id") Long id) {
        return R.data(deliveryNoteService.getDeliveryNoteInfo(id));
    }

    /**
     * 新增
     */
    @PostMapping("/save")
    @ApiOperation(value = "新增", notes = "传入DeliveryNote")
    public R save(@RequestBody @Validated DeliveryNoteSaveParam deliveryNote) {
        return R.data(deliveryNoteService.saveDeliveryNote(deliveryNote));
    }

    /**
     * 通过id删除
     */
    @DeleteMapping("/deleteById/{id}")
    @ApiOperation(value = "通过id删除", notes = "传入ids")
    public R delete(@PathVariable(value = "id") String id) {
        deliveryNoteService.deleteDeliveryNote(Long.parseLong(id));
        return R.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    @ApiOperation(value = "修改", notes = "传入deliveryNote")
    public R updateById(@RequestBody @Validated DeliveryNoteSaveParam deliveryNote) {
        deliveryNoteService.updateDeliveryNote(deliveryNote);
        return R.success();
    }


    /**
     * 查询列表
     */
    @PostMapping("/list")
    @ApiOperation(value = "查询列表", notes = "传入deliveryNote")
    public R list(@RequestBody DeliveryNote deliveryNote) {
        List<DeliveryNote> list = deliveryNoteService.list(Condition.getQueryWrapper(deliveryNote));

        return R.data(list);
    }

    /**
     * 分页查询
     */
    @PostMapping("/page")
    @ApiOperation(value = "分页查询", notes = "传入DeliveryNoteParam")
    public R page(Pageable pageable, @RequestBody QueryParam<DeliveryNoteParam> queryParam) {
        IPage<DeliveryNote> page = deliveryNoteService.selectPageByOrgId(PageUtils.toPage(pageable), queryParam);
        IPage<DeliveryNoteVo> deliveryNoteVoIPage = DeliveryNoteWrapper.build().pageVO(page);

        //设置送货明细数据
        deliveryNoteVoIPage.getRecords().forEach(deliveryNoteVo -> {
            List<DeliveryDetail> detailList = detailService.list(new LambdaQueryWrapper<DeliveryDetail>().eq(DeliveryDetail::getDeliveryId, deliveryNoteVo.getId()));
            List<DeliveryDetailVo> detailVoList = BeanUtil.copy(detailList, DeliveryDetailVo.class);
            //设置明细中的COA附件列表
            deliveryNoteService.batchSetDeliveryDetailAttachment(detailVoList);
            //设置 是否有COA附件 属性的值
            detailVoList.forEach(detail -> {
                if (detail != null) {
                    if (detail.getAttachmentList().size() > 0)
                        detail.setAttachment("Y");
                    else if (detail.getAttachmentList().size() <= 0)
                        detail.setAttachment("N");
                }
            });
            deliveryNoteVo.setDeliveryDetailList(detailVoList);
        });

        return R.data(PageUtils.result(deliveryNoteVoIPage));
    }


    /**
     * 送货单数量统计
     *
     * @return
     */
    @GetMapping("/statistics")
    @ApiOperation(value = "送货单数量统计", notes = "传入DeliveryNoteParam")
    public R statistics() {
        return R.data(deliveryNoteService.statistics());
    }


    /**
     * 申请发货
     */
    @PostMapping("/applyDelivery/{id}")
    @ApiOperation(value = "申请发货", notes = "传入deliveryNote")
    public R applyDelivery(@PathVariable(value = "id") Long id) {
        return deliveryNoteService.applyDelivery(id);
    }

    /**
     * 作废申请
     */
    @PostMapping("/invalidApply/{id}")
    @ApiOperation(value = "作废申请", notes = "传入deliveryNote")
    public R invalidApply(@PathVariable(value = "id") Long id) {
        return deliveryNoteService.invalidApply(id);
    }

    /**
     * 撤回申请
     */
    @PostMapping("/withdrawApply/{id}")
    @ApiOperation(value = "撤回申请", notes = "传入deliveryNote")
    public R withdrawApply(@PathVariable(value = "id") Long id) {
        return deliveryNoteService.withdrawApply(id);
    }

    /**
     * 确认发出
     */
    @PostMapping("/confirmationIssued/{id}")
    @ApiOperation(value = "确认发出", notes = "传入deliveryNote")
    public R confirmationIssued(@PathVariable(value = "id") Long id) {
        return deliveryNoteService.confirmationIssued(id);
    }

    /**
     * 撤回送货
     */
    @PostMapping("/withdrawDelivery/{id}")
    @ApiOperation(value = "撤回送货", notes = "传入deliveryNote")
    public R withdrawDelivery(@PathVariable(value = "id") Long id) {
        return deliveryNoteService.withdrawDelivery(id);
    }

    /**
     * 货物已发
     */
    @PostMapping("/delivered/{id}")
    @ApiOperation(value = "货物已发", notes = "传入deliveryNote")
    public R delivered(@PathVariable(value = "id") Long id) {
        return deliveryNoteService.delivered(id);
    }

    /**
     * 作废送货
     */
    @PostMapping("/invalidDelivery/{id}")
    @ApiOperation(value = "作废送货", notes = "传入deliveryNote")
    public R invalidDelivery(@PathVariable(value = "id") Long id) {
        return deliveryNoteService.invalidDelivery(id);
    }

    /**
     * 提醒
     */
    @PostMapping("/remind/{id}")
    @ApiOperation(value = "提醒", notes = "传入deliveryNote")
    public R remind(@PathVariable(value = "id") Long id) {
        return deliveryNoteService.remind(id);
    }


    /**
     * 查询列表，供feign调用
     */
    @PostMapping("/feign/list")
    @ApiOperation(value = "查询列表", notes = "传入deliveryNote")
    public R feignList(@RequestBody String params) {
        List<DeliveryNote> list = deliveryNoteService.queryList(params);
        return R.data(list);
    }

    /**
     * 查询列表，供feign调用
     */
    @PostMapping("/feign/updateSettlementPoolSyncStatus")
    @ApiOperation(value = "查询列表", notes = "传入deliveryNote")
    public R updateSettlementPoolSyncStatus(@RequestBody List<Long> deliveryIdList) {
        boolean result = deliveryNoteService.updateSettlementPoolSyncStatus(deliveryIdList);
        return R.data(result);
    }

    /**
     * 查询列表，供feign调用
     */
    @PostMapping("/feign/queryListByDeliveryIds")
    @ApiOperation(value = "查询列表", notes = "传入deliveryNote")
    public R queryListByDeliveryIds(@RequestBody List<Long> deliveryIdList) {
        List<DeliveryNote> list = deliveryNoteService.queryListByDeliveryIds(deliveryIdList);
        return R.data(list);
    }


    /**
     * 导出
     *
     * @param response
     * @param queryParam
     */
    @PostMapping("/exportDeliveryNote")
    public void exportDeliveryNote(HttpServletResponse response, @RequestBody QueryParam<DeliveryNoteParam> queryParam) {
        List<DeliveryNoteExcelModel> excelModels = deliveryNoteService.getDeliveryNoteList(queryParam);
        try {
            String fileName = "送货单" + System.currentTimeMillis() + ExcelTypeEnum.XLSX.getValue();
            response.setContentType("application/vnd.ms-excel");
            // response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("UTF-8");
            // 这里URLEncoder.encode可以防止中文乱码
            fileName = URLEncoder.encode(fileName, "UTF-8");
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

            EasyExcel.write(response.getOutputStream(), DeliveryNoteExcelModel.class)
                    .head(DeliveryNoteExcelModel.class)
                    .registerWriteHandler(new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle))
                    .sheet("送货单")
                    //设置默认样式及写入头信息开始的行数
                    .useDefaultStyle(true).relativeHeadRowIndex(0)
                    .doWrite(excelModels);
        } catch (IOException e) {
            log.error("导出数据失败，错误信息：{}", JSON.toJSONString(e.getMessage()));
            e.printStackTrace();
        }
    }
}
