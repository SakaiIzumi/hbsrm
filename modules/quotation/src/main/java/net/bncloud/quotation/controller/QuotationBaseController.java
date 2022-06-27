package net.bncloud.quotation.controller;


import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.base.BaseEntity;
import net.bncloud.common.util.DateUtil;
import net.bncloud.quotation.param.QuotationBaseRestateParam;
import net.bncloud.quotation.service.QuotationBaseCommonService;
import net.bncloud.quotation.vo.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.support.Condition;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Pageable;
import net.bncloud.common.pageable.PageUtils;

import net.bncloud.quotation.service.QuotationBaseService;
import net.bncloud.quotation.entity.QuotationBase;
import net.bncloud.quotation.param.QuotationBaseParam;

import javax.servlet.http.HttpServletResponse;
import javax.validation.groups.Default;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;


/**
 * 询价基础信息 前端控制器
 *
 * @author Auto-generator
 * @since 2022-02-14
 */
@RestController
@RequestMapping("/quotation-base")
public class QuotationBaseController {

    
    private final QuotationBaseService quotationBaseService;

    private final QuotationBaseCommonService quotationBaseCommonService;

    public QuotationBaseController(QuotationBaseService quotationBaseService, QuotationBaseCommonService quotationBaseCommonService) {
        this.quotationBaseService = quotationBaseService;
        this.quotationBaseCommonService = quotationBaseCommonService;
    }


    /**
    * 通过id查询
    */
    @GetMapping("{id}/getById")
    @ApiOperation(value = "根据ID查询", notes = "传入QuotationBase")
    public R<QuotationBaseVo> getById(@PathVariable(value = "id") Long id) throws Exception {
        return R.data(quotationBaseService.getInfoById(id));
    }

    /**
    * 新增
    */
    @PostMapping("/save")
    @ApiOperation(value = "新增", notes = "传入QuotationBase")
    public R save(@RequestBody @Validated QuotationBaseVo quotationBase){
        Long quotationBaseId = quotationBaseService.saveInfo(quotationBase);
        return R.data(quotationBaseService.getInfoById(quotationBaseId));
    }

    /**
    * 通过id删除
    */
    @DeleteMapping("/deleteById/{id}")
    @ApiOperation(value = "通过id删除", notes = "传入id")
    public R delete(@PathVariable(value = "id") String id){
        quotationBaseService.removeById(Long.parseLong(id));
        return R.success();
    }

    /**
    * 修改
    */
    @PutMapping("/update")
    @ApiOperation(value = "修改", notes = "传入quotationBase")
    public R updateById(@RequestBody @Validated({Default.class,BaseEntity.Update.class}) QuotationBaseVo quotationBase){
        quotationBaseService.updateInfo(quotationBase);
        return R.data(quotationBaseService.getInfoById(quotationBase.getId()));
    }


    /**
    * 查询列表
    */
    @PostMapping("/list")
    @ApiOperation(value = "查询列表", notes = "传入quotationBase")
    public R list(@RequestBody QuotationBase quotationBase ){
        List<QuotationBase> list = quotationBaseService.list(Condition.getQueryWrapper(quotationBase));
        return R.data(list);
    }

    /**
    * 分页查询
    */
    @PostMapping("/page")
    @ApiOperation(value = "查询列表", notes = "传入QuotationBaseParam")
    public R page(Pageable pageable, @RequestBody QueryParam<QuotationBaseParam> pageParam){
        IPage<QuotationBaseVo> pageVO  = quotationBaseService.selectPage(PageUtils.toPage(pageable), pageParam);
		return R.data(PageUtils.result(pageVO));
    }

    /**
     * 复制询价单详情
     */
    @GetMapping("/copyQuotation/{id}")
    @ApiOperation(value = "复制询价单详情", notes = "传入id")
    public R copyQuotation(@PathVariable(value = "id") Long id){
        quotationBaseService.copyQuotation(id);
        return  R.success();
    }

    /**
     * 物料询价列表导出接口
     */
    @PostMapping("/download")
    public void download(HttpServletResponse response,@RequestBody QueryParam<QuotationBaseParam> queryParam ) throws IOException {
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码
        String timeStamp = DateUtil.format(new Date(), DateUtil.PATTERN_DATETIME_NUM);
        String fileName = URLEncoder.encode("物料询价"+timeStamp, "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        List<QuotationBase> list = quotationBaseService.queryList(queryParam);
        List<QuotationBaseData> data = quotationBaseService.convertDictCode(list);
        EasyExcel.write(response.getOutputStream(),QuotationBaseData.class).sheet("物料询价").doWrite(data);
    }
    /**
     * 修改报价截止时间
     */
    @PutMapping("/updateDate")
    @ApiOperation(value = "修改报价截止时间", notes = "传入quotationBase")
    public R updateDate(@RequestBody @Validated({BaseEntity.Update.class}) QuotationUpdateDateVo quotationUpdateDateVo){
        quotationBaseService.updateDate(quotationUpdateDateVo);
        return R.success();
    }


    /**
     * 导出excel报价单详细页
     */
    @PostMapping("/exportData")
    @ApiOperation(value = "导出报价单详细页", notes = "传入quotationBase")
    public void  exportData(HttpServletResponse response,@RequestBody QueryParam<QuotationBase> queryParam){
        quotationBaseService.exportData(queryParam.getParam(),response);
//        return R.success();
    }

    /**
     * 通用保存接口，不推荐使用
     * @param quotationBaseCommonVO 询价单通用VO
     * @return 响应结果
     */
    @PostMapping("/commonSave")
    public R commonSave(@RequestBody QuotationBaseCommonVO quotationBaseCommonVO){
        quotationBaseCommonService.commonSave(quotationBaseCommonVO);
        return R.success();
    }


    /**
     * 修改询价单作废状态
     * 询价单状态，failure_bid流标，obsolete已作废
     */
    @PutMapping("/updateDisableStatus/{id}")
    @ApiOperation(value = "修改", notes = "传入quotationBase")
    public R updateDisableStatus(@PathVariable(value = "id") Long id){
        quotationBaseService.updateDisableStatus(id);
        return R.success();
    }


    /**
     * 修改询价单流标
     * 询价单状态，failure_bid流标，obsolete已作废
     */
    @PutMapping("/updateInvalideStatus/{id}")
    @ApiOperation(value = "修改", notes = "传入quotationBase")
    public R updateInvalideStatus(@PathVariable(value = "id") Long id){
        quotationBaseService.updateInvalideStatus(id);
        return R.success();
    }


    /**
     * 询价单重报
     */
    @PutMapping("/restate/do/{quotationBaseId}")
    @ApiOperation(value = "询价单重报", notes = "传入quotationBaseId,isPushCheapest,supplierId")
    public R restate(@PathVariable(value = "quotationBaseId") Long quotationBaseId, @RequestBody QuotationBaseRestateParam quotationBaseRestateParam){
        quotationBaseService.restateDo(quotationBaseId,quotationBaseRestateParam);
        return R.success();
    }


    /**
     * 重报校验接口-郑湘侠
     */
    @PostMapping("/restate/check/{quotationBaseId}")
    @ApiOperation(value = "重报校验接口", notes = "传入quotationBaseId")
    public R markedSupplier(@PathVariable(value = "quotationBaseId") Long quotationBaseId) {
        quotationBaseService.restateCheck(quotationBaseId);
        return R.success();
    }

    /**
     * 询价单数量统计接口
     */
    @PostMapping("/count")
    @ApiOperation(value = "重报校验接口", notes = "传入quotationBaseId")
    public R count(@RequestBody QuotationBase quotationBase) {
        int count = quotationBaseService.count(Condition.getQueryWrapper(quotationBase));
        return R.data(count);
    }


    /**
     * 获取询价单各种状态的数量
     */
    @GetMapping("/statics")
    public R<QuotationStaticCountVo> statics(){
        return R.data(quotationBaseService.statics());
    }

    /**
     * 发布询价单
     */
    @PostMapping("/{quotationBaseId}/publish")
    @ApiOperation(value = "发布询价单", notes = "传入quotationBaseId")
    public R<Long> publish(@PathVariable(name = "quotationBaseId") Long quotationBaseId) {
        return R.data(quotationBaseService.publish(quotationBaseId));
    }

    /**
     * 获取预警信息接口
     */
    @PostMapping("/{quotationBaseId}/selectEarlyWranning")
    @ApiOperation(value = "获取预警信息接口", notes = "传入quotationBaseId")
    public R<EarlyWranningVo> selectEarlyWranning(@PathVariable(name = "quotationBaseId") Long quotationBaseId) {
        return R.data(quotationBaseService.selectEarlyWranning(quotationBaseId));
    }

    /**
     * 修改询价单预警设置
     */
    @PostMapping("/{quotationBaseId}/earlyWranningSwitch")
    @ApiOperation(value = "修改询价单预警设置", notes = "传入quotationBaseId")
    public R<Boolean> earlyWranningSwitch(@PathVariable(name = "quotationBaseId") Long quotationBaseId) {
        return R.data(quotationBaseService.earlyWranningSwitch(quotationBaseId));
    }

}
