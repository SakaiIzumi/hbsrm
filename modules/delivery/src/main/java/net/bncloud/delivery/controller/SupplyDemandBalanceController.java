package net.bncloud.delivery.controller;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.pageable.PageUtils;
import net.bncloud.delivery.entity.SupplyDemandBalance;
import net.bncloud.delivery.entity.SupplyDemandDetailView;
import net.bncloud.delivery.param.SupplyDemandBalanceParam;
import net.bncloud.delivery.param.SupplyDemandDetailViewParam;
import net.bncloud.delivery.service.SupplyDemandBalanceService;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

/**
 * @author ddh
 * @description 供需平衡报表
 * @since 2022/4/8
 */

/**
 * 供需平衡报表
 */
@Slf4j
@RestController
@RequestMapping("/report")
public class SupplyDemandBalanceController {

    private final SupplyDemandBalanceService balanceService;

    private final StringRedisTemplate stringRedisTemplate;

    public SupplyDemandBalanceController(SupplyDemandBalanceService balanceService,StringRedisTemplate stringRedisTemplate) {
        this.balanceService = balanceService;
        this.stringRedisTemplate=stringRedisTemplate;
    }

    /**
     * 详情
     *
     * @param pageable   page=1&size=10
     * @param queryParam 高价搜索参数
     * @return
     */
    @PostMapping(value = "/details")
    public R<PageImpl<SupplyDemandDetailView>> getReportDetailList(Pageable pageable, @RequestBody @Valid @Validated QueryParam<SupplyDemandDetailViewParam> queryParam) {

        //时间处理 前端传的格式是 yyyy-MM-dd HH:mm:ss ,转换成 yyyy-MM-dd
        @Valid SupplyDemandDetailViewParam param = queryParam.getParam();
        if (param != null && param.getStartDate() != null && !"".equals(param.getEndDate())){
            param.setStartDate(param.getStartDate().substring(0, 10));
        }
        if (param != null && param.getEndDate() != null && !"".equals(param.getEndDate())){
            param.setEndDate(param.getEndDate().substring(0, 10));
        }
        return R.data(balanceService.selectReportDetailPage(PageUtils.toPage(pageable), queryParam));
    }


    /**
     * 导出详情
     *
     * @param response
     * @param queryParam
     * @throws IOException
     */
    @PostMapping(value = "/exportReportDetail")
    public void exportReportDetail(HttpServletResponse response, @RequestBody @Valid @Validated QueryParam<SupplyDemandDetailViewParam> queryParam) {
        try {
            balanceService.exportReportDetail(response, queryParam);
        } catch (IOException e) {
            log.error("导出供需平衡明细报表失败，{}", JSON.toJSONString(e.getMessage()));

        }
    }

    /**
     * 报表
     *
     * @param pageable
     * @param queryParam
     * @return
     */
    @PostMapping(value = "/page")
    public R<PageImpl<SupplyDemandBalance>> getReportList(Pageable pageable, @RequestBody @Valid @Validated QueryParam<SupplyDemandBalanceParam> queryParam) {
        return R.data(balanceService.selectReportPage(PageUtils.toPage(pageable), queryParam));
    }

    /**
     * 导出报表
     *
     * @param response
     * @param queryParam
     */
    @PostMapping(value = "/exportReport")
    public void exportReport(HttpServletResponse response, @RequestBody @Valid @Validated QueryParam<SupplyDemandBalanceParam> queryParam) {
        try {
            balanceService.exportReport(response, queryParam);
        } catch (IOException e) {
            log.error("导出供需平衡报表失败，{}", JSON.toJSONString(e.getMessage()));
        }
    }


    /**
     * 重新计算
     *
     * @return
     */
    @GetMapping("/recalculate")
    public R<String> recalculateReport() {
        try {
            balanceService.batchCalculateReport(null);
        } catch (Exception e) {
            log.error("重新计算失败，{}",JSON.toJSONString(e.getMessage()));
            return R.fail(e.getMessage());
        }
        return R.success();
    }

    /**
     * 缺货计算完成时间
     * @return
     */
    @GetMapping("/getCalculateTime")
    public R<String> getRecalculateTime(){
        String recalculateTime = stringRedisTemplate.opsForValue().get("record_recalculate_time");
        return R.data(recalculateTime);
    }

}
