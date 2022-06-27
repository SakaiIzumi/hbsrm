package net.bncloud.delivery.xxljobtask;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.delivery.constant.DeliveryTaskConstants;
import net.bncloud.delivery.service.SupplyDemandBalanceService;
import org.springframework.stereotype.Component;

/**
 * desc: 供需平衡报表任务
 *
 * @author Rao
 * @Date 2022/03/07
 **/
@Slf4j
@Component
public class SupplyAndDemandBalanceTask {


    private final SupplyDemandBalanceService balanceService;
    public SupplyAndDemandBalanceTask(SupplyDemandBalanceService balanceService) {
        this.balanceService = balanceService;
    }

    /**
     * 供需平衡报表清洗数据任务
     * @return
     */
    @XxlJob(DeliveryTaskConstants.SUPPLY_AND_DEMAND_BALANCE_CLEAN_DATA)
    public ReturnT<String> supplyAndDemandBalanceCleanData(){

        XxlJobHelper.log("calculate supplyDemandBalanceReport start");

        //重新计算
        balanceService.batchCalculateReport(null);

        XxlJobHelper.log("calculate supplyDemandBalanceReport end");
        return ReturnT.SUCCESS;

    }

}
