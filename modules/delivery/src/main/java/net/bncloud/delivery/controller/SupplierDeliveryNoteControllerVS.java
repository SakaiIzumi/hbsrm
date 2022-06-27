package net.bncloud.delivery.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.pageable.PageUtils;
import net.bncloud.common.util.BeanUtil;
import net.bncloud.delivery.entity.DeliveryDetail;
import net.bncloud.delivery.entity.DeliveryNote;
import net.bncloud.delivery.entity.DeliveryNoteExcelImportDetailVo;
import net.bncloud.delivery.entity.DeliveryNoteExcelModel;
import net.bncloud.delivery.param.DeliveryDetailBatchDetailParams;
import net.bncloud.delivery.param.DeliveryNoteImportDetailParam;
import net.bncloud.delivery.param.DeliveryNoteParam;
import net.bncloud.delivery.param.DeliveryNoteSaveParam;
import net.bncloud.delivery.service.DeliveryDetailService;
import net.bncloud.delivery.service.DeliveryNoteServiceVS;
import net.bncloud.delivery.service.listener.TextCellWriteHandle;
import net.bncloud.delivery.vo.DeliveryDetailBatchDetailVo;
import net.bncloud.delivery.vo.DeliveryDetailExcelImportResult;
import net.bncloud.delivery.vo.DeliveryDetailVo;
import net.bncloud.delivery.vo.DeliveryNoteVo;
import net.bncloud.delivery.wrapper.DeliveryNoteWrapper;
import net.bncloud.support.Condition;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 由于产品的关系，送货单接口需要大改，交付日期临近，
 * 为了保证过去的业务正常运行，建立灰度接口
 *
 */
@RestController
@RequestMapping("/zy/delivery-note/vs")
@RequiredArgsConstructor
@Slf4j
public class SupplierDeliveryNoteControllerVS {


    private final DeliveryNoteServiceVS deliveryNoteService;

    private final DeliveryDetailService detailService;


    /**
     * 通过id查询
     */
    @GetMapping("/getById/{id}")
    @ApiOperation(value = "根据ID查询", notes = "传入DeliveryNote")
    public R<DeliveryNoteVo> getById(@PathVariable(value = "id") Long id) {
        return R.data(deliveryNoteService.getDeliveryNoteInfo(id));
    }

    /**
     * 新增页 -- 保存
     */
    @PostMapping("/save")
    @ApiOperation(value = "新增", notes = "传入DeliveryNote")
    public R save(@RequestBody @Validated DeliveryNoteSaveParam deliveryNote) {
        return R.data(deliveryNoteService.saveDeliveryNote(deliveryNote));
    }

    /**
     * 1、新增页 -- 发布
     */
    @PostMapping("/saveSign")
    @ApiOperation(value = "确认发布", notes = "送货单")
    public R saveAndSign(@RequestBody @Validated DeliveryNoteSaveParam deliveryNote) {
        return R.data(deliveryNoteService.saveAndSign(deliveryNote));
    }


    /**
     * 1、待签收编辑页 -- 发布 （会更新收料通知单）
     * 2、草稿详情编辑页 -- 保存（不创建收料通知单，因为无erpId）
     */
    @PutMapping("/update")
    @ApiOperation(value = "修改", notes = "传入deliveryNote")
    public R updateById(@RequestBody @Validated DeliveryNoteSaveParam deliveryNote) {
        deliveryNoteService.updateDeliveryNote(deliveryNote);
        return R.success();
    }

    /**
     * 草稿编辑页 -- 发布
     */
    @PutMapping("/updateSign")
    @ApiOperation(value = "修改", notes = "传入deliveryNote")
    public R updateSign(@RequestBody @Validated DeliveryNoteSaveParam deliveryNote) {
        deliveryNoteService.updateSign(deliveryNote);
        return R.success();
    }

    /**
     * 修改
     */
    @Deprecated
    @PutMapping("/updateV")
    @ApiOperation(value = "修改", notes = "传入deliveryNote")
    public R updateByIdV2(Long id) {
        deliveryNoteService.updateDeliveryNoteV2(id);
        return R.success();
    }

    /**
     * 草稿详情页 -- 确认发货
     */
    @PutMapping("/updateStatus/{id}")
    public R updateStatus(@PathVariable("id") Long id) {
        deliveryNoteService.updateStatus(id);
        return R.success();
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
     * 分页查询
     */
    @PostMapping("/page")
    @ApiOperation(value = "分页查询", notes = "传入DeliveryNoteParam")
    public R page(Pageable pageable, @RequestBody QueryParam<DeliveryNoteParam> queryParam) {
        IPage<DeliveryNote> page = deliveryNoteService.selectPage(PageUtils.toPage(pageable), queryParam);
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
                    if (detail.getAttachmentList().size() > 0) {
                        detail.setAttachment("Y");
                    } else if (detail.getAttachmentList().size() <= 0) {
                        detail.setAttachment("N");
                    }
                }
            });
            deliveryNoteVo.setDeliveryDetailList(detailVoList);
        });

        //设置权限按钮
        deliveryNoteService.buildPermissionButtonBatch(deliveryNoteVoIPage.getRecords());
        return R.data(PageUtils.result(deliveryNoteVoIPage));
    }

    /**
     * 待签收数量统计
     *
     * @return
     */
    @GetMapping("/count")
    @ApiOperation(value = "送货单数量统计", notes = "")
    public R count() {
        return R.data(deliveryNoteService.selectToBeSignCount());
    }

    /**
     * 全部的数量统计
     *
     * @return
     */
    @GetMapping("/statistics")
    @ApiOperation(value = "全部的数量统计", notes = "")
    public R statistics() {
        return R.data(deliveryNoteService.selectStatistics());
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
     * 批量发货（已弃用）
     *
     * @return
     */
    @Deprecated
    @PostMapping("/batchShipment")
    @ApiOperation(value = "批量发货")
    public R<Long> batchShipment(@RequestBody List<String> itemIds) {
        return R.data(deliveryNoteService.batchDelivery(itemIds));
    }

    /**
     * 导出
     * @param response
     * @param queryParam
     * @return
     */
    @PostMapping("/exportDeliveryNote")
    public void exportDeliveryNote(HttpServletResponse response, @RequestBody QueryParam<DeliveryNoteParam> queryParam){
        List<DeliveryNoteExcelModel> excelModels = deliveryNoteService.getDeliveryNoteList(queryParam);
        try {
            String fileName = "送货单"+System.currentTimeMillis()+ ExcelTypeEnum.XLSX.getValue();
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

            EasyExcel.write(response.getOutputStream(), DeliveryNoteExcelModel.class)
                    .head(DeliveryNoteExcelModel.class)
                    .registerWriteHandler(new HorizontalCellStyleStrategy(headWriteCellStyle,contentWriteCellStyle))
                    .sheet("送货单")
                    //设置默认样式及写入头信息开始的行数
                    .useDefaultStyle(true).relativeHeadRowIndex(0)
                    .doWrite(excelModels);
        } catch (IOException e) {
            log.error("导出数据失败，错误信息：{}", JSON.toJSONString(e.getMessage()),e);
        }

    }

    /**
     * 对账模块调用,通过收料通知单号获取送货单号
     *  @return
     */
    @PostMapping("/getDeliveryNoteNo")
    public R<DeliveryNote> getDeliveryNoteNo(@RequestBody String fNumber){
        //无法通过实体类去获取对应的fNumber（收料通知单号）的数据，所以换成了String接收
        Map mapTypes = JSON.parseObject(fNumber);
        DeliveryNote deliveryNote=new DeliveryNote();
        deliveryNote.setFNumber(mapTypes.get("fNumber").toString());
        DeliveryNote one = deliveryNoteService.getOne(Condition.getQueryWrapper(deliveryNote));
        return R.data(one);
    }

    /**
     * 下载模板
     * @param response
     * @return
     */
    @GetMapping("/downloadModel")
    @ApiOperation("下载模板")
    @Deprecated
    public R<String> downloadModel(HttpServletResponse response)  {
        try {
            String fileName = "送货单新建导入" + System.currentTimeMillis() + ExcelTypeEnum.XLSX.getValue();
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("UTF-8");
            // 这里URLEncoder.encode可以防止中文乱码
            fileName = URLEncoder.encode(fileName, "UTF-8");
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName);
            EasyExcel.write(response.getOutputStream()).head(DeliveryNoteExcelImportDetailVo.class).sheet("送货明细").registerWriteHandler( new TextCellWriteHandle() ).doWrite(new ArrayList<DeliveryNoteExcelImportDetailVo>());
        } catch (IOException ex){
            log.error("downloadModel error!",ex);
            return R.fail("生成Excel失败,请联系管理人员！");
        }
        return R.success("生成成功！");
    }

    /**
     * 送货详情 --> excel导入明细
     * @param file
     * @param deliveryNoteImportDetailParam
     * @return
     */
    @PostMapping("/importDetail")
    public R<DeliveryDetailExcelImportResult> importDetail(@RequestParam("file") MultipartFile file, DeliveryNoteImportDetailParam deliveryNoteImportDetailParam) {
        DeliveryDetailExcelImportResult deliveryDetailExcelImportResult = deliveryNoteService.importDetail(file, deliveryNoteImportDetailParam);

        return R.data( deliveryDetailExcelImportResult );
    }

    /**
     * 查询导入状态
     * @param deliveryNoteId 送货单ID
     * @return
     */
    @GetMapping("/{id}/importDetail-status")
    public R<DeliveryDetailExcelImportResult> getImportDetailStatus(@NotNull(message = "未指定送货单！") @PathVariable("id") Long deliveryNoteId){
        DeliveryDetailExcelImportResult importDetailStatus = deliveryNoteService.getImportDetailStatus(deliveryNoteId);
        return R.data( importDetailStatus );
    }

    /**
     * 获取导入明细的excel模板地址  现在是以附件ID的形式返回  请求路径未做修改
     * @return
     */
    @GetMapping("/getImportDetailExcelTemplateUrl")
    public R<Map<String,Long>> getImportDetailExcelAttachmentId(){
        return R.data( deliveryNoteService.getImportDetailExcelAttachmentId());
    }

    /**
     * 分批发货 --> 批次详情
     * @return
     */
    @GetMapping("deliveryDetail/batchDetail")
    public R<DeliveryDetailBatchDetailVo> getDeliveryDetailBatchDetail(@Validated DeliveryDetailBatchDetailParams deliveryDetailBatchDetailParams){
        return R.data(deliveryNoteService.getDeliveryDetailBatchDetail(deliveryDetailBatchDetailParams));
    }

    /**
     * 分批发货 --> 批次详情保存
     * @return
     */
    @PostMapping("deliveryDetail/batchDetailSave")
    public R<DeliveryNoteVo> saveDeliveryDetailBatchDetailSave(@RequestBody @Validated @Valid DeliveryNoteSaveParam deliveryNoteSaveParam){
        DeliveryNoteVo deliveryNoteVo = deliveryNoteService.saveDeliveryDetailBatchDetailSave(deliveryNoteSaveParam);
        return R.data( deliveryNoteVo);
    }

}
