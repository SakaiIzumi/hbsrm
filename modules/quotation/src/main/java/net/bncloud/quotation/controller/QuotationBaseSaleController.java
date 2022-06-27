package net.bncloud.quotation.controller;


import com.alibaba.excel.EasyExcel;
import io.swagger.annotations.ApiOperation;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.pageable.PageUtils;
import net.bncloud.common.util.DateUtil;
import net.bncloud.quotation.entity.QuotationBase;
import net.bncloud.quotation.entity.QuotationLineExt;
import net.bncloud.quotation.param.ComputeLineExtPriceParam;
import net.bncloud.quotation.param.QuotationBaseParam;
import net.bncloud.quotation.service.QuotationBaseService;
import net.bncloud.quotation.vo.*;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 询价基础信息-销售协同
 * @author Auto-generator
 * @since 2022-02-14
 */
@RestController
@RequestMapping("/quotation-base/sale-synergy")
public class QuotationBaseSaleController {

    
    private final QuotationBaseService quotationBaseService;

    public QuotationBaseSaleController(QuotationBaseService quotationBaseService) {
        this.quotationBaseService = quotationBaseService;
    }


    /**
    * 询价单详情查询接口
    */
    @GetMapping("{id}/getById")
    @ApiOperation(value = "根据ID查询", notes = "传入QuotationBase")
    public R<QuotationBaseVo> getById(@PathVariable(value = "id") Long id) throws Exception {
        return R.data(quotationBaseService.getSaleInfoById(id));
    }



    /**
     * 询价单分页列表查询接口
     * @param pageable
     * @param pageParam
     * @return
     */
    @PostMapping("/page")
    @ApiOperation(value = "查询列表", notes = "传入QuotationBase")
    public R<PageImpl<QuotationBaseVo>> page(Pageable pageable, @RequestBody(required = false)  QueryParam<QuotationBaseParam> pageParam) throws Exception {
        return R.data(PageUtils.result(quotationBaseService.selectQuotationBaseSalePage(PageUtils.toPage(pageable),pageParam)));
    }

    /**
     * 询价单列表导出接口
     * @param fileName
     * @param pageParam
     * @param servletResponse
     */
    @GetMapping("/exportSaleExcelData")
    public void exportSaleExcelData(@RequestParam(required = false,defaultValue = "销售协同表") String fileName,@RequestBody(required = false) QueryParam<QuotationBaseParam> pageParam,HttpServletResponse servletResponse) throws Exception {
        quotationBaseService.exportSaleExcelData(fileName,pageParam,servletResponse);
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
        List<QuotationBase> list = quotationBaseService.querySaleList(queryParam);
        List<QuotationBaseSaleData> data = quotationBaseService.convertSaleDictCode(list);
        EasyExcel.write(response.getOutputStream(),QuotationBaseSaleData.class).sheet("物料询价").doWrite(data);
    }

    /**
     * 保存供应商应标确认信息接口
     * @param quotationId 单据ID
     */
    @PostMapping("/confirmMark/{quotationId}")
    public R<String> confirmMarked(@PathVariable(value = "quotationId") Long quotationId) throws Exception {
        return quotationBaseService.confirmMarked(quotationId);
    }

    /**
     * 保存供应商报价信息接口
     * @return
     */
    @PostMapping("/saveQuotePriceInfo")
    public R<String> saveQuotePriceInfo(@RequestBody SaveQuotePriceInfoVo saveQuotePriceInfoVo) throws Exception {
        quotationBaseService.saveQuotePriceInfo(saveQuotePriceInfoVo.getQuotationId(),saveQuotePriceInfoVo.getQuotationLineExts());
        return R.success();
    }

    /**
     * 计算询价单公式价格
     * @param computeLineExtPriceParam
     */
    @PostMapping("/computeLineExtPrice")
    public R<List<QuotationLineExtVo>> computeLineExtPrice(@RequestBody ComputeLineExtPriceParam computeLineExtPriceParam) {
        List<QuotationLineExtVo> resultQuotationLineExtList = quotationBaseService.computeLineExtPrice(computeLineExtPriceParam.getQuotationId(),computeLineExtPriceParam.getQuotationLineExtList());
        return R.data(resultQuotationLineExtList);
    }

    /**
     * 获取询价单各种状态的数量
     * 当前权限 匹配当前用户的供应商
     */
    @GetMapping("/getQuotationStaticCount")
    public R<QuotationStaticCountVo> getQuotationStaticCount(){
        return R.data(quotationBaseService.getQuotationStaticCount());
    }

    /**
     * 获取询价单单个的数量
     * 当前权限 匹配当前用户的供应商
     * @param status 询价单状态，draft草稿，quotation报价中，bid_opening待开标，comparison比价中，failure_bid流标，have_pricing已定价，obsolete已作废，fresh新的轮次，no_quotation 未报价
     */
    @GetMapping("/getQuotationSingleStaticCount/{status}")
    public R<Integer> getQuotationSingleStaticCount(@PathVariable("status") String status){
        return R.data(quotationBaseService.getQuotationSingleStaticCount(status));
    }
}
