package net.bncloud.delivery.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.common.api.ResultCode;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.base.repeatrequest.RepeatRequestOperation;
import net.bncloud.common.exception.ApiException;
import net.bncloud.common.pageable.PageUtils;
import net.bncloud.common.util.CollectionUtil;
import net.bncloud.common.util.DateUtil;
import net.bncloud.common.util.StringUtil;
import net.bncloud.delivery.entity.SupplyDemandBalance;
import net.bncloud.delivery.entity.SupplyDemandDetailView;
import net.bncloud.delivery.mapper.SupplyDemandBalanceMapper;
import net.bncloud.delivery.mapper.SupplyDemandDetailViewMapper;
import net.bncloud.delivery.param.SupplyDemandBalanceParam;
import net.bncloud.delivery.param.SupplyDemandDetailViewParam;
import net.bncloud.delivery.service.SupplyDemandBalanceService;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author ddh
 * @description
 * @since 2022/4/8
 */
@Slf4j
@Service
public class SupplyDemandBalanceServiceImpl implements SupplyDemandBalanceService {

    private final SupplyDemandBalanceMapper balanceMapper;
    private final SupplyDemandDetailViewMapper detailViewMapper;
    private final StringRedisTemplate redisTemplate;

    public SupplyDemandBalanceServiceImpl(SupplyDemandBalanceMapper balanceMapper, SupplyDemandDetailViewMapper detailViewMapper, StringRedisTemplate redisTemplate) {
        this.balanceMapper = balanceMapper;
        this.detailViewMapper = detailViewMapper;
        this.redisTemplate = redisTemplate;
    }


    /**
     * 分页查询供需平衡明细报表
     *
     * @param page
     * @param queryParam
     * @return
     */
    @Override
    public PageImpl<SupplyDemandDetailView> selectReportDetailPage(IPage<SupplyDemandDetailView> page, QueryParam<SupplyDemandDetailViewParam> queryParam) {
        List<SupplyDemandDetailView> views = detailViewMapper.selectPageList(page, queryParam);
        batchConvertTypeAndStatus(views);
        return PageUtils.result(page.setRecords(views));

    }

    /**
     * 导出供需平衡明细报表
     *
     * @param response
     * @param queryParam
     * @throws IOException
     */
    @Override
    public void exportReportDetail(HttpServletResponse response, QueryParam<SupplyDemandDetailViewParam> queryParam) throws IOException {
        List<SupplyDemandDetailView> pageList = detailViewMapper.selectPageList(null, queryParam);
        batchConvertTypeAndStatus(pageList);
        String fileName = "供需平衡明细报表" + System.currentTimeMillis() + ExcelTypeEnum.XLSX.getValue();
        response.setContentType("application/vnd.ms-excel");
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

        //excel如需下载到本地,只需要将response.getOutputStream()换成File即可(注释掉以上response代码)
        EasyExcel.write(response.getOutputStream(), SupplyDemandDetailView.class)
                //设置输出excel版本,不设置默认为xlsx
                .excelType(ExcelTypeEnum.XLSX)
                .head(SupplyDemandDetailView.class)
                //设置拦截器或自定义样式
                // .registerWriteHandler()
                .registerWriteHandler(new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle))
                .sheet("供需平衡明细报表")
                //设置默认样式及写入头信息开始的行数
                .useDefaultStyle(true).relativeHeadRowIndex(0)
                //这里的addsumColomn方法是个添加合计的方法,可删除
                .doWrite(pageList);
    }

    /**
     * 设置单据类型：由code转换成中文
     *
     * @param detail 报表明细
     */
    private void convertTypeAndStatus(SupplyDemandDetailView detail) {
        if (detail == null) {
            log.error("转换单据类型和单据状态时，传入的参数为null");
            throw new ApiException(ResultCode.FAILURE.getCode(), "转换单据类型和单据状态时，传入的参数为null");
        }
        if ("0".equals(detail.getBillType())) {
            detail.setBillType("订单");
            switch (detail.getBillStatus()) {
                case "1":
                    detail.setBillStatus("草稿");
                    break;
                case "2":
                    detail.setBillStatus("待答交");
                    break;
                case "3":
                    detail.setBillStatus("已留置");
                    break;
                case "4":
                    detail.setBillStatus("答交差异");
                    break;
                case "5":
                    detail.setBillStatus("退回");
                    break;
                case "6":
                    detail.setBillStatus("变更中");
                    break;
                case "7":
                    detail.setBillStatus("已确认");
                    break;
                case "8":
                    detail.setBillStatus("已完成");
                    break;
            }
        } else if ("1".equals(detail.getBillType())) {
            detail.setBillType("送货计划");
            switch (detail.getBillStatus()) {
                case "0":
                    detail.setBillStatus("草稿");
                    break;
                case "1":
                    detail.setBillStatus("待确认");
                    break;
                case "2":
                    detail.setBillStatus("已确认");
                    break;
            }
        } else if ("2".equals(detail.getBillType())) {
            detail.setBillType("送货单");
            switch (detail.getBillStatus()) {
                case "1":
                    detail.setBillStatus("草稿");
                    break;
                case "2":
                    detail.setBillStatus("待发货");
                    break;
                case "3":
                    detail.setBillStatus("申请中");
                    break;
                case "4":
                    detail.setBillStatus("申请作废");
                    break;
                case "5":
                    detail.setBillStatus("申请撤回");
                    break;
                case "6":
                    detail.setBillStatus("申请退回");
                    break;
                case "7":
                    detail.setBillStatus("已同意");
                    break;
                case "8":
                    detail.setBillStatus("部分同意");
                    break;
                case "9":
                    detail.setBillStatus("待签收");
                    break;
                case "10":
                    detail.setBillStatus("送货撤回");
                    break;
                case "11":
                    detail.setBillStatus("送货作废");
                    break;
                case "12":
                    detail.setBillStatus("送货退回");
                    break;
                case "13":
                    detail.setBillStatus("已完成");
                    break;
                case "14":
                    detail.setBillStatus("已冻结");
                    break;
                case "15":
                    detail.setBillStatus("签收确认中");
                    break;

            }
        } else {
            detail.setBillType("入库单");
            switch (detail.getBillStatus()) {
                case "not_signed":
                    detail.setBillStatus("未签收");
                    break;
                case "signed":
                    detail.setBillStatus("已签收");
                    break;
            }
        }
    }


    /**
     * 批量转换
     *
     * @param details 报表明细
     */
    private void batchConvertTypeAndStatus(List<SupplyDemandDetailView> details) {
        if (CollectionUtil.isNotEmpty(details)) {
            details.forEach(this::convertTypeAndStatus);
        }
    }


    /**
     * 计算：将报表明细中的数据统计到报表 t_supply_demand_balance中
     * 1.查询所有的产品（编码）
     * 2.逐个产品进行统计
     * 3.统计好之后，先删除旧的报表数据，再保存新的报表数据
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @RepeatRequestOperation
    public void batchCalculateReport(String supplier) {
        List<String> productCodes = detailViewMapper.getProductCodeByCondition(supplier);
        productCodes.forEach(this::calculateReport);
        //记录计算完成的时间
        String key = "record_recalculate_time";
        String value = DateTimeFormatter.ofPattern(DateUtil.PATTERN_DATETIME).format(LocalDateTime.now());
        redisTemplate.opsForValue().set(key, value);
    }


    /**
     * 根据产品获取报表数据
     *
     * @param
     */
    public void calculateReport(String productCode) {
        List<SupplyDemandBalance> balances = new ArrayList<>();
        //同一物料的所有单据类型的明细
        List<SupplyDemandDetailView> reportDetails = detailViewMapper.getDetailsByCode(productCode);
        //同一产品下明细的所有日期(升序)
        List<LocalDate> dateList = reportDetails.stream().map(SupplyDemandDetailView::getDate)
                .distinct()
                .sorted(LocalDate::compareTo)
                .collect(Collectors.toList());
        for (LocalDate date : dateList) {
            //某一日期的数据（包含订单明细，计划明细，送货单明细，入库单明细）
            List<SupplyDemandDetailView> oneDateDataList = reportDetails.stream().filter(detail -> date.equals(detail.getDate())).collect(Collectors.toList());
            //当前日期没有数据，跳过
            if (CollectionUtil.isEmpty(oneDateDataList)) {
                continue;
            }
            SupplyDemandDetailView view = oneDateDataList.get(0);
            SupplyDemandBalance balance = new SupplyDemandBalance()
                    .setProductCode(view.getProductCode())
                    .setProductName(view.getProductName())
                    .setMerchantCode(view.getMerchantCode())
                    .setDate(date);

            //订单需求数量
            List<SupplyDemandDetailView> orderDemandQuantityList = oneDateDataList.stream().filter(detail -> "0".equals(detail.getBillType())).collect(Collectors.toList());
            Optional.of(orderDemandQuantityList).ifPresent(list -> {
                double orderDemandQuantity = list.stream().mapToDouble(detailView -> Double.parseDouble(detailView.getQuantity())).sum();
                balance.setOrderDemandQuantity(String.valueOf(orderDemandQuantity));
            });


            //计划发货数量
            List<SupplyDemandDetailView> planDeliveryQuantityList = oneDateDataList.stream().filter(detail -> "1".equals(detail.getBillType())).collect(Collectors.toList());
            Optional.of(planDeliveryQuantityList).ifPresent(list -> {
                double planDeliveryQuantity = list.stream().mapToDouble(detailView -> Double.parseDouble(detailView.getQuantity())).sum();
                balance.setPlanDeliveryQuantity(String.valueOf(planDeliveryQuantity));
            });

            //当前日期的已确认未发货数量(计划项次中的剩余可发货数量)
            List<SupplyDemandDetailView> remainingQuantityList = oneDateDataList.stream().filter(detail -> "1".equals(detail.getBillType()))
                    .filter(detail -> "2".equals(detail.getBillStatus()))
                    .collect(Collectors.toList());
            Optional.of(remainingQuantityList).ifPresent(list -> {
                double remainQuantity = list.stream().mapToDouble(detailView -> Double.parseDouble(detailView.getQuantity())).sum();
                balance.setConfirmedUndeliveryQuantity(String.valueOf(remainQuantity));
            });

            //预计到货数量
            List<SupplyDemandDetailView> estimatedArrivalQuantityList = oneDateDataList.stream().filter(detail -> "2".equals(detail.getBillType())).collect(Collectors.toList());
            Optional.of(estimatedArrivalQuantityList).ifPresent(list -> {
                double estimatedArrivalQuantity = list.stream().mapToDouble(detailView -> Double.parseDouble(detailView.getQuantity())).sum();
                balance.setEstimatedArrivalQuantity(String.valueOf(estimatedArrivalQuantity));
            });


            //入库数量
            List<SupplyDemandDetailView> receiptQuantityList = oneDateDataList.stream().filter(detail -> "3".equals(detail.getBillType())).collect(Collectors.toList());
            Optional.of(receiptQuantityList).ifPresent(list -> {
                double receiptQuantity = list.stream().mapToDouble(detailView -> Double.parseDouble(detailView.getQuantity())).sum();
                balance.setReceiptQuantity(String.valueOf(receiptQuantity));
            });


            //供需结余=上一日期的供需结余+已确认未发货数量的展示值+预计到货数量+入库数量-计划发货数量
            //先求 供需结余=预计到货数量+入库数量-计划发货数量
            //double confirmedUndeliveryQuantityShow = 0.0;
            double estimatedArrivalQuantity = 0.0;
            double receiptQuantity = 0.0;
            double planDeliveryQuantity = 0.0;

            if (StringUtil.isNotBlank(balance.getReceiptQuantity())) {
                receiptQuantity = Double.parseDouble(balance.getReceiptQuantity());
            }
            if (StringUtil.isNotBlank(balance.getPlanDeliveryQuantity())) {
                planDeliveryQuantity = Double.parseDouble(balance.getPlanDeliveryQuantity());
            }
            if (StringUtil.isNotBlank(balance.getEstimatedArrivalQuantity())) {
                estimatedArrivalQuantity = Double.parseDouble(balance.getEstimatedArrivalQuantity());
            }
            double num = estimatedArrivalQuantity + receiptQuantity - planDeliveryQuantity;
            balance.setBalanceQuantity(String.valueOf(num));
            balances.add(balance);
        }

        //confirmedUndeliveryQuantityShow 已确认未发货展示值 显示在【系统日期+3天】和【计划日期】中更大的日期中
        LocalDate now = LocalDate.now();
        Optional<SupplyDemandBalance> nowConfirmedUndeliveryQuantityOpt = balances.stream().filter(balance -> now.equals(balance.getDate())).findFirst();
        if (nowConfirmedUndeliveryQuantityOpt.isPresent()) {
            SupplyDemandBalance supplyDemandBalance = nowConfirmedUndeliveryQuantityOpt.get();
            String nowConfirmedUndeliveryQuantity = supplyDemandBalance.getConfirmedUndeliveryQuantity();
            LocalDate nowPlusThreeDay = now.plus(3, ChronoUnit.DAYS);
            Optional<SupplyDemandBalance> nowPlusThreeDayConfirmedUndeliveryQuantityOpt = balances.stream().filter(balance -> nowPlusThreeDay.equals(balance.getDate())).findFirst();
            //系统日期+3天有报表数据，设置已确认未发货的展示值；没有报表数据，新建报表的数据再设值
            if (nowPlusThreeDayConfirmedUndeliveryQuantityOpt.isPresent()) {
                nowPlusThreeDayConfirmedUndeliveryQuantityOpt.get().setConfirmedUndeliveryQuantityShow(nowConfirmedUndeliveryQuantity);
            } else {
                SupplyDemandBalance newSupplyDemandBalance = new SupplyDemandBalance()
                        .setProductCode(supplyDemandBalance.getProductCode())
                        .setProductName(supplyDemandBalance.getProductName())
                        .setMerchantCode(supplyDemandBalance.getMerchantCode())
                        .setDate(nowPlusThreeDay)
                        .setConfirmedUndeliveryQuantityShow(nowConfirmedUndeliveryQuantity);
                balances.add(newSupplyDemandBalance);
            }
        }

        //日期升序排序
        balances = balances.stream().sorted(Comparator.comparing(SupplyDemandBalance::getDate)).collect(Collectors.toList());
        for (int i = 0; i < balances.size(); i++) {
            SupplyDemandBalance balance = balances.get(i);
            //供需结余=上一日期的供需结余+当前天的已确认未发货数量+ （预计到货数量+入库数量-计划发货数量）
            //上一天的供需结余
            double lastDateBalanceQuantity = 0.0;
            if (i >= 1) {
                SupplyDemandBalance lastDateBalance = balances.get(i-1);
                if (StringUtil.isNotBlank(lastDateBalance.getBalanceQuantity())) {
                    lastDateBalanceQuantity = Double.parseDouble(lastDateBalance.getBalanceQuantity());
                }
            }
            //当前天的供需结余
            double nowBalanceQuantity = 0.0;
            if (StringUtil.isNotBlank(balance.getBalanceQuantity())) {
                nowBalanceQuantity = Double.parseDouble(balance.getBalanceQuantity());
            }
            //当前天的已确认未发货展示值
            double confirmedUndeliveryQuantityShow = 0.0;
            if (StringUtil.isNotBlank(balance.getConfirmedUndeliveryQuantityShow())) {
                confirmedUndeliveryQuantityShow = Double.parseDouble(balance.getConfirmedUndeliveryQuantityShow());
            }
            balance.setBalanceQuantity(String.valueOf(nowBalanceQuantity + lastDateBalanceQuantity + confirmedUndeliveryQuantityShow));
        }

        //balances.forEach(balance -> {
        //    //供需结余=上一日期的供需结余+当前天的已确认未发货数量+ （预计到货数量+入库数量-计划发货数量）
        //
        //    //前一天的日期
        //    LocalDate beforeDate = balance.getDate().plus(-1L, ChronoUnit.DAYS);
        //    Optional<SupplyDemandBalance> beforeDateBalanceOpt = balances.stream().filter(data -> beforeDate.equals(data.getDate())).findFirst();
        //    beforeDateBalanceOpt.ifPresent(opt -> {
        //        //上一天的供需结余
        //        double beforeBalanceQuantity = 0.0;
        //        if (StringUtil.isNotBlank(opt.getBalanceQuantity())) {
        //            beforeBalanceQuantity = Double.parseDouble(opt.getBalanceQuantity());
        //        }
        //        //当前天的供需结余
        //        double nowBalanceQuantity = 0.0;
        //        if (StringUtil.isNotBlank(balance.getBalanceQuantity())) {
        //            nowBalanceQuantity = Double.parseDouble(balance.getBalanceQuantity());
        //        }
        //        //当前天的已确认未发货展示值
        //        double confirmedUndeliveryQuantityShow = 0.0;
        //        if (StringUtil.isNotBlank(balance.getConfirmedUndeliveryQuantityShow())) {
        //            confirmedUndeliveryQuantityShow = Double.parseDouble(balance.getConfirmedUndeliveryQuantityShow());
        //        }
        //        balance.setBalanceQuantity(String.valueOf(nowBalanceQuantity + beforeBalanceQuantity + confirmedUndeliveryQuantityShow));
        //    });
        //});

        //计算 未完成订单=同物料下所有日期的订单需求数量之和 - 同物料下所有日期的入库数量之和
        double orderDemandQuantitySum = balances.stream()
                .filter(balance -> StringUtil.isNotBlank(balance.getOrderDemandQuantity()))
                .mapToDouble(balance -> Double.parseDouble(balance.getOrderDemandQuantity())).sum();
        double receiptQuantitySum = balances.stream()
                .filter(balance -> StringUtil.isNotBlank(balance.getReceiptQuantity()))
                .mapToDouble(balance -> Double.parseDouble(balance.getReceiptQuantity())).sum();
        String outStandingOrderQuantity = String.valueOf(orderDemandQuantitySum - receiptQuantitySum);
        balances.forEach(balance -> balance.setOutstandingOrdersQuantity(outStandingOrderQuantity));

        //数据为0或0.0的不要展示为0或0.0，展示为null
        balances.forEach(balance -> {
            if ("0".equals(balance.getOrderDemandQuantity()) || "0.0".equals(balance.getOrderDemandQuantity())) {
                balance.setOrderDemandQuantity(null);
            }
            if ("0".equals(balance.getPlanDeliveryQuantity()) || "0.0".equals(balance.getPlanDeliveryQuantity())) {
                balance.setPlanDeliveryQuantity(null);
            }
            if ("0".equals(balance.getConfirmedUndeliveryQuantity()) || "0.0".equals(balance.getConfirmedUndeliveryQuantity())) {
                balance.setConfirmedUndeliveryQuantity(null);
            }
            if ("0".equals(balance.getConfirmedUndeliveryQuantityShow()) || "0.0".equals(balance.getConfirmedUndeliveryQuantityShow())) {
                balance.setConfirmedUndeliveryQuantityShow(null);
            }
            if ("0".equals(balance.getEstimatedArrivalQuantity()) || "0.0".equals(balance.getEstimatedArrivalQuantity())) {
                balance.setEstimatedArrivalQuantity(null);
            }
            if ("0".equals(balance.getReceiptQuantity()) || "0.0".equals(balance.getReceiptQuantity())) {
                balance.setReceiptQuantity(null);
            }
            if ("0".equals(balance.getOutstandingOrdersQuantity()) || "0.0".equals(balance.getOutstandingOrdersQuantity())) {
                balance.setOutstandingOrdersQuantity(null);
            }

        });

        // 某一日期中的数据为空，不保存
        balances.removeIf(balance -> balance.getOrderDemandQuantity() == null && balance.getPlanDeliveryQuantity() == null && balance.getConfirmedUndeliveryQuantity() == null
                && balance.getConfirmedUndeliveryQuantityShow() == null && balance.getEstimatedArrivalQuantity() == null && balance.getReceiptQuantity() == null
                && balance.getBalanceQuantity() == null);


        //有存在的旧的报表数据，删除
        LambdaQueryWrapper<SupplyDemandBalance> queryWrapper = Wrappers.<SupplyDemandBalance>lambdaQuery().eq(SupplyDemandBalance::getProductCode, productCode);
        List<SupplyDemandBalance> balanceList = balanceMapper.selectList(queryWrapper);
        if (CollectionUtil.isNotEmpty(balanceList)) {
            balanceMapper.delete(queryWrapper);
        }
        //保存新的报表数据
        balances.forEach(balanceMapper::insert);


    }


    /**
     * 分页查询报表
     * 需求：根据供应商搜索时，希望可以看到该供应商的报表数据
     * 1.先删除旧的报表中的所有数据
     * 2.根据供应商过滤出明细表中的产品
     * 3.根据产品重新计算，将计算的结果保存到t_supply_demand_balance
     * 4.分页查询报表中的数据
     *
     * @param page
     * @param queryParam
     * @return
     */
    @Override
    public PageImpl<SupplyDemandBalance> selectReportPage(IPage<SupplyDemandBalance> page, QueryParam<SupplyDemandBalanceParam> queryParam) {
        @Valid SupplyDemandBalanceParam param = queryParam.getParam();
        String supplier = null;
        if (StringUtil.isNotBlank(param.getSupplier()) || StringUtil.isNotBlank(queryParam.getSearchValue())) {
            supplier = StringUtil.isNotBlank(param.getSupplier()) ? param.getSupplier() : queryParam.getSearchValue();
        }
        //1.清空表中的数据
        balanceMapper.cleanTable();
        this.batchCalculateReport(supplier);
        return PageUtils.result(page.setRecords(balanceMapper.selectReportPage(page, queryParam)));
    }

    /**
     * 导出报表数据
     *
     * @param response
     * @param queryParam
     * @throws IOException
     */
    @Override
    public void exportReport(HttpServletResponse response, QueryParam<SupplyDemandBalanceParam> queryParam) throws IOException {
        List<SupplyDemandBalance> balances = balanceMapper.selectReportPage(null, queryParam);
        String fileName = "供需平衡报表" + System.currentTimeMillis() + ExcelTypeEnum.XLSX.getValue();
        response.setContentType("application/vnd.ms-excel");
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

        //excel如需下载到本地,只需要将response.getOutputStream()换成File即可(注释掉以上response代码)
        EasyExcel.write(response.getOutputStream(), SupplyDemandBalance.class)
                //设置输出excel版本,不设置默认为xlsx
                .excelType(ExcelTypeEnum.XLSX)
                .head(SupplyDemandBalance.class)
                //设置拦截器或自定义样式
                // .registerWriteHandler()
                .registerWriteHandler(new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle))
                .sheet("供需平衡报表")
                //设置默认样式及写入头信息开始的行数
                .useDefaultStyle(true).relativeHeadRowIndex(0)
                //这里的addsumColomn方法是个添加合计的方法,可删除
                .doWrite(balances);
    }

}
